package simulation;

import utilities.*;
import graphics.*;
public class Main {

	public static void main(String[] args) {
		new Main();
	}
	
	public Main() {
		//Globals.height = 65;
		//Globals.width = 65;
		//Globals.heightmap = new float[Globals.width][Globals.height];
		//Globals.heightmap = Fractal.generateFractal(Globals.heightmap, 0.0f, 255.0f);
		
		
		
		
		// Used to test Fractal.java
		 
		Globals.height = 33;
		Globals.width = 33;
		Globals.heightmap = new float[Globals.width][Globals.height];
	
		//Globals.heightmap = Fractal.intitateCorners(Globals.heightmap, 50.0f, 255.0f);
		//Globals.heightmap = Fractal.diamondStep(Globals.heightmap, Globals.width-1, 0, Globals.height-1, 0);
		//Globals.heightmap = Fractal.squareStep(Globals.heightmap, Globals.width-1, 0, Globals.height-1, 0);
	
		
		Globals.heightmap = Fractal.generateFractal(Globals.heightmap, 255.0f, 0.0f);
		new Graphics2D();
	}

}
