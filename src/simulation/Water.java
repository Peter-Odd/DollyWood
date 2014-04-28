package simulation;

import utilities.Globals;

public class Water implements Runnable{
	int tickLength;
	private float[][] groundWaterLevel;
	private float[][] cloudWaterLevel;
	
	public Water(int tickLength){
		this.tickLength = tickLength;
		groundWaterLevel = new float[Globals.width][Globals.height];		
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
	
	private void step() {
		float waterSum = 0.0f;
		float waterLossFactor = 0.003f;
		for(int x = 0; x < Globals.width; x++){
			for(int y = 0; y < Globals.height; y++){
				groundWaterLevel[x][y] -= 0.003f;
				if(groundWaterLevel[x][y] < 0.0f)
					groundWaterLevel[x][y] = 0.0f;
				waterSum += groundWaterLevel[x][y];
			}
		}
		if(waterSum < Globals.width*Globals.height*0.2f){
			//toggleDownfall();
		}
	}
}
