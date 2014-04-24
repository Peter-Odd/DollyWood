package graphics.utilities;

import org.lwjgl.util.vector.Vector3f;

public class AnimationState{
	public Vector3f position;
	public Vector3f rotation;
	public Vector3f scale;
	public int stateType;
	public float speed;
	
	public AnimationState clone(){
		AnimationState clone = new AnimationState();
		clone.position = new Vector3f(position.x, position.y, position.z);
		clone.rotation = new Vector3f(rotation.x, rotation.y, rotation.z);
		clone.scale = new Vector3f(scale.x, scale.y, scale.z);
		clone.stateType = stateType;
		clone.speed = speed;
		return clone;
	}
}