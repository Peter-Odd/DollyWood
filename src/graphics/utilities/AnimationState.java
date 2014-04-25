package graphics.utilities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a keyframe.
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */
public class AnimationState{
	public String model;
	public Vector3f position;
	public Vector3f rotation;
	public Vector3f scale;
	public int stateType;
	public float speed;
	
	/**
	 * Returns a copy of the state
	 */
	public AnimationState clone(){
		AnimationState clone = new AnimationState();
		clone.model = String.valueOf(model);
		clone.position = new Vector3f(position.x, position.y, position.z);
		clone.rotation = new Vector3f(rotation.x, rotation.y, rotation.z);
		clone.scale = new Vector3f(scale.x, scale.y, scale.z);
		clone.stateType = stateType;
		clone.speed = speed;
		return clone;
	}
}