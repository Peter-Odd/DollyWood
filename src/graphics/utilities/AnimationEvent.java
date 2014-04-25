package graphics.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents and entire animation with multiple keyframes.
 * @see AnimationState
 * @author OSM Group 5 - DollyWood project
 * @version 1.01
 */
public class AnimationEvent 
{
	public static final int ANIMATION_STATE_LINEAR = 0;
	public static final int ANIMATION_STATE_SMOOTH = 1;
	public String model;
	public float currentProgress;
	public ArrayList<AnimationState> states = new ArrayList<AnimationState>();
	public AnimationState currentState;
	public int loopType;
	
	/**
	 * Loads a non-binary .ani file and returns the AnimationEvent within.
	 * <br>
	 * A .ani file consists primarily of 5 fields. and looks like the following<br />
	 * o string with model name<br />
	 * p x.x y.y z.z //Position vector<br />
	 * r x.x y.y z.z //Rotation vector<br />
	 * sc x.x y.y z.z //Scale vector<br />
	 * s x.x //Speed of the keyframe.<br />
	 * 
	 * These field should appear in this order, and can be repeated for each keyframe.<br />
	 * You can then specify if the animation should loop with the string:<br />
	 * lt LOOP
	 * @param filename Location of the .ani file
	 * @param position Position offset. This is where the animation will be centered
	 * @param rotation Rotation offset.
	 * @param scale Scale offset. Note that they should be 0.0, if a final scale of 1.0 is desired
	 * @return A new AnimationEvent loaded from a .ani file
	 * @throws FileNotFoundException
	 */
	public static AnimationEvent loadEvent(String filename, Vector3f position, Vector3f rotation, Vector3f scale) throws FileNotFoundException{
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String line = null;
		AnimationEvent event = new AnimationEvent();
		AnimationState state = null;
		try {
			while((line = in.readLine()) != null){
				if(line.startsWith("o ")){
					state = new AnimationState();
					state.model = line.split(" ")[1];
					state.position = new Vector3f();
					state.rotation = new Vector3f();
					state.scale = new Vector3f();
					event.states.add(state);
				}
				else if(line.startsWith("p ")){
					String[] components = line.split(" ");
					Vector3f.add(position, parseVector(components, 1), state.position);
				}
				else if(line.startsWith("r ")){
					String[] components = line.split(" ");
					Vector3f.add(rotation, parseVector(components, 1), state.rotation);
				}
				else if(line.startsWith("sc ")){
					String[] components = line.split(" ");
					Vector3f.add(scale, parseVector(components, 1), state.scale);
				}
				else if(line.startsWith("s ")){
					String[] components = line.split(" ");
					state.speed = Float.parseFloat(components[1]);
				}
				else if(line.startsWith("lt ")){
					String[] components = line.split(" ");
					if(components[1].equals("LOOP"))
						event.loopType = 1;
				}
			}
			in.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		event.currentState = event.states.get(0).clone();
		event.model = event.currentState.model;
		return event;
	}
	
	private static Vector3f parseVector(String[] components, int offset){
		float x = Float.parseFloat(components[0+offset]);
		float y = Float.parseFloat(components[1+offset]);
		float z = Float.parseFloat(components[2+offset]);
		return new Vector3f(x,y,z);
	}

	/**
	 * sets the progress to 0.0 and the currentState to the first state
	 */
	public void reset() {
		currentProgress = 0.0f;
		currentState = states.get(0).clone();
	}
}