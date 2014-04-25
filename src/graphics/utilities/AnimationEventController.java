package graphics.utilities;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

public class AnimationEventController{
	
	private int tickLength;
	public ArrayList<AnimationEvent> events = new ArrayList<>();
	
	public AnimationEventController(int tickLength)
	{
		this.tickLength = tickLength;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized ArrayList<AnimationEvent> getEvents(){
		return (ArrayList<AnimationEvent>) events.clone();
	}
	
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
