package simulation;

import utilities.*;
import graphics.*;
public class Main {

	public static void main(String[] args) {
		new Main();
	}
	
	public Main() {
		Globals.height = 65;
		Globals.width = 65;
		Globals.heightmap = new float[Globals.width][Globals.height];
		Globals.heightmap = Fractal.generateFractal(Globals.heightmap, 0.0f, 255.0f);
		new Graphics2D();
	}

}
