package simulation;

import java.util.Random;

public abstract class Animal {
	private Random random;
	private float rotation;
	public Animal(){
		random = new Random();
		rotation = random.nextInt(360);
	}
	public float getRotation() {
		return rotation;
	}
}