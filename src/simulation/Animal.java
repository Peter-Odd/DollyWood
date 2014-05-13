package simulation;

import java.util.Random;

public class Animal{
	private Random random;
	private float rotation;
	private boolean gender; // true = female, false = male
	private float age; // 1.0f = adult;
	boolean pregnant;

		
	public Animal(){
		random = new Random();
		rotation = random.nextInt(360);
		age = 0.3f;
		this.pregnant = false;
		if(random.nextInt(100) >= 50){
			this.gender = true;
		} else {
			this.gender = false;
		}
	}
	
	public Animal(boolean gender){
		random = new Random();
		rotation = random.nextInt(360);
		age = 0.3f;
		this.pregnant = false;
		this.gender = gender;
	}
	
	public boolean getGender(){
		return gender;
	}
	
	public float getAge(){
		return age;
	}
	
	public float getRotation() {
		return rotation;
	}
	

	public float getSize(){
		return 1.0f;
	}
	
	public void setPregnant(boolean bool){
		this.pregnant = bool;
	}
	
	public boolean getPregnant(){
		return pregnant;
	}
	
	public void setRotation(float rotation){
		this.rotation = rotation;
	}
	
}
