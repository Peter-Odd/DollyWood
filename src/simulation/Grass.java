package simulation;

import utilities.Globals;
import utilities.HexagonUtils;

public class Grass extends Race implements Runnable{

	private int tickLength;
	private float[][] grassLevel;

	private Grass(String specName) {
		super(specName);
	}
	
	public Grass(int tickLength){
		this("Grass");
		this.tickLength = tickLength;
		grassLevel = new float[Globals.width][Globals.height];
	}

	public float getGrassAt(int x, int y){
		return grassLevel[x][y];
	}
	
	private void step() {
		for(int x = 0; x < Globals.width; x++){
			for(int y = 0; y < Globals.height; y++){
				float waterAmmount = 0.0f;
				for(int[] neighbor : HexagonUtils.neighborTiles(x, y, true)){
					waterAmmount += Globals.water.getGroundWaterLevel(neighbor[0], neighbor[1]);
				}
				//-((x-3.5)^2)/6.125+1.0 = exponential, ranging from -1 to 1, with center i 3.5. This asumes that waterAmmount ranges from 0 to 7
				float grassGrowth = (float) (-(Math.pow(waterAmmount-3.5f, 2))/6.125f+1.0f);
				grassLevel[x][y] += grassGrowth*0.01f;
				
				if(grassLevel[x][y] > 1.0f)
					grassLevel[x][y] = 1.0f;
				else if(grassLevel[x][y] < 0.0f)
					grassLevel[x][y] = 0.0f;
			}
		}
	}
	public void run() {
		while(true){
			step();
			try {
				Thread.sleep(tickLength);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
