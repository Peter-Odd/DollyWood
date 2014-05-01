package simulation;

import java.util.ArrayList;
import java.util.Random;

import utilities.Fractal;
import utilities.Globals;
import utilities.HexagonUtils;
import utilities.NeedsController;
import utilities.Needs;
import utilities.NeedsController.NeedsControlled;
/**
 * The complete water system will simulate both water flowing down to a lower position and the dissipation through dry ground.
 * This is also a container for the clouds which extends the simulation with rain and evaporation.
 * @author OSM Group 5 - DollyWood project
 * @version 1.01
 */
public class Water implements Runnable, NeedsControlled{
	int tickLength;
	private float[][] groundWaterLevel;
	
	private ArrayList<Cloud> clouds = new ArrayList<>();
	
	private float[][] fractalMap;
	
	/**
	 * Constructor
	 * @param tickLength sets the sleep duration of the Thread
	 */
	public Water(int tickLength){
		this.tickLength = tickLength;
		groundWaterLevel = new float[Globals.width][Globals.height];		
		fractalMap = new float[Globals.width][Globals.height];
		Fractal.generateFractal(fractalMap, 6.0f, 0.0f, 3.0f, 1.5f);
		NeedsController.registerNeed("Water", this);
	}
	
	/**
	 * Starter point for the thread.
	 * The clouds will be created here and the simulation will start here.
	 */
	public void run() {
		for(int x = 0; x < Globals.width; x++)
			for(int y = 0; y < Globals.height; y++)
				groundWaterLevel[x][y] = 0.5f;
		
		Random random = new Random();
		float[][] xCurrent = new float[Globals.width][Globals.height];
		float[][] yCurrent = new float[Globals.width][Globals.height];
		Fractal.generateFractal(xCurrent, 1.0f, 0.0f, 2.0f, 1.5f);
		Fractal.generateFractal(yCurrent, 1.0f, 0.0f, 2.0f, 1.5f);
		for(int i = 0; i < (int)(random.nextInt(5))+10; i++){
			Cloud cloud = new Cloud(100, random.nextFloat()*Globals.width, random.nextFloat()*Globals.height, random.nextFloat()*3.0f, xCurrent, yCurrent);
			Thread cloudThread = new Thread(cloud);
			cloudThread.start();
			clouds.add(cloud);
		}
		while(true){
			step();
			try{
				Thread.sleep(tickLength);
			}
			catch(InterruptedException e){
				
			}
		}
	}

	/**
	 * 
	 * @param x position
	 * @param y position
	 * @return The amount of ground water at (x,y)
	 */
	public float getGroundWaterLevel(int x, int y){
		return groundWaterLevel[x][y];
	}
	/**
	 * Adds amount to the ground water at (x,y)
	 * @param x
	 * @param y
	 * @param ammount
	 */
	public void addGroundWaterLevel(int x, int y, float ammount){
		groundWaterLevel[x][y] += ammount;
	}
	/**
	 * Removes amount of water from the ground at (x,y)
	 * @param x
	 * @param y
	 * @param ammount
	 * @return true if there was enough water to remove, false otherwise. Note that it will not remove anything if the function returns false.
	 */
	public boolean drawGroundWaterLevel(int x, int y, float ammount){
		if(groundWaterLevel[x][y] >= ammount){
			groundWaterLevel[x][y] -= ammount;
			return true;
		}
		else
			return false;
	}
	
	public ArrayList<Cloud> getClouds(){
		return clouds;
	}
	
	/**
	 * Steps the simulation one step forward.
	 * It will at this point only flow the water and dissipate it.
	 */
	private void step() {
		//evaporate();
		waterFlow();
		dissipate();
	}
	
	/**
	 * Dissipate the water.
	 * This moves water from a wet/moist position to a dry position.
	 * The amount moved is dependent on the difference in ground water amount.
	 */
	private void dissipate() {
		float flowRate = 0.3f;
		for(int x = 0; x < Globals.width; x++){
			for(int y = 0; y < Globals.height; y++){
				if(groundWaterLevel[x][y] >= flowRate){
					for(int[] neighbor : HexagonUtils.neighborTiles(x, y, false)){
						if(groundWaterLevel[neighbor[0]][neighbor[1]] < groundWaterLevel[x][y]){
							float flow = (groundWaterLevel[x][y]-groundWaterLevel[neighbor[0]][neighbor[1]])*flowRate;
							groundWaterLevel[x][y] -= flow;
							groundWaterLevel[neighbor[0]][neighbor[1]] += flow;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Flows the water from a high position to a low position.
	 * Note that it will only flow down if the lower position has a waterLevel of < 1.0
	 */
	private void waterFlow() {
		float flowRate = 0.04f;
		float flowThreshold = 0.1f;
		for(int x = 1; x < Globals.width-1; x++){
			for(int y = 1; y < Globals.height-1; y++){
				if(groundWaterLevel[x][y] >= flowRate){
					float height = Globals.heightmap[x][y];
					for(int[] neighbor : HexagonUtils.neighborTiles(x, y, false)){
						if(Globals.heightmap[neighbor[0]][neighbor[1]] < height && groundWaterLevel[neighbor[0]][neighbor[1]] < 1.0f-flowThreshold){
							groundWaterLevel[x][y] -= flowRate;
							groundWaterLevel[neighbor[0]][neighbor[1]] += flowRate;
						}
					}
				}
			}
		}
	}

	public float getNeed(Needs need, int x, int y) {
		if(groundWaterLevel[x][y] >= need.getAmmount()){
			//groundWaterLevel[x][y] -= need.getAmmount();
			return need.getAmmount();
		}
		else{
			float tmp = groundWaterLevel[x][y];
			//groundWaterLevel[x][y] = 0.0f;
			return tmp;
		}
	}
}
