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
		new Graphics2D();
	}

}
