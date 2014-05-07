package simulation;


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

	/**
	 * 
	 * @param tickLength Time (in ms) thread should sleep between "work"
	 * @param xPos a trees x-coordinate
	 * @param yPos a trees y-coordinate
	 * @param race the race a tree belongs to
	 */
	public Tree(int tickLength, int xPos, int yPos, Race race) {
		this.tickLength = tickLength;
		treeHealth = 1.0f; //initial health value of 1.0f (max). ev random
		this.xPos = xPos;
		this.yPos = yPos;
		this.race = race;
	}

	public float getTreeHealth() {
		return treeHealth;
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
		//				sunIntensity += nc.getNeed(new Needs("SunLight", 1.0f), xPos, yPos);
		//			}
		//			treeHealth += sunIntensity;
		//		}

		treeHealth += treeGrowth*0.1f; //increase treeHealth

		if (treeHealth > 1.0f)
			treeHealth = 1.0f;
		else if (treeHealth < 0.0f) 
			treeHealth = 0.0f;

		treeHealth -= 0.05; //decrease treeHealth

		if (treeHealth < 0.1) { //tree's health too bad, lets kill it
			race.getAndRemoveSpeciesAt(xPos, yPos);
		}
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
