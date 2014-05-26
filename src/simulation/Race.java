package simulation;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import utilities.Globals;
import utilities.Needs;
import utilities.NeedsController.NeedsControlled;

public class Race implements NeedsControlled{
	private Animal[][] species;
	private AtomicInteger[][] lockArray;
	private String specName;
	protected AtomicInteger numberOfInstances = new AtomicInteger();
	protected Semaphore allowedWorker;

	public Race(String specName) {
		allowedWorker = new Semaphore(20, true);
		
		this.specName = specName;
		species = new Animal[Globals.height][Globals.width];
		lockArray = new AtomicInteger[Globals.height][Globals.width];
		
		//initiates the lockArray.
		for(int x = 0; x<Globals.width;x++){
			for(int y = 0; y<Globals.height;y++){
				lockArray[x][y] = new AtomicInteger(1);
			}
		}
	}

	public String getSpecies() {
		return specName;
	}

	public Animal getSpeciesAt(int x, int y) {
		return species[x][y];
	}
	
	/** Removes and returns the animal at the coordinates (x, y), if the cell is unlocked.
	 * 
	 * @param x Coordinate to get from.
	 * @param y Coordinate to get from.
	 * @return An animal if the cell is unlocked, else it returns null.
	 */

	public Animal getAndRemoveSpeciesAt(int x, int y){
		if(compareAndSet(x, y)){
			Animal animal = species[x][y];
			species[x][y] = null;
			unlock(x, y);
			return animal;
		}
		return null;
	}
	
	/** Moves an animal from (currentX, currentY) to (newX, newY), if that cell is unlocked and there is nothing else
	 *  on that cell.
	 * 
	 * @param currentX Current x coordinate.
	 * @param currentY Current y coordinate.
	 * @param newX Coordinate which the object want to move to.
	 * @param newY Coordinate which the object want to move to.
	 * @return True if successfully moved, else false.
	 */

	public boolean moveSpecies(int currentX, int currentY, int newX, int newY){
		
		Race tree = null;
		Race sheep = null;
		Race wolf = null;
		
		if(newX == currentX && newY == currentY){
			return true;
		}
		// Find the races.
		for(Race race : Globals.races){
			if(race.getSpecName().equals("Wolf")){
				wolf = race;
			}else if(race.getSpecName().equals("Tree")){
				tree = race;
			}else if(race.getSpecName().equals("Sheep")){
				sheep = race;
			}
		}
		
		if(!tree.containsAnimal(newX, newY) && tree.compareAndSet(newX, newY)){
			if(!wolf.containsAnimal(newX, newY) && wolf.compareAndSet(newX, newY)){
				if(!sheep.containsAnimal(newX, newY) && sheep.compareAndSet(newX, newY)){
					
					Animal animal = getAndRemoveSpeciesAt(currentX, currentY);
					if(animal == null){
						tree.unlock(newX, newY);
						wolf.unlock(newX, newY);
						sheep.unlock(newX, newY);
						return false;
					}else{										
						species[newX][newY] = animal;
						animal.calcRotation(newX, newY);
						animal.setXandYPos(newX, newY);
						tree.unlock(newX, newY);
						wolf.unlock(newX, newY);
						sheep.unlock(newX, newY);

						return true;			
					}
				}
			}
		}
			
		tree.unlock(newX, newY);
		wolf.unlock(newX, newY);
		sheep.unlock(newX, newY);
		return false;
			
	}
	

	/** Sets animal at coordinates (x, y) if the cell is unlocked and is not occupied, else it does nothing.
	 * 
	 * @param x The x coordinate which the animal want to be spawned.
	 * @param y The y coordinate which the animal want to be spawned.
	 * @param animal The animal which will be spawned.
	 * @return True if successfully spawned, else false.
	 */
	
	public boolean setSpeciesAt(int x, int y, Animal animal) {
		if(compareAndSet(x,y)){
			if (species[x][y] == null) {
				species[x][y] = animal;
				unlock(x,y);
				return true;
			} 
			else {
				unlock(x,y);
				return false;
			}
		}
		return false;
	}
	
	/** Returns true if the cell at coordinates (x, y) is unlocked and contains an animal.
	 * 
	 * @param x The coordinate to be checked.
	 * @param y The coordinate to be checked.
	 * @return True if the cell at coordinates (x, y) is unlocked and contains an animal.
	 */

	public boolean containsAnimal(int x, int y){
		if(compareAndSet(x,y)){
			if(species[x][y] != null){
				unlock(x,y);
				return true;
			} else {
				unlock(x,y);
				return false;
			}
		}
		return false;
	}

	@Override
	public float getNeed(Needs need, int x, int y) {
		if(containsAnimal(x, y)){
			return species[x][y].getNeed(need);
		} 
		return 0.0f;
	}
	
	@Override
	public float peekNeed(Needs need, int x, int y){
		if(species[x][y] != null){
			return species[x][y].peekNeed(need);
		}
		return 0.0f;
	}
	
	public String getSpecName(){
		return specName;
	}
	
	/**
	 * Locks the position (x,y) in the lockArray. 
	 * 
	 * @param x Coordinate to lock.
	 * @param y Coordinate to lock.
	 * @return True if the position was unlocked, else false.
	 */
	
	public boolean compareAndSet(int x, int y){
		return lockArray[x][y].compareAndSet(1, 0);
	}
	
	/**
	 * Unlocks the position (x,y) in the lockArray.
	 * 
	 * @param x Coordinate to unlock.
	 * @param y Coordinate to unlock.
	 */
	
	public void unlock(int x, int y){
		if(lockArray[x][y].get() == 0){
			lockArray[x][y].incrementAndGet();
		}
	}

}

