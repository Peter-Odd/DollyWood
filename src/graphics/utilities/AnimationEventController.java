package graphics.utilities;

import java.util.ArrayList;

public class AnimationEventController{
	
	private int tickLength;
	public ArrayList<AnimationEvent> events = new ArrayList<>();
	
	public AnimationEventController(int tickLength)
	{
		this.tickLength = tickLength;
	}
	
	public void step(){
		for(AnimationEvent e : events){
			if((int)e.currentProgress+1 == e.states.size())
				events.remove(e);
			else{
				e.currentState.position.x = e.states.get((int)e.currentProgress).position.x + (e.states.get((int)e.currentProgress).position.x-e.states.get((int)e.currentProgress+1).position.x)*(e.currentProgress-(int)e.currentProgress);
				e.currentState.position.y = e.states.get((int)e.currentProgress).position.y + (e.states.get((int)e.currentProgress).position.y-e.states.get((int)e.currentProgress+1).position.y)*(e.currentProgress-(int)e.currentProgress);
				e.currentState.position.z = e.states.get((int)e.currentProgress).position.z + (e.states.get((int)e.currentProgress+1).position.z-e.states.get((int)e.currentProgress).position.z)*(e.currentProgress-(int)e.currentProgress);
				e.currentProgress += e.states.get((int)e.currentProgress).speed;
			}
		}
	}
	
	public void run() {
		while(events.size() > 0){
			//step();
			try {
				Thread.sleep(tickLength);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
