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
	}

	public Sheep(int xPos, int yPos, Race sheep, boolean gender){
		super(gender);
		super.xPos = xPos;
		super.yPos = yPos;
		super.race = sheep;
		this.timeUntilBirth = 1.0f;
		
		this.race = sheep;
		race.numberOfInstances.incrementAndGet();
	}	

	private float getNumberOfSheep() {
		return (float)race.numberOfInstances.get();
	}

	public void run(){
		super.hunger = Globals.getSetting("Sheep hunger", "Sheep");
		super.hunger = Globals.getSetting("Sheep thirst", "Sheep");

		Globals.registerGraph("Number of sheep", "Sheep", new Callable<Float>() {
			public Float call() throws Exception {
				return getNumberOfSheep();
			}
		}, 500);

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
			
			if(pregnant){
				age += 0.02;
				hunger -= 0.03f;
				thirst -= 0.03f;
				timeUntilBirth -= 0.02;
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
			}else if(hunger > 0.5f && thirst > 0.5f && !this.getGender() && age > 0.4f){
				propagate();
				hunger = 0.3f;
				thirst = 0.3f;
			}else if(this.getGender()){
				if(age > 0.4f && !pregnant){
					setReadyToBreed(true);
				}else{
					setReadyToBreed(false);
				}
			}
			if(hunger < 0.0f){
				race.numberOfInstances.decrementAndGet();
				this.alive = false;
				race.getAndRemoveSpeciesAt(xPos, yPos);
			}else if (thirst < 0.0f){
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
			//unlocks sheep
			super.busy.release();

		}
	}

	private void move(){
		ArrayList<Needs> needList = new ArrayList<>();
		needList.add(new Needs("Water", 0.0f));
		needList.add(new Needs("Meat", 1.0f));
		int[] requestedPosition = super.calculatePositionValue(needList, super.xPos, super.yPos);
		super.moveTo(requestedPosition[0], requestedPosition[1], 0);
	}
	
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

	public float getSize(){
		if(age >1.2f)
			return 1.2f;
		else
			return age;
	}
	
	
	public float getNeed(Needs need){
		race.numberOfInstances.decrementAndGet();
		this.alive = false;
		race.getAndRemoveSpeciesAt(xPos, yPos);
		return 1.0f;
	}
	
	public float peekNeed(Needs need){
		return 1.0f;
	}
}
