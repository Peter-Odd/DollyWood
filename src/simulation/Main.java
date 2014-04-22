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
		 
		Globals.height = 5;
		Globals.width = 5;
		Globals.heightmap = new float[Globals.width][Globals.height];
		
		Globals.heightmap = Fractal.intitateCorners(Globals.heightmap, 0.0f, 255.0f);
		Globals.heightmap = Fractal.diamondStep(Globals.heightmap, 4, 0, 4, 0);
		Globals.heightmap = Fractal.squareStep(Globals.heightmap, 4, 0, 4, 0);
		Fractal.print(Globals.heightmap);
		new Graphics2D();
	}

}
