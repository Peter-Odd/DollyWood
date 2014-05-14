package simulation;

import java.util.ArrayList;


import utilities.HexagonUtils;
import utilities.NeedsController;
import utilities.NeedsController.NeedsControlled;
import utilities.Needs;


public class Sheep extends Animal implements  Runnable{
	

	
	private float timeUntilBirth;
	
	
	public Sheep(int xPos, int yPos, Race sheep){
		super();
		super.xPos = xPos;
		super.yPos = yPos;
		super.sheepRace = sheep;
		super.hunger = 0.5f;
		super.thirst = 0.5f;
		this.timeUntilBirth = 1.0f;
	}
	
	public Sheep(int xPos, int yPos, Race sheep, boolean gender){
		super(gender);
		super.xPos = xPos;
		super.yPos = yPos;
		super.sheepRace = sheep;
		super.hunger = 0.5f;
		super.thirst = 0.5f;
		this.timeUntilBirth = 1.0f;
	}	
	
	
	public void run(){
		while(true){
			try {
			    Thread.sleep(500);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			
			if(pregnant){
				age += 0.02;
				hunger -= 0.05f;
				thirst -= 0.05f;
				timeUntilBirth -= 0.02;
				System.out.println("Pregnant");
			} else {
				age += 0.02;
				hunger -= 0.02f;
				thirst -= 0.02f;
			}
			if(timeUntilBirth <= 0.0f){
				giveBirth();
			}else if(thirst < 0.4f){
				drink();
			}else if(hunger < 0.4f){
				eat();
			}else if(hunger > 0.5f && thirst > 0.5f && !this.getGender()){
				propagate();
				hunger = 0.3f;
				thirst = 0.3f;
			}			
			if(hunger < 0.0f){
				sheepRace.getAndRemoveSpeciesAt(xPos, yPos);
			}
			if(thirst < 0.0f){
				sheepRace.getAndRemoveSpeciesAt(xPos, yPos);
			}
			
			moveRandom();
			
			
		}
	}
	
	private void giveBirth(){
		ArrayList<int[]> neighbors = HexagonUtils.neighborTiles(xPos, yPos, false);
		System.out.println("Birth");
		for(int[] neighbor : neighbors){
			if(!sheepRace.containsAnimal(neighbor[0], neighbor[1])){
				System.out.println("Really birth");
				Sheep lamb = new Sheep(neighbor[0], neighbor[1], sheepRace);
				sheepRace.setSpeciesAt(neighbor[0], neighbor[1], lamb);
				Thread sheepThread = new Thread(lamb);
				sheepThread.start();
				timeUntilBirth = 1.0f;
				pregnant = false;
				break;
			}
		}
	}
	
	private void eat(){
		
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
}
