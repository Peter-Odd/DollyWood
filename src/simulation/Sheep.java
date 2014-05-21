package simulation;

import java.util.ArrayList;



import java.util.concurrent.Callable;

import utilities.Globals;
import utilities.HexagonUtils;
import utilities.NeedsController;
import utilities.NeedsController.NeedsControlled;
import utilities.Needs;


public class Sheep extends Animal implements  Runnable{

	private float timeUntilBirth;
	private boolean alive = true;
	private Race race;

	public Sheep(int xPos, int yPos, Race sheep){
		super();
		super.xPos = xPos;
		super.yPos = yPos;
		super.race = sheep;
		super.hunger = 0.5f;
		super.thirst = 0.5f;
		this.timeUntilBirth = 1.0f;
		this.race = sheep;
		race.numberOfInstances.incrementAndGet();
		
		Globals.registerSetting("Herd priority", "Sheep", 0, 2, 0.4f);
		Globals.registerSetting("Hunger priority", "Sheep", 0, 2, 1);
		Globals.registerSetting("Thirst priority", "Sheep", 0, 2, 1);
		
		Globals.registerSetting("Sheep sleep", "Sheep", 0, 1500, 500);
		Globals.registerSetting("Sheep thirst", "Sheep", 0.1f, 1, 0.5f);
		Globals.registerSetting("Sheep hunger", "Sheep", 0.1f, 1, 0.5f);
	}

	
	/**
	 * Makes it available for the sheep to eat, drink, propagate, age, die, get pregnant, get hungry, get thirsty.
	 */

	public void run(){
		super.hunger = Globals.getSetting("Hunger", "Sheep");
		super.thirst = Globals.getSetting("Thirst", "Sheep");

		Globals.registerGraph("Number of sheep", "Sheep", new Callable<Float>() {
			public Float call() throws Exception {
				return getNumberOfSheep();
			}
		}, 500);
		
		// Just to let the world load.
		try {
			Thread.sleep(5000);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		while(alive){
			try {
				Thread.sleep((int)Globals.getSetting("Sheep sleep", "Sheep"));
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			
			//locks sheep
			try {
				super.busy.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			/*
			 * Makes the sheep hungry, thirst and makes it grow older. The sheep is getting more hungry if she is
			 * pregnant and then also decreases the timeUntilBirth.
			 */
			
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
			
			/*
			 * Checks if the Sheep needs to give birth, drink, eat, propagate or are ready to breed. This
			 * is done in a order of importance.
			 */

			if(timeUntilBirth <= 0.0f){
				giveBirth();
			}else if(hunger > 0.8f){
				eat();
			}else if(thirst > 0.3f){
				drink();
			}else if(hunger < 0.6f && thirst < 0.4f && !this.getGender() && age > 0.4f){
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
			
			/*
			 * Checks if the sheep will die because of thirst, hunger or age. 
			 */
			
			if(hunger > 1.0f){
				System.out.println("Hunger");
				race.numberOfInstances.decrementAndGet();
				this.alive = false;
				race.getAndRemoveSpeciesAt(xPos, yPos);
			}else if (thirst > 1.0f){
				System.out.println("Thirst");
				race.numberOfInstances.decrementAndGet();
				this.alive = false;
				race.getAndRemoveSpeciesAt(xPos, yPos);
			}else if(age > 5.0f){
				System.out.println("Age");
				race.numberOfInstances.decrementAndGet();
				this.alive = false;
				race.getAndRemoveSpeciesAt(xPos, yPos);
			}

			// Makes the sheep to move to a new position.
			move();
			//unlocks sheep
			super.busy.release();

		}
	}
	
	/**
	 * Moves the sheep to a position within a radius of 6 form the current xPos and yPos. What position which is selected
	 * is dependent on what the sheep currently needs.
	 */

	private void move(){
		ArrayList<Needs> needList = new ArrayList<>();
		needList.add(new Needs("Water", thirst*Globals.getSetting("Thirst priority", "Sheep")));
		needList.add(new Needs("Meat", Globals.getSetting("Herd priority", "Sheep")));
		needList.add(new Needs("Plant", hunger*Globals.getSetting("Hunger priority", "Sheep")));
		needList.add(new Needs("Predator", -20.0f));
		int[] requestedPosition = super.calculatePositionValue(needList, super.xPos, super.yPos);
		super.moveTo(requestedPosition[0], requestedPosition[1], 0);
	}
	
	/** 
	 * Spawns a new sheep at an empty position around the current xPos and yPos, if no empty tile is available
	 * nothing happens. Also sets pregnant to false and time until birth to 1.0f;
	 */
	
	private void giveBirth(){
		ArrayList<int[]> neighbors = HexagonUtils.neighborTiles(xPos, yPos, false);

		for(int[] neighbor : neighbors){
			if(!race.containsAnimal(neighbor[0], neighbor[1])){
				Sheep lamb = new Sheep(neighbor[0], neighbor[1], race);
				race.setSpeciesAt(neighbor[0], neighbor[1], lamb);
				Thread sheepThread = new Thread(lamb);
				sheepThread.start();
				timeUntilBirth = 1.0f;
				pregnant = false;
				break;
			}
		}
	}

	/**
	 * Looks for the need "Plant" at the current xPos and yPos, if any is found it's added to hunger and the 
	 * thread is put to sleep for 2000ms.
	 */
	
	public void eat(){
		float food = 0.0f;

		for(NeedsControlled nc : NeedsController.getNeed("Plant")){
			food += nc.getNeed(new Needs("Plant", 0.6f), xPos, yPos);
		}

		if(food > 0.3f){
			hunger -= food;
			try {
				Thread.sleep(2000);
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}	
		}
	}
	
	/**
	 * Returns the age if age is less or equal to 1.2, else it returns 1.2.
	 */

	public float getSize(){
		if(age >1.2f)
			return 1.2f;
		else
			return age;
	}
	
	/**
	 * Stops the current thread and returns 1.0f.
	 */
	
	public float getNeed(Needs need){
		race.numberOfInstances.decrementAndGet();
		this.alive = false;
		race.getAndRemoveSpeciesAt(xPos, yPos);
		return 1.0f;
	}
	
	/**
	 * Returns what the current sheep have to offer as need.
	 */
	
	public float peekNeed(Needs need){
		return 1.0f;
	}
	
	/**
	 * @return The current number of sheep in the world.
	 */
	
	private float getNumberOfSheep() {
		return (float)race.numberOfInstances.get();
	}
}
