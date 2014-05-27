package simulation;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;
import java.util.concurrent.Semaphore;

import utilities.Astar;
import utilities.Globals;
import utilities.HexagonUtils;
import utilities.Needs;
import utilities.NeedsController;
import utilities.NeedsController.NeedsControlled;

public class Animal{
	protected int xPos;
	protected int yPos;
	protected Random random;
	private float rotation;
	protected boolean gender; // true = female, false = male
	protected float age; // 1.2f = adult;
	protected float size;
	protected boolean pregnant;
	protected float hunger;
	protected float thirst;
	protected Race race;
	protected boolean readyToBreed;
	protected Semaphore busy = new Semaphore(1);
	protected Animal predator = null;
	protected boolean alive = true;


		
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
	
	/** 
	 * Returns an animal which is ready to breed within the radius of 7 from position (xPos, yPos). Else 
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
	
	/**
	 * Calculates the value of a tile depending on how much resources its neighbors within a radius
	 * of 2 contains.  
	 * 
	 * @param needList The needs which will be considered.
	 * @param x The coordinate for the position.
	 * @param y The coordinate for the position.
	 * @return The value of the tile at position (x,y).
	 */
	
	private float calculateLocalPositionValue(ArrayList<Needs> needList, int x, int y){
		float value = 0.0f;
		for(int[] neighbor : HexagonUtils.neighborTiles(x, y, 2, true)){
			for(Needs n : needList){
				float need = 0.0f;
				for(NeedsControlled nc : NeedsController.getNeed(n.getNeed())){
					need += nc.peekNeed(new Needs(n.getNeed(), 1.0f), neighbor[0], neighbor[1]);
				}
				value += need*n.getAmmount();
			}
		}
		return value;
	}
	
	/**
	 * Returns a position which the current animal will walk too, depending on its needs.
	 * 
	 * @param needList The needs which will be considered.
	 * @return The new position.
	 */
	
	public int[] calculatePositionValue(ArrayList<Needs> needList){
		ArrayList<int[]> neighbor = HexagonUtils.neighborTiles(xPos, yPos, 6, false);
		float[][] randomTiles = new float[12][2];
		for(int i = 0; i < randomTiles.length; i++){
			randomTiles[i][0] = random.nextInt(neighbor.size());
			int[] tile = neighbor.get((int)(randomTiles[i][0]));
			randomTiles[i][1] = calculateLocalPositionValue(needList, tile[0], tile[1]);
		}
		float max = 0.0f;
		int index = 0;
		for(float[] positionValue : randomTiles){
			if(positionValue[1] > max){
				max = positionValue[1];
				index = (int)(positionValue[0]);
			}
		}
		return neighbor.get((int)(index));
	}
	
	/** 
	 * Locates and collects water which is close by, if any is found it sleeps for 2000ms and
	 * adds the amount found to this.thirst.
	 */
	
	protected void drink(){
		float water = 0.0f;
		
		for(NeedsControlled nc : NeedsController.getNeed("Water")){
			   water += nc.getNeed(new Needs("Water", 0.8f), xPos, yPos);
		}
		
		if(water >= 0.4f){
			
			this.thirst -= water;
			
			race.allowedWorker.release();
			try {
				Thread.sleep(2000);
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			try {
				race.allowedWorker.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** 
	 * Moves to a random neighbor tile if possible.
	 */
	
	protected void moveRandom(){
		ArrayList<int[]> neighbor = HexagonUtils.neighborTiles(xPos, yPos, false);
		int[] randomNeighbor = neighbor.get(random.nextInt(neighbor.size()));
		moveTo(randomNeighbor[0], randomNeighbor[1], 0, false);
	}
	
	/** 
	 * Finds a female sheep which is ready to breed, and tells her to walk to a random tile
	 *  next to this sheep.
	 */
	
	protected void propagate(){
	
		ArrayList<int[]> neighbor = HexagonUtils.neighborTiles(xPos, yPos, false);
		int[] randomNeighbor = neighbor.get(random.nextInt(neighbor.size()));
		
		Animal animal = findReadyToBreedAnimal();
				if(animal != null){
					animal.lock();
					race.allowedWorker.release();
					boolean goalReached =  animal.moveTo(randomNeighbor[0], randomNeighbor[1], 0, false); // waiting for the female
					try {
						race.allowedWorker.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(goalReached == true){
						animal.setPregnant(true);
					}
					animal.unlock();
				}
	}
	
	/** 
	 * The animal moves to position x, y. And when destination is reached, return true.
	 * 
	 * @param x coordinate to move to.
	 * @param y coordinate to move to.
	 * @return true is destination is reached, else false.
	 */
	
	protected boolean moveTo(int x, int y, int blocked, boolean oneStep){
		if(!Astar.noSpecies(xPos, yPos)){
			alive = false;
			return false;
		}
		
		Deque<int[]> path = Astar.calculatePath(xPos, yPos, x, y);
		if(path.size() == 0)
			return false;
		path.removeFirst(); // removes the current location
		for(int [] nextCord : path){
			race.allowedWorker.release();
			if(this.race.getSpecName().equals("Sheep")){
				try {
				    Thread.sleep((int)Globals.getSetting("Sheep sleep", "Sheep"));
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
			}else if(this.race.getSpecName().equals("Wolf")){
				try {
				    Thread.sleep((int)Globals.getSetting("Wolf sleep", "Wolf"));
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
			}
			
			try {
				race.allowedWorker.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			boolean moved = race.moveSpecies(xPos, yPos, nextCord[0], nextCord[1]);
			
			if(!moved && blocked < 4){
				blocked++;
				this.moveTo(x, y, blocked, oneStep);
			}else if(!moved && blocked > 3){
				return false;
			}else if(oneStep){
				return true;
			}
		}
		return true;
	}
	
	/** 
	 * Calculates the correct rotation for a sheep when moved.
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
	

	public float peekNeed(Needs need){
		return 1.0f;
	}
	
	public float getNeed(Needs need){
		return 1.0f;
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
	
	/** Sets pregnant to bool. If bool == true and pregnant == false, then the hunger and thirst of this animal is set
	 *  to 0.3.
	 * 
	 * @param bool Pregnant will be set to this.
	 */
	
	public void setPregnant(boolean bool){
		if(!pregnant && bool){
			hunger = 0.3f;
			thirst = 0.3f;
			this.pregnant = bool;
		}else{
			this.pregnant = bool;
		}
	}
	
	public boolean getPregnant(){
		return pregnant;
	}
	
	public int getX(){
		return xPos;
	}
	
	public int getY(){
		return yPos;
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
	
	public void lock(){
		try {
			busy.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void unlock(){
		busy.release();
	}
	
	public void setHunter(Animal animal){
		this.predator = animal;
	}
}
