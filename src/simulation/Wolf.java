package simulation;

import java.util.ArrayList;

import utilities.Globals;
import utilities.HexagonUtils;
import utilities.NeedsController;
import utilities.NeedsController.NeedsControlled;
import utilities.Needs;

public class Wolf extends Animal implements Runnable{

	private float timeUntilBirth;

	public Wolf(int xPos, int yPos, Race wolf){
		super();
		super.xPos = xPos;
		super.yPos = yPos;
		super.race = wolf;
		this.timeUntilBirth = 0.5f;
	}

	public Wolf(int xPos, int yPos, Race wolf, boolean gender){
		super(gender);
		super.xPos = xPos;
		super.yPos = yPos;
		super.race = wolf;
		this.timeUntilBirth = 0.5f;
	}

	public void run(){
		super.hunger = Globals.getSetting("Wolves hunger", "Wolf");
		super.thirst = Globals.getSetting("Wolves thirst", "Wolf");
		
		while(true){
			try {
				Thread.sleep((int)Globals.getSetting("Wolves sleep", "Wolf"));
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

			if(pregnant){
				age += 0.02;
				hunger -= 0.05f;
				thirst -= 0.05f;
				timeUntilBirth -= 0.02;
				System.out.println("Wolf: Pregnant");
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
				race.getAndRemoveSpeciesAt(xPos, yPos);
			}
			if(thirst < 0.0f){
				race.getAndRemoveSpeciesAt(xPos, yPos);
			}

			moveRandom();
		}
	}

	private void giveBirth(){
		ArrayList<int[]> neighbors = HexagonUtils.neighborTiles(xPos, yPos, false);
		System.out.println("Wolf: Birth");
		for(int[] neighbor : neighbors){
			if(!race.containsAnimal(neighbor[0], neighbor[1])){
				System.out.println("Wolf: Really birth");
				Wolf puppy = new Wolf(neighbor[0], neighbor[1], race);
				race.setSpeciesAt(neighbor[0], neighbor[1], puppy);
				Thread wolfThread = new Thread(puppy);
				wolfThread.start();
				timeUntilBirth = 1.0f;
				pregnant = false;
				break;
			}
		}
	}


	public void eat(){
		float food = 0.0f;

		for(NeedsControlled nc : NeedsController.getNeed("Meat")){
			food += nc.getNeed(new Needs("Meat", 0.6f), xPos, yPos);
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