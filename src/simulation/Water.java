package simulation;

import utilities.Fractal;
import utilities.Globals;
import utilities.HexagonUtils;

public class Water implements Runnable{
	int tickLength;
	private float[][] groundWaterLevel;
	private float[][] cloudWaterLevel;
	
	private float[][] fractalMap;
	private boolean downfall;
	
	public Water(int tickLength){
		this.tickLength = tickLength;
		groundWaterLevel = new float[Globals.width][Globals.height];		
		cloudWaterLevel = new float[Globals.width][Globals.height];		
		fractalMap = new float[Globals.width][Globals.height];
		Fractal.generateFractal(fractalMap, 6.0f, 0.0f, 3.0f, 1.5f);
	}
	
	public void run() {
		for(int x = 0; x < Globals.width; x++)
			for(int y = 0; y < Globals.height; y++)
				groundWaterLevel[x][y] = 0.75f;
		
		while(true){
			step();
			try{
				Thread.sleep(tickLength);
			}
			catch(InterruptedException e){
				
			}
		}
	}

	public float getGroundWaterLevel(int x, int y){
		return groundWaterLevel[x][y];
	}
	
	public float[][] getCloudWaterLevel(){
		return cloudWaterLevel;
	}
	
	private void step() {
		evaporate();
		
		float waterSum = countWaterTotal();
		cloudWaterLevel = Fractal.cutMap(fractalMap.clone(), 1.0f, 0.99f-(waterSum/(Globals.width*Globals.height)));
		if(waterSum < Globals.width*Globals.height*0.2f){
			this.downfall = true;
		}
		downfall();
		if(waterSum > Globals.width*Globals.height*0.7f){
			this.downfall = false;
		}
		
		waterFlow();
		dissipate();
	}
	

	private void dissipate() {
		float flowRate = 0.015f;
		for(int x = 1; x < Globals.width-1; x++){
			for(int y = 1; y < Globals.height-1; y++){
				if(groundWaterLevel[x][y] >= flowRate){
					for(int[] neighbor : HexagonUtils.neighborTiles(x, y, false)){
						if(groundWaterLevel[neighbor[0]][neighbor[1]] < 1.0f-flowRate){
							groundWaterLevel[x][y] -= flowRate;
							groundWaterLevel[neighbor[0]][neighbor[1]] += flowRate;
						}
					}
				}
			}
		}
	}
	
	private float countWaterTotal(){
		float waterSum = 0.0f;
		for(int x = 0; x < Globals.width; x++){
			for(int y = 0; y < Globals.height; y++){
				waterSum += groundWaterLevel[x][y];
			}
		}
		return waterSum;
	}
	
	private void evaporate(){
		float waterLossFactor = 0.003f;
		for(int x = 0; x < Globals.width; x++){
			for(int y = 0; y < Globals.height; y++){
				groundWaterLevel[x][y] -= waterLossFactor;
				if(groundWaterLevel[x][y] < 0.0f)
					groundWaterLevel[x][y] = 0.0f;
			}
		}
	}

	private void waterFlow() {
		float flowRate = 0.02f;
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

	private void downfall() {
		if(downfall){
			System.out.println("Downfall active");
			for(int x = 0; x < Globals.width; x++){
				for(int y = 0; y < Globals.height; y++){
					groundWaterLevel[x][y] += 1.0f-cloudWaterLevel[x][y];
					if(groundWaterLevel[x][y] > 1.0f)
						groundWaterLevel[x][y] = 1.0f;
				}
			}
		}
	}
}
