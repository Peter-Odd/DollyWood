package simulation;

import utilities.Globals;
import utilities.Needs;
import utilities.NeedsController;
import utilities.NeedsController.NeedsControlled;

/**
 * DayNightCycle will simply run the world clock making it day/night.
 * <br /><br />
 * DayNightCycle registers a provider of the need <code>"SunLight"</code>
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */
public class DayNightCycle implements Runnable, NeedsControlled{
	private float time;
	private float stepSize;
	
	/**
	 * Constructor
	 * @param stepSize Indicates how far the simulation should "jump" at each tick.
	 * @param tickLength Sleep duration for the Thread
	 */
	public DayNightCycle(float stepSize) {
		this.stepSize = stepSize;
		NeedsController.registerNeed("SunLight", this);
		Globals.registerSetting("Sleep", "Day/Night", 1, 1000, 500);
	}

	/**
	 * Starter for the DayNightCycle
	 */
	public void run() {
		while(true){
			time = (time+stepSize)%24;
			try{
				Thread.sleep((long)(Globals.getSetting("Sleep", "Day/Night")));
			}
			catch(InterruptedException e){
				System.out.println("Could not sleep DayNightCycle, stopping thread\n");
				e.printStackTrace();
				break;
			}
		}
	}

	/**
	 * Gives current time.
	 * @return The time in 24h format, so return value varies from [0.0f, 24.0f)
	 */
	public float getTime(){
		return time;
	}

	public float getNeed(Needs need, int x, int y) {
		return ((time+12.0f)%24.0f)/24.0f;//TODO Fix this. it is abit off. Needs to be an exponential curve, peeking at 1.0 in time:12.0f, and 0.0 at the edges
	}
}
