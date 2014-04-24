package graphics.utilities;

import java.util.ArrayList;

public class AnimationEvent 
{
	public static final int ANIMATION_STATE_LINEAR = 0;
	public static final int ANIMATION_STATE_SMOOTH = 1;
	public String model;
	public float currentProgress;
	public ArrayList<AnimationState> states = new ArrayList<AnimationState>();
	public AnimationState currentState;
}
