package simulation;

import java.util.concurrent.atomic.AtomicInteger;

import utilities.Globals;
import utilities.HexagonUtils;
import utilities.Needs;
import utilities.NeedsController;
import utilities.NeedsController.NeedsControlled;

public class Race implements NeedsControlled{
	private Animal[][] species;
	private AtomicInteger[][] lockArray;
	private String specName;

	public Race(String specName) {
		this.specName = specName;
		species = new Animal[Globals.height][Globals.width];
		lockArray = new AtomicInteger[Globals.height][Globals.width];
		
		//initates the lockArray.
		for(int x = 0; x<Globals.width;x++){
			for(int y = 0; y<Globals.height;y++){
				lockArray[x][y] = new AtomicInteger(1);
			}
		}

		if(specName == "Sheep"){
			NeedsController.registerNeed("Meat", this);
		}else if(specName == "Tree"){
			NeedsController.registerNeed("Water", this);
			NeedsController.registerNeed("Tree", this);
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
			lock(x, y);
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
		
		if(newX == currentX && newY == currentY){
			return true;
		}
		// Find the Tree race.
		for(Race race: Globals.races){
			if(race.getSpecName().equals("Tree")){
				tree = race;
			}
		}
		
		if(!tree.containsAnimal(newX, newY)){
			if(compareAndSet(newX, newY)){
				if(species[newX][newY] == null){
					Animal animal = getAndRemoveSpeciesAt(currentX, currentY);
					if(animal == null){
						return false;
					}else{
						species[newX][newY] = animal;
						animal.calcRotation(newX, newY);
						animal.setXandYPos(newX, newY);
						lockArray[newX][newY].incrementAndGet();
						return true;
					}
				} else {
					lock(newX, newY);
					return false;
				}
			}
		}
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
				lock(x,y);
				return true;
			} 
			else {
				lock(x,y);
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
				lock(x,y);
				return true;
			} else {
				lock(x,y);
				return false;
			}
		}
		return false;
	}


	@Override
	public float getNeed(Needs need, int x, int y) {
		if(need.getNeed().equals("Meat") && containsAnimal(x, y)){
			getAndRemoveSpeciesAt(x, y);
			return 1.0f;
		} else if (need.getNeed().equals("Water")) {
			for (int[] neighbor : HexagonUtils.neighborTiles(x, y, true)) {
				if (containsAnimal(neighbor[0], neighbor[1])) {
					return -0.2f;
				}
			}
			return 0.0f;
		} else if (need.getNeed().equals("Tree")) {
			for (int[] neighbor : HexagonUtils.neighborTiles(x, y, 2, true)) {
				if (containsAnimal(neighbor[0], neighbor[1])) {
					return 1.0f;
				}
			}
			return 0.0f;
		} 
		return 0.0f;
	}
	
	public String getSpecName(){
		return specName;
	}
	
	public boolean compareAndSet(int x, int y){
		return lockArray[x][y].compareAndSet(1, 0);
	}
	
	public void lock(int x, int y){
		lockArray[x][y].incrementAndGet();
	}

}

