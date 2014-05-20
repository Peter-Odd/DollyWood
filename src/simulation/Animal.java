package simulation;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;
import java.util.concurrent.Semaphore;

import utilities.Astar;
import utilities.HexagonUtils;
import utilities.Needs;
import utilities.NeedsController;
import utilities.NeedsController.NeedsControlled;

public class Animal{
	protected int xPos;
	protected int yPos;
	private Random random;
	private float rotation;
	protected boolean gender; // true = female, false = male
	protected float age; // 1.0f = adult;
	protected float size;
	protected boolean pregnant;
	protected float hunger;
	protected float thirst;
	protected Race race;
	protected boolean readyToBreed;
	protected Semaphore busy = new Semaphore(1);
	protected Animal hunter = null;


		
	public Animal(){
		random = new Random();
		rotation = random.nextInt(360);
		age = 0.3f;
		size = 0.3f;
		this.pregnant = false;
		if(random.nextInt(100) >= 50){
			this.gender = true;
			this.readyToBreed = true;
			size = 0.7f;
		} else {
			this.gender = false;
			this.readyToBreed = false;
			size = 1.5f;
		}
	}
	
	public Animal(boolean gender){
		random = new Random();
		rotation = random.nextInt(360);
		age = 0.4f;
		this.pregnant = false;
		this.gender = gender;
		if(gender){
			size = 0.7f;
			readyToBreed = true;
		}else{
			size = 1.5f;
			readyToBreed = false;
		}
	}
	
	/** Returns an animal which is ready to breed within the radius of 7 from position (xPos, yPos). Else 
	 * it returns null. 
	 * 
	 * @return An animal which is ready to breed.
	 */
	
	public Animal findReadyToBreedAnimal(){
		ArrayList<int[]> neighbor = HexagonUtils.neighborTiles(xPos, yPos, 7, false);
		Animal animal;
		for(int[] sheeps : neighbor){
			animal = race.getSpeciesAt(sheeps[0], sheeps[1]);

			if(animal != null && animal.getReadyToBreed()){
				animal.setReadyToBreed(false);
				return animal;
			}
		}
		return null;
		
	}
	
	/** Locates and collects water which is close by, if any is found it sleeps for 2000ms and
	 *  adds the amount found to this.thirst.
	 */
	
	protected void drink(){
		float water = 0.0f;
		
		for(NeedsControlled nc : NeedsController.getNeed("Water")){
			   water += nc.getNeed(new Needs("Water", 0.6f), xPos, yPos);
		}
		thirst += water;
		
		if(thirst > 0.4f){
			try {
				Thread.sleep(2000);
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}	
		}
	}
	
	/** Moves to a random neighbor tile if possible.
	 */
	
	protected void moveRandom(){
		ArrayList<int[]> neighbor = HexagonUtils.neighborTiles(xPos, yPos, false);
		int[] randomNeighbor = neighbor.get(random.nextInt(neighbor.size()));
		moveTo(randomNeighbor[0], randomNeighbor[1], 0);
	}
	
	/** Finds a female sheep which is ready to breed, and tells her to walk to a random tile
	 *  next to this sheep.
	 */
	
	protected void propagate(){
	
		ArrayList<int[]> neighbor = HexagonUtils.neighborTiles(xPos, yPos, false);
		int[] randomNeighbor = neighbor.get(random.nextInt(neighbor.size()));
		
		Animal animal = findReadyToBreedAnimal();
				if(animal != null){
					animal.lock();
					boolean goalReached = animal.moveTo(randomNeighbor[0], randomNeighbor[1], 0); // waiting for the female
					if(goalReached == true){
						animal.setPregnant(true);
					}
					animal.unlock();
				}
	}
	
	/** The animal moves to position x, y. And when destination is reached, return true.
	 * 
	 * @param x coordinate to move to.
	 * @param y coordinate to move to.
	 * @return true is destination is reached, else false.
	 */
	
	protected boolean moveTo(int x, int y, int blocked){
		Deque<int[]> path = Astar.calculatePath(xPos, yPos, x, y);
		path.removeFirst(); // removes the current location
		for(int [] nextCord : path){
			try {
			    Thread.sleep(500);
			} catch(InterruptedException ex) {
				System.out.println("Thread interrupt");
			    Thread.currentThread().interrupt();
			}
			
			boolean moved = race.moveSpecies(xPos, yPos, nextCord[0], nextCord[1]);
			if(!moved && blocked < 4){
				blocked++;
				this.moveTo(x, y, blocked);
			}else if(!moved && blocked > 3){
				return false;
			}else{
				return true;
			}
		}
		return true;
	}
	
	public int[] calcPositionToMove(ArrayList<String> needs){
		
		
		return null;
	}
	
	/** Calculates the correct rotation for a sheep when moved.
	 * 
	 * @param newPos The position which the sheep will be moved to.
	 */
	
	public void calcRotation(int x, int y){
		

		if(xPos == x){			// middle
			if(yPos < y){		// top
				setRotation(180);
			}else{
				setRotation(0); 		// bottom
			}
		}else if(xPos < x){  	//left
			if(yPos == y){		
				if((xPos%2) == 0){		//bottom left
					setRotation(120);
				} else{					// top left
					setRotation(60);
				}
			}else{
				if((xPos%2) == 0){		//bottom left
					setRotation(60);
				} else{					// top left
					setRotation(120);
				}
			}
		}else{							// right
			if(yPos == y){		
				if((xPos%2) == 0){		
					setRotation(240);
				} else{					
					setRotation(300);
				}
			}else{
				if((xPos%2) == 0){		
					setRotation(300);
				} else{					
					setRotation(240);
				}
			}
		}
	}
	

	public float peekNeed(Needs need){
		return 0.0f;
	}
	
	public float getNeed(Needs need){
		return 0.0f;
	}
	
	public boolean getGender(){
		return gender;
	}
	
	public float getAge(){
		return age;
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public float getSize(){
		return 1.0f;
	}
	
	/** Sets pregnant to bool. If bool == true and pregnant == false, then the hunger and thirst of this animal is set
	 *  to 0.3.
	 * 
	 * @param bool Pregnant will be set to this.
	 */
	
	public void setPregnant(boolean bool){
		if(!pregnant && bool){
			hunger = 0.3f;
			thirst = 0.3f;
			this.pregnant = bool;
		}else{
			this.pregnant = bool;
		}
	}
	
	public boolean getPregnant(){
		return pregnant;
	}
	
	public void setRotation(float rotation){
		this.rotation = rotation;
	}
	
	public void setHunger(float hunger){
		this.hunger = hunger;
	}
	
	public void setThirst(float thirst){
		this.thirst = thirst;
	}
	
	public boolean getReadyToBreed(){
		return readyToBreed;
	}
	
	public void setReadyToBreed(boolean breed){
		readyToBreed = breed;
	}
	
	public void setXandYPos(int x, int y){
		this.xPos = x;
		this.yPos = y;
	}
	
	public void lock(){
		try {
			busy.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void unlock(){
		busy.release();
	}
	
	public void setHunter(Animal animal){
		this.hunter = animal;
	}
}
