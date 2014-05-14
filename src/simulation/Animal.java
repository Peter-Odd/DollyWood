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
	public float age; // 1.0f = adult;
	boolean pregnant;
	protected float hunger;
	protected float thirst;
	protected Race sheepRace;

		
	public Animal(){
		random = new Random();
		rotation = random.nextInt(360);
		age = 0.3f;
		this.pregnant = false;
		if(random.nextInt(100) >= 50){
			this.gender = true;
		} else {
			this.gender = false;
		}
	}
	
	public Animal(boolean gender){
		random = new Random();
		rotation = random.nextInt(360);
		age = 0.3f;
		this.pregnant = false;
		this.gender = gender;
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
			animal = sheepRace.getSpeciesAt(sheeps[0], sheeps[1]);
			if(animal != null && animal.readyToBreed()){
				return animal;
			}
		}
		return null;
		
	}
	
	/** Returns true if a sheep is ready to breed, else false.
	 * 
	 * @return True if the sheep is ready to breed, else false.
	 */
	
	protected boolean readyToBreed(){
		if(age > 0.3f && thirst > 0.5f && hunger > 0.5f && !getPregnant()){
			System.out.println("Ready to breed.");
			return true;
		}else{
			return false;
		}
	}
	

	
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
		
		sheepRace.moveSpecies(xPos, yPos, randomNeighbor[0], randomNeighbor[1]);
		calcRotation(randomNeighbor);
	}
	
	protected void propagate(){
		
		Animal animal = findReadyToBreedAnimal();
			System.out.println("Propagate");
				if(animal != null){
						animal.moveTo(xPos+1, yPos);
						System.out.println("Busy wainting");
						while(!sheepRace.containsAnimal(xPos+1, yPos)){
							//Busy waiting
						}
						System.out.println("Past busy waiting.");
						Animal sheep = sheepRace.getSpeciesAt(xPos+1, yPos);
						sheep.setPregnant(true);
					}
	}
	
	protected void moveTo(int x, int y){
		Deque<int[]> path = Astar.calculatePath(xPos, yPos, x, y);
		System.out.println("Move");
		for(int [] nextCord : path){
			try {
			    Thread.sleep(500);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			sheepRace.moveSpecies(xPos, yPos, nextCord[0], nextCord[1]);
			xPos = nextCord[0];
			yPos = nextCord[1];
		}
		try {
		    Thread.sleep(2000);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
	
	/** Calculates the correct rotation for a sheep when moved.
	 * 
	 * @param newPos The position which the sheep will be moved to.
	 */
	
	protected void calcRotation(int[] newPos){
		
		
		if(xPos == newPos[0]){			// middle
			if(yPos < newPos[1]){		// top
				setRotation(180);
			}else{
				setRotation(0); 		// bottom
			}
		}else if(xPos < newPos[0]){  	//left
			if(yPos == newPos[1]){		
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
			if(yPos == newPos[1]){		
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
		xPos = newPos[0];
		yPos = newPos[1];
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
	
}
