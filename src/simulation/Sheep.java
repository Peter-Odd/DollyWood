package simulation;

import java.util.ArrayList;
import java.util.Random;

import utilities.HexagonUtils;


public class Sheep extends Animal implements  Runnable{
	
	int xPos;
	int yPos;
	Race sheep;
	float hunger;
	
	public Sheep(int xPos, int yPos, Race sheep, float hunger){
		super();
		this.xPos = xPos;
		this.yPos = yPos;
		this.sheep = sheep;
		this.hunger = hunger;
	}	
	
	
	public void run(){
		while(true){
			try {
			    Thread.sleep(200);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		
			ArrayList<int[]> neighbor = HexagonUtils.neighborTiles(xPos, yPos, false);
			Random myRandomizer = new Random();
			int[] randomNeighbor = neighbor.get(myRandomizer.nextInt(neighbor.size()));
			
			
			
			
			
			sheep.moveSpecies(xPos, yPos, randomNeighbor[0], randomNeighbor[1]);
			calcRotation(randomNeighbor);
			xPos = randomNeighbor[0];
			yPos = randomNeighbor[1];
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
	}
}
