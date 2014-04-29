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
		
		Globals.screenHeight = 1080;
		Globals.screenWidth = 1920;
		
		
		// Used to test Fractal.java

		Globals.height = 33;
		Globals.width = 33;
		Globals.heightmap = new float[Globals.width][Globals.height];
	
		//Globals.heightmap = Fractal.intitateCorners(Globals.heightmap, 50.0f, 255.0f);
		//Globals.heightmap = Fractal.diamondStep(Globals.heightmap, Globals.width-1, 0, Globals.height-1, 0);
		//Globals.heightmap = Fractal.squareStep(Globals.heightmap, Globals.width-1, 0, Globals.height-1, 0);
	

		
		Globals.heightmap = Fractal.generateFractal(Globals.heightmap, 200.0f, 0.0f, 25.0f, 3.0f);
		new Graphics2D();
		/*
		Race sheep = new Race("Sheep");
		Globals.races.add(sheep);
		for(int[] position : HexagonUtils.neighborTiles(6,6, false)){
			sheep.setSpeciesAt(position[0], position[1], new Sheep());
		}
		Globals.water = new Water(100);
		Thread waterThread = new Thread(Globals.water);
		waterThread.start();
		Grass grass = new Grass(100);
		Thread grassThread = new Thread(grass);
		Globals.races.add(grass);
		grassThread.start();
		Globals.dayNightCycle = new DayNightCycle(0.1f, 1000);
		Thread dayNightThread = new Thread(Globals.dayNightCycle);
		dayNightThread.start();
		new Graphics3D();*/
	}

}
