package simulation;


import utilities.Globals;
import utilities.HexagonUtils;

public class Tree extends Animal implements Runnable {

	private int tickLength;
	private float[][] treeHealth;


	public Tree(int tickLength) {
		this.tickLength = tickLength;
		treeHealth = new float[Globals.width][Globals.height];
	}
	
	public float getTreeHealth(int x, int y) {
		return treeHealth[x][y];
	}	
	/*En kommentar*/
	private void step() {
		for (int x = 0; x < Globals.width; x++) {
			for (int y = 0; y < Globals.height; y++) {
			//	float waterAmmount = 0.0f;
				for (int[] neighbor : HexagonUtils.neighborTiles(x, y, true)) {
				//	waterAmmount += Globals.water.getGroundWaterLevel(neighbor[0], neighbor[1]);
					Globals.water.drawGroundWaterLevel(neighbor[0], neighbor[1], 1.0f);
				}
//				float treeGrowth = (float) (-(Math.pow(waterAmmount-3.5f, 2))/6.125f+1.0f);
//				treeHealth[x][y] += treeGrowth*0.01f;
//				
//				if (treeHealth[x][y] > 1.0f)
//					treeHealth[x][y] = 1.0f;
//				else if (treeHealth[x][y] < 0.0f) 
//					treeHealth[x][y] = 0.0f;
			}
		}
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
