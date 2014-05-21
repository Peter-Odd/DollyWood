package simulation;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;

import utilities.Globals;
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

	private float treeHealth;		//a trees health
	private float treeSeed;			//a trees seed amount
	private int xPos;				//a trees x-position
	private int yPos;				//a trees y-position
	private long birthTime;			//a trees age in milliseconds
	private Race race;				//the race a tree belongs to

	/**
	 * 
	 * @param xPos a trees x-coordinate
	 * @param yPos a trees y-coordinate
	 * @param race a tree belongs to race
	 */
	public Tree(int xPos, int yPos, Race race) {
		treeHealth = new Random().nextFloat(); 				//initial health value of random between 0.0f and 0.99..f
		this.xPos = xPos;									//a trees x position
		this.yPos = yPos;									//a trees y position
		this.treeSeed = Globals.getSetting("Tree seed amount", "Tree"); //Tree seed amount value
		this.race = race;									//the race a tree belongs to
		birthTime = (System.currentTimeMillis() / 1000) - (long)(new Random().nextInt(25));
		setRotation(new Random().nextFloat() * 360); 		//Rotate tree randomized between 0 & 360 degrees, just to create some variation
		race.numberOfInstances.incrementAndGet();	
	}

	public float getTreeHealth() {
		return treeHealth;
	}	

	public float getSize() {
		return treeHealth; //this manages the growth/degrowth of the tree
	}

	/**
	 * Spawns a new tree if treeSeed is greater than spawnProbability. This value is increased once in a while
	 */
	private void spawnTree() {
		if (treeSeed > 0.90f && treeHealth >= 1.0f) {	//lets spawn a tree
			ArrayList<int[]> neighbors = HexagonUtils.neighborTiles(this.xPos, this.yPos, 2, false);
			int randomSpawnedTree = new Random().nextInt(neighbors.size());
			int treeXPos = neighbors.get(randomSpawnedTree)[0];
			int treeYPos = neighbors.get(randomSpawnedTree)[1];

			float treeProximity = 0.0f;
			for(NeedsControlled nc : NeedsController.getNeed("Tree"))
				for(int[] neighbor : HexagonUtils.neighborTiles(treeXPos, treeYPos, 2, true))
					treeProximity += nc.getNeed(new Needs("Tree", 1.0f), neighbor[0], neighbor[1]);
			if (race.getSpeciesAt(treeXPos, treeYPos) == null && treeProximity < 2) {
				Tree tree = new Tree(treeXPos, treeYPos, this.race);
				this.race.setSpeciesAt(treeXPos, treeYPos, tree);
				Thread treeThread = new Thread(tree);
				treeThread.start();	
				race.numberOfInstances.incrementAndGet();
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
		spawnTree(); 		//check if a new tree is to be spawned

		float waterAmmount = 0.0f;

		for (int[] neighbor : HexagonUtils.neighborTiles(xPos, yPos, true)) {
			for (NeedsControlled nc : NeedsController.getNeed("Water")) {
				waterAmmount += nc.getNeed(new Needs("Water", 1.0f), neighbor[0], neighbor[1]); //what is a good value 0.5f?
			}
		}
		float treeGrowth = (float) (-(Math.pow(waterAmmount-3.5f, 2))/6.125f+1.0f);

		treeHealth += treeGrowth*0.02f; //increase treeHealth

		if (treeHealth > 1.5f)
			treeHealth = 1.5f;
		else if (treeHealth < 0.0f)  {
			treeHealth = 0.0f;
		}

		treeHealth -= 0.01; 	//decrease treeHealth
	} 

	private float getNumberofTrees(){
		return (float)race.numberOfInstances.get();
	}

	/**
	 * Starter point for the thread.
	 * The entire tree simulation will run here.
	 */
	public void run() {
		Globals.registerGraph("Number of trees", "Tree", new Callable<Float>() {
			public Float call() throws Exception {
				return getNumberofTrees();
			}
		}, 3000);	

		while( ( (long)(System.currentTimeMillis()/1000) - birthTime) < (long)Globals.getSetting("Tree time of life (sec)", "Tree") ) {
			step();
			try {
				Thread.sleep((int)Globals.getSetting("Tree sleep (ms)", "Tree"));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		race.getAndRemoveSpeciesAt(xPos, yPos);
		race.numberOfInstances.decrementAndGet();
	}
}
