package simulation;

import java.util.Random;

import utilities.Globals;
import utilities.HexagonUtils;

public class Cloud implements Runnable{
	
	private int tickLength;
	private float size = 0.0f;
	private float xPos;
	private float yPos;
	private boolean downfall = false;
	private float[][] xCurrent;
	private float[][] yCurrent;
	
	public Cloud(int tickLength, float xPos, float yPos, float size, float[][] xCurrent, float[][] yCurrent){
		this.tickLength = tickLength;
		this.xPos = xPos;
		this.yPos = yPos;
		this.size = size;
		this.ticks = (int) (size*10);
		this.xCurrent = xCurrent;
		this.yCurrent = yCurrent;
	}
	
	int ticks = 0;
	public void run() {
		while(true){
			ticks++;
			if(ticks > 300){
				downfall = true;
				ticks = 0;
			}
			//if(this.size > 3.0f)
			//	downFall = true;
			if(this.size < 0.0f){
				Random random = new Random();
				xPos = random.nextFloat()*(Globals.width-1);
				xPos = random.nextFloat()*(Globals.height-1);
				downfall = false;
			}
			step();
			try{
				Thread.sleep(tickLength);
			}
			catch(InterruptedException e){
				
			}
		}
	}

	private void step() {
		float windStrength = (6.0f-this.size)*0.01f;
		float drawSize = 0.02f;
		float fallSize = 0.4f;
		xPos = (xPos+xCurrent[(int)xPos][(int)(yPos)]*windStrength)%Globals.width;
		yPos = (yPos+yCurrent[(int)xPos][(int)(yPos)]*windStrength)%Globals.height;
		for(int[] neighbor : HexagonUtils.neighborTiles((int)xPos, (int)yPos, true)){
			if(this.downfall){
				//System.out.println(this.size);
				this.size -= fallSize/13.0f;
				Globals.water.addGroundWaterLevel(neighbor[0], neighbor[1], fallSize);
			}
			if(Globals.water.drawGroundWaterLevel(neighbor[0], neighbor[1], drawSize))
				this.size += drawSize/13.0f;
		}
	}

	public boolean downfall(){
		return downfall;
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
