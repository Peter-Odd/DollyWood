package simulation;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;

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
	private boolean gender; // true = female, false = male
	protected float age; // 1.0f = adult;
	protected float size;
	protected boolean pregnant;
	protected float hunger;
	protected float thirst;
	protected Race race;
	protected boolean readyToBreed;


		
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
	
	/** Returns true if a sheep is ready to breed, else false.
	 * 
	 * @return True if the sheep is ready to breed, else false.
	 */
	
	/**protected boolean readyToBreed(){
		if(age > 0.3f && thirst > 0.5f && hunger > 0.5f && !getPregnant()){
			System.out.println("Ready to breed.");
			return true;
		}else{
			return false;
		}
	}
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
	
	protected void moveRandom(){
		ArrayList<int[]> neighbor = HexagonUtils.neighborTiles(xPos, yPos, false);
		Random myRandomizer = new Random();
		int[] randomNeighbor = neighbor.get(myRandomizer.nextInt(neighbor.size()));
		race.moveSpecies(xPos, yPos, randomNeighbor[0], randomNeighbor[1]);
	}
	
	protected void propagate(){
		ArrayList<int[]> neighbor = HexagonUtils.neighborTiles(xPos, yPos, false);
		Random myRandomizer = new Random();
		int[] randomNeighbor = neighbor.get(myRandomizer.nextInt(neighbor.size()));
		
		Animal animal = findReadyToBreedAnimal();
				if(animal != null){
						boolean goalReached = animal.moveTo(randomNeighbor[0], randomNeighbor[1]); // waiting for the female
						if(goalReached == true){
							animal.setPregnant(true);
						}
				}
	}
	
	/** The animal moves to position x, y. And when destination is reached, the current animal notifies animal.
	 * 
	 * @param x coordinate to move to.
	 * @param y coordinate to move to.
	 */
	
	protected boolean moveTo(int x, int y){
		Deque<int[]> path = Astar.calculatePath(xPos, yPos, x, y);
		for(int [] nextCord : path){
			try {
			    Thread.sleep(500);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			
			boolean moved = race.moveSpecies(xPos, yPos, nextCord[0], nextCord[1]);
			if(!moved){
				this.moveTo(x, y);
			}
		}
		return true;
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
	
	public void setPregnant(boolean bool){
		hunger = 0.3f;
		thirst = 0.3f;
		this.pregnant = bool;
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
}
