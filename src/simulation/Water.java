package simulation;

import utilities.Globals;

public class Water implements Runnable{
	int tickLength;
	private float[][] groundWaterLevel;
	
	public Water(int tickLength){
		this.tickLength = tickLength;
		groundWaterLevel = new float[Globals.width][Globals.height];		
	}
	
	public void run() {
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
		return 0.5f;//groundWaterLevel[x][y];
	}
	
	private void step() {
		
	}
}
