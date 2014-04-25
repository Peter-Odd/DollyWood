package graphics.utilities;

import java.util.ArrayList;

/**
 * Represents and entire animation with multiple keyframes.
 * @see AnimationState
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */
public class AnimationEvent 
{
	public static final int ANIMATION_STATE_LINEAR = 0;
	public static final int ANIMATION_STATE_SMOOTH = 1;
	public String model;
	public float currentProgress;
	public ArrayList<AnimationState> states = new ArrayList<AnimationState>();
	public AnimationState currentState;
}
