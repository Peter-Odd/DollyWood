package simulation;


import utilities.Globals;
import utilities.HexagonUtils;
import utilities.Needs;
import utilities.NeedsController;
import utilities.NeedsController.NeedsControlled;

public class Tree extends Animal implements Runnable {

	private int tickLength;
	private float treeHealth;
	private int xPos;
	private int yPos;


	public Tree(int tickLength, int xPos, int yPos) {
		this.tickLength = tickLength;
		treeHealth = 1.0f; //initial health value of 1.0f (max). ev random
		this.xPos = xPos;
		this.yPos = yPos;
	}

	public float getTreeHealth() {
		return treeHealth;
	}	

	private void step() {
		float waterAmmount = 0.0f;
		for (int[] neighbor : HexagonUtils.neighborTiles(xPos, yPos, true)) {
			//	waterAmmount += Globals.water.getGroundWaterLevel(neighbor[0], neighbor[1]);
			//	Globals.water.drawGroundWaterLevel(neighbor[0], neighbor[1], 1.0f);
			for (NeedsControlled nc : NeedsController.getNeed("Water")) {
				waterAmmount += nc.getNeed(new Needs("Water", 1.0f), neighbor[0], neighbor[1]);
			}
		}
		float treeGrowth = (float) (-(Math.pow(waterAmmount-3.5f, 2))/6.125f+1.0f);
		treeHealth += treeGrowth*0.01f;

		if (treeHealth > 1.0f)
			treeHealth = 1.0f;
		else if (treeHealth < 0.0f) 
			treeHealth = 0.0f;
	}



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
