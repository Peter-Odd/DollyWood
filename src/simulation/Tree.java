package simulation;


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

	private int tickLength;
	private float treeHealth;
	private int xPos;
	private int yPos;
	private Race race;
	//	private Random random;

	/**
	 * 
	 * @param tickLength Time (in ms) thread should sleep between "work"
	 * @param xPos a trees x-coordinate
	 * @param yPos a trees y-coordinate
	 * @param race the race a tree belongs to
	 */
	public Tree(int tickLength, int xPos, int yPos, Race race) {
		this.tickLength = tickLength;
		treeHealth = new Random().nextFloat(); //initial health value of 1.0f (max). ev random
		this.xPos = xPos;
		this.yPos = yPos;
		this.race = race;
		setRotation(new Random().nextFloat() * 360); //Rotate tree randomized between 0 & 360 degrees
	}

	public float getTreeHealth() {
		return treeHealth; 
	}	


	public float getSize() {
		return treeHealth; //this manages the growth/degrowth of the tree
	}

	/**
	 * Each "tickLength" (see doc for constructor) this method will run once
	 */
	private void step() {
		float waterAmmount = 0.0f;

		for (int[] neighbor : HexagonUtils.neighborTiles(xPos, yPos, true)) {
			for (NeedsControlled nc : NeedsController.getNeed("Water")) {
				waterAmmount += nc.getNeed(new Needs("Water", 0.5f), neighbor[0], neighbor[1]); //what is a good value 0.5f?
			}
		}
		float treeGrowth = (float) (-(Math.pow(waterAmmount-3.5f, 2))/6.125f+1.0f);
		//TODO: if too much water around tree, decrease treeHealth

//		if (treeGrowth > 0.0f) {	//if trees should depend on sunlight
//			float sunIntensity = 0.0f;
//			for (NeedsControlled nc : NeedsController.getNeed("SunLight")) {
//				sunIntensity += nc.getNeed(new Needs("SunLight", 0.4f), xPos, yPos);
//			}
//			treeHealth += sunIntensity;
//		}

		treeHealth += treeGrowth*0.02f; //increase treeHealth

		if (treeHealth > 1.5f)
			treeHealth = 1.5f;
		else if (treeHealth < 0.0f) 
			treeHealth = 0.0f;

		treeHealth -= 0.01; //decrease treeHealth

		//		if (treeHealth < 0.1) { //tree's health too bad, lets kill it
		//			race.getAndRemoveSpeciesAt(xPos, yPos);
		//		}

		getSize(); //this manages the growth/degrowth of the tree
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
