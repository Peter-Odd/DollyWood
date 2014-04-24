package simulation;

public class DayNightCycle implements Runnable{
	private float time;
	private float stepSize;
	private int tickLength;
	
	public DayNightCycle(float stepSize, int tickLength) {
		this.stepSize = stepSize;
		this.tickLength = tickLength;
	}

	public void run() {
		while(true){
			time = (time+stepSize)%24;
			try{
				Thread.sleep(tickLength);
			}
			catch(InterruptedException e){
				System.out.println("Could not sleep DayNightCycle, stopping thread\n");
				e.printStackTrace();
				break;
			}
		}
	}

	public float getTime(){
		return time;
	}
}
