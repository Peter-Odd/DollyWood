package simulation;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import utilities.Globals;
import utilities.HexagonUtils;
import utilities.NeedsController;
import utilities.NeedsController.NeedsControlled;
import utilities.Needs;

public class Wolf extends Animal implements Runnable{

	private float timeUntilBirth;
	private boolean alive = true;
	private Race race;

	public Wolf(int xPos, int yPos, Race wolf){
		super();
		super.xPos = xPos;
		super.yPos = yPos;
		super.race = wolf;
		super.hunger = 0.5f;
		super.thirst = 0.5f;
		this.timeUntilBirth = 0.5f;

		this.race = wolf;
		race.numberOfInstances.incrementAndGet();
	}

	public Wolf(int xPos, int yPos, Race wolf, boolean gender){
		super(gender);
		super.xPos = xPos;
		super.yPos = yPos;
		super.race = wolf;
		this.timeUntilBirth = 0.5f;

		this.race = wolf;
		race.numberOfInstances.incrementAndGet();
	}

	private float getNumberOfWolves() {
		return (float)race.numberOfInstances.get();
	}

	public void run(){
		super.hunger = Globals.getSetting("Wolves hunger", "Wolf");
		super.thirst = Globals.getSetting("Wolves thirst", "Wolf");

		Globals.registerGraph("Number of wolves", "Wolves", new Callable<Float>() {
			public Float call() throws Exception {
				return getNumberOfWolves();
			}
		}, 500);

		while(alive){
			try {
				Thread.sleep((int)Globals.getSetting("Wolves sleep", "Wolf"));
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

			//Locks wolf
			try {
				super.busy.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if(pregnant){
				age += 0.02;
				hunger += 0.03f;
				thirst += 0.03f;
				timeUntilBirth -= 0.02;
			} else {
				age += 0.02;
				hunger += 0.02f;
				thirst += 0.02f;
			}
			if(timeUntilBirth <= 0.0f){
				giveBirth();
			}else if(thirst > 0.5f){
				drink();
			}else if(hunger > 0.5f){
				eat();
			}else if(hunger < 0.5f && thirst < 0.5f && !this.getGender() && age > 0.4f){
				propagate();
				hunger = 0.7f;
				thirst = 0.7f;
			}else if(this.getGender()){
				if(age > 0.4f && !pregnant){
					setReadyToBreed(true);
				}else{
					setReadyToBreed(false);
				}
			}			
			if(hunger > 1.0f){
				race.numberOfInstances.decrementAndGet();
				this.alive = false;
				race.getAndRemoveSpeciesAt(xPos, yPos);
			}
			if(thirst > 1.0f){
				race.numberOfInstances.decrementAndGet();
				this.alive = false;
				race.getAndRemoveSpeciesAt(xPos, yPos);
			}else if(age > 2.0f){
				race.numberOfInstances.decrementAndGet();
				this.alive = false;
				race.getAndRemoveSpeciesAt(xPos, yPos);
			}
			//moveRandom();
			move();

			//Unlocks wolf
			super.busy.release();
		}
	}

	private void move(){
		ArrayList<Needs> needList = new ArrayList<>();
		needList.add(new Needs("Water", this.thirst));
		needList.add(new Needs("Plant", 0.5f));
		int[] requestedPosition = super.calculatePositionValue(needList, super.xPos, super.yPos);
		super.moveTo(requestedPosition[0], requestedPosition[1], 0);
	}

	private void giveBirth(){
		ArrayList<int[]> neighbors = HexagonUtils.neighborTiles(xPos, yPos, false);

		for(int[] neighbor : neighbors){
			if(!race.containsAnimal(neighbor[0], neighbor[1])){
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

		Animal sheep = findSheep();

		while(sheep != null && food == 0.0f){
			this.moveTo(sheep.xPos, sheep.yPos, 0);

			for (NeedsControlled nc : NeedsController.getNeed("Meat")){
				food += nc.getNeed(new Needs("Meat", 0.6f), xPos, yPos);
			}	
			hunger -= food;
		}

		if(hunger < 0.5f){
			try {
				Thread.sleep(2000);
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}	
		}
	}

	private Animal findSheep() {
		ArrayList<int[]> neighbor = HexagonUtils.neighborTiles(xPos, yPos, 5, false);
		Race sheep = null;
		Animal tempSheep = null;

		for(Race race: Globals.races){
			if  (race.getSpecName().equals("Sheep")) {
				sheep = race;
			}
		}

		for (int[] food : neighbor) {
			if(sheep.containsAnimal(food[0], food[1])){
				Animal targetSheep = sheep.getSpeciesAt(food[0], food[1]);
				if(targetSheep.hunter == null){
					targetSheep.setHunter(this);
					return targetSheep;
				}
			}
		}
		return tempSheep;
	}

}
