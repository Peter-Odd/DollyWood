package simulation;

import java.util.ArrayList;
import java.util.Random;

import utilities.HexagonUtils;
import utilities.NeedsController;
import utilities.NeedsController.NeedsControlled;
import utilities.Needs;


public class Sheep extends Animal implements  Runnable{
	
	int xPos;
	int yPos;
	Race sheep;
	float hunger;
	float thirst;
	boolean gender; // true = female, false = male.
	float timeUntilBirth;
	
	
	public Sheep(int xPos, int yPos, Race sheep){
		super();
		this.xPos = xPos;
		this.yPos = yPos;
		this.sheep = sheep;
		this.hunger = 0.5f;
		this.thirst = 0.5f;
		this.timeUntilBirth = 1.0f;
	}
	
	public Sheep(int xPos, int yPos, Race sheep, boolean gender){
		super(gender);
		this.xPos = xPos;
		this.yPos = yPos;
		this.sheep = sheep;
		this.hunger = 0.5f;
		this.thirst = 0.5f;
		this.timeUntilBirth = 1.0f;
	}	
	
	
	public void run(){
		while(true){
			try {
			    Thread.sleep(1000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			
			if(pregnant){
				
				hunger -= 0.05f;
				thirst -= 0.05f;
				timeUntilBirth -= 0.2;
			} else {
				hunger -= 0.02f;
				thirst -= 0.02f;
			}
			if(timeUntilBirth <= 0.0f){
				System.out.println("Pregnatn");
				giveBirth();
			}else if(thirst < 0.4f){
				drink();
			}else if(hunger < 0.4f){
				eat();
			}else if(hunger > 0.2f && thirst > 0.2f && !this.getGender()){
				propagate();
				hunger = 0.1f;
				thirst = 0.1f;
			}			
			if(hunger < 0.0f){
				sheep.getAndRemoveSpeciesAt(xPos, yPos);
			}
			if(thirst < 0.0f){
				sheep.getAndRemoveSpeciesAt(xPos, yPos);
			}
			
			moveRandom();
			
			
		}
	}
	
	public void giveBirth(){
		ArrayList<int[]> neighbors = HexagonUtils.neighborTiles(xPos, yPos, false);
		System.out.println("Birth");
		for(int[] neighbor : neighbors){
			if(sheep.containsAnimal(neighbor[0], neighbor[1])){
				Sheep lamb = new Sheep(xPos+1, yPos, sheep);
				sheep.setSpeciesAt(xPos+1, yPos, lamb);
				Thread sheepThread = new Thread(lamb);
				sheepThread.start();
				timeUntilBirth = 1.0f;
				pregnant = false;
				break;
			}
		}
	}
	
	public void drink(){
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
	
	public void moveRandom(){
		ArrayList<int[]> neighbor = HexagonUtils.neighborTiles(xPos, yPos, false);
		Random myRandomizer = new Random();
		int[] randomNeighbor = neighbor.get(myRandomizer.nextInt(neighbor.size()));
		
		sheep.moveSpecies(xPos, yPos, randomNeighbor[0], randomNeighbor[1]);
		calcRotation(randomNeighbor);
	}
	
	public void propagate(){
	
		ArrayList<int[]> neighbors = HexagonUtils.neighborTiles(xPos, yPos, 10, false);
		float propagate = 0.0f;
		
		for(int[] neighbor : neighbors){
			for(NeedsControlled nc : NeedsController.getNeed("Sheep")){
				propagate += nc.getNeed(new Needs("Sheep", 0.6f), neighbor[0], neighbor[1]);
			}
			if(propagate > 0.0f){
				break;
			}
		}
	}
	
	public void eat(){
		
		float food = 0.0f;
		
		for(NeedsControlled nc : NeedsController.getNeed("Plant")){
			   food += nc.getNeed(new Needs("Plant", 0.6f), xPos, yPos);
		}
		
		hunger += food;
		
		if(hunger > 0.4f){
			try {
			    Thread.sleep(2000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}	
		}
	}
	
	/** Calculates the correct rotation for a sheep when moved.
	 * 
	 * @param newPos The position which the sheep will be moved to.
	 */
	
	private void calcRotation(int[] newPos){
		
		
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
}
