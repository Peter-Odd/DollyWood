package simulation;

import utilities.Fractal;
import utilities.Globals;

public class Water implements Runnable{
	int tickLength;
	private float[][] groundWaterLevel;
	private float[][] cloudWaterLevel;
	
	private float[][] fractalMap;
	
	public Water(int tickLength){
		this.tickLength = tickLength;
		groundWaterLevel = new float[Globals.width][Globals.height];		
		cloudWaterLevel = new float[Globals.width][Globals.height];		
		fractalMap = new float[Globals.width][Globals.height];
		Fractal.generateFractal(fractalMap, 8.0f, 0.0f, 4.0f, 1.5f);
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
		float waterSum = 0.0f;
		float waterLossFactor = 0.003f;
		for(int x = 0; x < Globals.width; x++){
			for(int y = 0; y < Globals.height; y++){
				groundWaterLevel[x][y] -= waterLossFactor;
				if(groundWaterLevel[x][y] < 0.0f)
					groundWaterLevel[x][y] = 0.0f;
				waterSum += groundWaterLevel[x][y];
			}
		}
		cloudWaterLevel = Fractal.cutMap(fractalMap, 1.0f, 0.99f-(waterSum/(Globals.width*Globals.height)));
		if(waterSum < Globals.width*Globals.height*0.2f){
			//toggleDownfall();
		}
	}
}
