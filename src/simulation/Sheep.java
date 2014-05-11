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
	
	public Sheep(int xPos, int yPos, Race sheep){
		super();
		this.xPos = xPos;
		this.yPos = yPos;
		this.sheep = sheep;
		this.hunger = 0.5f;
		this.thirst = 0.5f;
	}	
	
	
	public void run(){
		while(true){
			try {
			    Thread.sleep(200);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			hunger -= 0.02f;
			thirst -= 0.02f;
		

			
		

			if(thirst < 0.4f){
				drink();
			}else if(hunger < 0.4f){
				eat();
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
