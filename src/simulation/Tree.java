package simulation;


import java.util.ArrayList;
import java.util.Random;

import utilities.HexagonUtils;
import utilities.Needs;
import utilities.NeedsController;
import utilities.NeedsController.NeedsControlled;

/**
 * Tree object
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */
public class Tree extends Animal implements Runnable {

	private int tickLength;		//ms between each step (loop)
	private float treeHealth;	//a trees health
	private float treeSeed;		//a trees seed amount
	private int xPos;			//a trees x-position
	private int yPos;			//a trees y-position
	private int count;			//number of times a tree has "died"
	private Race race;			//the race a tree belongs to

	/**
	 * 
	 * @param tickLength Time (in ms) thread should sleep between "work"
	 * @param xPos a trees x-coordinate
	 * @param yPos a trees y-coordinate
	 * @param race a tree belongs to race
	 */
	public Tree(int tickLength, int xPos, int yPos, Race race) {
		this.tickLength = tickLength;
		treeHealth = new Random().nextFloat(); 				//initial health value of random between 0.0f and 0.99..f
		this.xPos = xPos;									//a trees x position
		this.yPos = yPos;									//a trees y position
		this.treeSeed = new Random().nextFloat(); 			//created random number for tree seed
		this.treeSeed = (float) ((treeSeed * 0.5f) + 0.1f);	//random value for tree seed between 0.1f and 0.6f
		this.race = race;									//the race a tree belongs to
		setRotation(new Random().nextFloat() * 360); 		//Rotate tree randomized between 0 & 360 degrees, just to create some variation
		this.count++;
	}

	public float getTreeHealth() {
		return treeHealth;
	}	

	public float getSize() {
		return treeHealth; //this manages the growth/degrowth of the tree
	}

	/**
	 * Spawns a new tree if treeSeed is greater than 0.99. This value is increased once in a while
	 */
	private void spawnTree() {
		if (treeSeed > 0.99 && treeHealth >= 1.0f) {	//lets spawn a tree
			ArrayList<int[]> neighbors = HexagonUtils.neighborTiles(this.xPos, this.yPos, 2, false);
			int randomSpawnedTree = new Random().nextInt(neighbors.size());
			int treeXPos = neighbors.get(randomSpawnedTree)[0];
			int treeYPos = neighbors.get(randomSpawnedTree)[1];
			if (race.getSpeciesAt(xPos, yPos) == null) {
				Tree tree = new Tree(400, treeXPos, treeYPos, this.race);
				this.race.setSpeciesAt(treeXPos, treeYPos, tree);
				Thread treeThread = new Thread(tree);
				treeThread.start();	
				this.treeSeed = 0.1f; //reset treeSeed when a tree has spawned a new tree
			}
		} else {
			treeSeed += 0.01; //increase the treeSeed
		}
	}

	/**
	 * For each "tickLength" (see doc for constructor) this method will run once
	 */
	private void step() {
//		float randomRun = new Random().nextFloat();
//		if (randomRun >= 0.50f) {

			spawnTree(); 		//check if a new tree is to be spawned

			float waterAmmount = 0.0f;

			for (int[] neighbor : HexagonUtils.neighborTiles(xPos, yPos, true)) {
				for (NeedsControlled nc : NeedsController.getNeed("Water")) {
					waterAmmount += nc.getNeed(new Needs("Water", 1.0f), neighbor[0], neighbor[1]); //what is a good value 0.5f?
				}
			}
			float treeGrowth = (float) (-(Math.pow(waterAmmount-3.5f, 2))/6.125f+1.0f);
			//			if (waterAmmount >= 2.5f/* && treeHealth >= 1.45f*/) { //TODO: How to decrease health when tree in excess water?
			//				treeHealth -= 0.5f; 	//decrease treeHealth
			//				count++;
			//			} else {
			//				treeHealth += treeGrowth*0.02f; //increase treeHealth
			//			}

			treeHealth += treeGrowth*0.02f; //increase treeHealth

			//		if (treeGrowth > 0.0f) {	//if trees should depend on sunlight
			//			float sunIntensity = 0.0f;
			//			for (NeedsControlled nc : NeedsController.getNeed("SunLight")) {
			//				sunIntensity += nc.getNeed(new Needs("SunLight", 0.4f), xPos, yPos);
			//			}
			//			treeHealth += sunIntensity;
			//		}

			if (treeHealth > 1.5f)
				treeHealth = 1.5f;
			else if (treeHealth < 0.0f)  {
				treeHealth = 0.0f;
				count++; 				//increase count (number of times a tree has "died"
			}

			if (count >= 3) {			// a tree has "died" 3 times, let's remove it
				race.getAndRemoveSpeciesAt(xPos, yPos);
			} else {
				treeHealth -= 0.01; 	//decrease treeHealth
			}

//		}//end random

	} 


	/**
	 * Starter point for the thread.
	 * The entire tree simulation will run here.
	 */
	public void run() {
		while(true) {
			step();
			try {
				Thread.sleep(tickLength);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

}
