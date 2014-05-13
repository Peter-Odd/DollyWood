package simulation;

import utilities.Globals;
import utilities.HexagonUtils;
import utilities.NeedsController;
import utilities.Needs;
import utilities.NeedsController.NeedsControlled;

/**
 * Grass system that simulates the growth and decay of grass across the world.
 * This is far from a realistic system as it only needs to be near the right amount of water to grow.
 * It also doesn't need to send out seeds or anything, the grass is everywhere all the time, it is just a matter of how much.
 * <br /><br />
 * The growth is as previously stated dependent on water. If there isn't enough water the grass will slowly die. 
 * If there is enough water (0.5 average at neighbors) it will grow as fast as possible.
 * And lastly if there is too much water it will also die.
 * <br /><b>-((x-3.5)^2)/6.125+1.0</b> this is what determines the growth rate, where x = waterAmmount.
 * <br /><br />
 * As of version 1.2, the grass also wants sunlight to grow. The decay of grass is constant no matter the sun level, but the growth is depending on the sun intensity.
 * <br /><br />
 * Grass registers a provider of the need <code>"Plant"</code>
 * @author OSM Group 5 - DollyWood project
 * @version 1.2
 */
public class Grass extends Race implements Runnable, NeedsControlled{

	private float[][] grassLevel;

	private Grass(String specName) {
		super(specName);
	}

	/**
	 * Constructor
	 */
	public Grass(){
		this("Grass");
		grassLevel = new float[Globals.width][Globals.height];
		NeedsController.registerNeed("Plant", this);
	}

	public float getGrassAt(int x, int y){
		return grassLevel[x][y];
	}
	
	public void decrementGrassLevel(int x, int y, float value){
		grassLevel[x][y] -= value;
	}
	
	private void step() {
		for(int x = 0; x < Globals.width; x++){
			for(int y = 0; y < Globals.height; y++){
				float waterAmmount = 0.0f;
				for(int[] neighbor : HexagonUtils.neighborTiles(x, y, true)){
					//waterAmmount += Globals.water.getGroundWaterLevel(neighbor[0], neighbor[1]);
					for(NeedsControlled nc : NeedsController.getNeed("Water")){
						waterAmmount += nc.getNeed(new Needs("Water", 1.0f), neighbor[0], neighbor[1]);
					}
				}
				//-((x-3.5)^2)/6.125+1.0 = exponential, ranging from -1 to 1, with center i 3.5. This asumes that waterAmmount ranges from 0 to 7
				float grassGrowth = (float) (-(Math.pow(waterAmmount-3.5f, 2))/6.125f+1.0f);
				if(grassGrowth > 0.0f){
					float sunIntensity = 0.0f;
					for(NeedsControlled nc : NeedsController.getNeed("SunLight")){
						sunIntensity += nc.getNeed(new Needs("SunLight", 1.0f), x, y);
					}
					grassGrowth *= sunIntensity;
				}
				grassLevel[x][y] += grassGrowth*0.01f;
				
				if(grassLevel[x][y] > 1.0f)
					grassLevel[x][y] = 1.0f;
				else if(grassLevel[x][y] < 0.0f)
					grassLevel[x][y] = 0.0f;
			}
		}
	}
	
	/**
	 * Starter point for the thread.
	 * The entire grass simulation will run here
	 */
	public void run() {
		while(true){
			step();
			try {
				Thread.sleep(Globals.grassSleepLength);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public float getNeed(Needs need, int x, int y) {
		if(grassLevel[x][y] >= need.getAmmount()){
			grassLevel[x][y] -= need.getAmmount();
			return need.getAmmount();
		}
		else{
			float tmp = grassLevel[x][y];
			grassLevel[x][y] = 0.0f;
			return tmp;
		}
	}
}
