package graphics.utilities;
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
	public static final int ANIMATION_STATE_LINEAR = 0;//TBI
	public static final int ANIMATION_STATE_SMOOTH = 1;//TBI
	public String animationID;
	public float currentModelProgress;
	public float currentAnimationProgress;
	public ArrayList<AnimationState> modelStates = new ArrayList<AnimationState>();
	public ArrayList<AnimationState> animationStates = new ArrayList<AnimationState>();
	public ArrayList<AnimationState> cameraStates = new ArrayList<AnimationState>();
	public AnimationState currentModelState;
	public AnimationState currentAnimationState;
	public AnimationState currentCameraState;
	public int loopType;

	public AnimationState getStateSum(){
		String model = currentModelState.model;
		Vector3f position = new Vector3f();
		Vector3f rotation = new Vector3f();
		Vector3f scale = new Vector3f();
		Vector3f.add(currentModelState.position, currentAnimationState.position, position);
		Vector3f.add(currentModelState.rotation, currentAnimationState.rotation, rotation);
		Vector3f.add(currentModelState.scale, currentAnimationState.scale, scale);
		return new AnimationState(model, position, rotation, scale);
	}
	
	public void resetModelState() {
		currentModelProgress = 0.0f;
		currentModelState = modelStates.get(0).clone();
	}
	
	public void resetAnimationState() {
		currentAnimationProgress = 0.0f;
		currentAnimationState = animationStates.get(0).clone();
	}

	public int totalStates() {
		return modelStates.size() + animationStates.size() + cameraStates.size();
	}
}