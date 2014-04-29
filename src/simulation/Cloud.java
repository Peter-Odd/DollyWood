package simulation;

import utilities.Globals;
import utilities.HexagonUtils;

public class Cloud implements Runnable{
	
	private int tickLength;
	private float size = 0.0f;
	private float xPos;
	private float yPos;
	private boolean downFall = false;
	
	
	public Cloud(int tickLength, float xPos, float yPos){
		this.tickLength = tickLength;
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public void run() {
		while(true){
			if(this.size > 3.0f)
				downFall = true;
			if(this.size < 0.5f)
				downFall = false;
			step();
			try{
				Thread.sleep(tickLength);
			}
			catch(InterruptedException e){
				
			}
		}
	}

	private void step() {
		float windStrength = this.size*0.01f;
		float drawSize = 0.001f;
		float fallSize = 0.01f;
		xPos = (xPos+windStrength)%Globals.width;
		yPos = (yPos+windStrength)%Globals.height;
		for(int[] neighbor : HexagonUtils.neighborTiles((int)xPos, (int)yPos, true)){
			if(this.downFall){
				//System.out.println(this.size);
				this.size -= fallSize;
				Globals.water.addGroundWaterLevel(neighbor[0], neighbor[1], fallSize);
			}
			if(Globals.water.drawGroundWaterLevel(neighbor[0], neighbor[1], drawSize))
				this.size += drawSize;
		}
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public float getxPos() {
		return xPos;
	}

	public void setxPos(float xPos) {
		this.xPos = xPos;
	}

	public float getyPos() {
		return yPos;
	}

	public void setyPos(float yPos) {
		this.yPos = yPos;
	}
}
