package graphics.utilities;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

/**
 * AnimationEventController is a holder and executor of events.
 * It can hold multiple animations(or events) each with multiple keyframes(or states).
 * The controller can be run on it's own thread or step by step using step function.
 * <br />
 * It calculates the current state of the animation based on 2 states and a progress float.
 * @see AnimationEvent
 * @see AnimationState
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */
public class AnimationEventController implements Runnable{
	
	private int tickLength;
	public ArrayList<AnimationEvent> events = new ArrayList<>();
	
	/**
	 * Sets for how long the thread should sleep between working. Only relevant if used with a thread.
	 * @param tickLength Time in ms
	 */
	public AnimationEventController(int tickLength)
	{
		this.tickLength = tickLength;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * returns all animationEvents
	 * @return
	 */
	public synchronized ArrayList<AnimationEvent> getEvents(){
		return (ArrayList<AnimationEvent>) events.clone();
	}
	
	/**
	 * Calculates the current state of the animation based on 2 states and a progress float, for each of the events.
	 * if one event completes, it stops prematurely.
	 */
	public synchronized void step(){
		for(AnimationEvent e : events){
			if((int)e.currentProgress+1 == e.states.size()){
				events.remove(e);
				break;
			}
			else{
				e.currentState.position = getNewProgressVector(e.states.get((int)e.currentProgress).position, e.states.get((int)e.currentProgress+1).position, e.currentProgress-(int)e.currentProgress);
				e.currentState.scale = getNewProgressVector(e.states.get((int)e.currentProgress).scale, e.states.get((int)e.currentProgress+1).scale, e.currentProgress-(int)e.currentProgress);
				e.currentState.rotation = getNewProgressVector(e.states.get((int)e.currentProgress).rotation, e.states.get((int)e.currentProgress+1).rotation, e.currentProgress-(int)e.currentProgress);
				e.currentProgress += e.states.get((int)e.currentProgress).speed;
			}
		}
	}
	
	private Vector3f getNewProgressVector(Vector3f preVal, Vector3f postVal, float progress){
		float x = preVal.x + (postVal.x-preVal.x)*(progress);
		float y = preVal.y + (postVal.y-preVal.y)*(progress);
		float z = preVal.z + (postVal.z-preVal.z)*(progress);
		return new Vector3f(x,y,z);
	}
	
	/**
	 * Starter for thread.
	 * it stops when there are no more events to animate.
	 */
	public void run() {
		while(events.size() > 0){
			step();
			try {
				Thread.sleep(tickLength);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
