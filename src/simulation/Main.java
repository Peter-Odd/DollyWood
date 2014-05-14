package simulation;



import utilities.*;
import graphics.*;
public class Main {

	public static void main(String[] args) {
		new Main();
	}

	public Main() {

		Globals.screenHeight = 1080;
		Globals.screenWidth = 1920;


		// Used to test Fractal.java

		Globals.height = 33;
		Globals.width = 33;
		Globals.heightmap = new float[Globals.width][Globals.height];




		Globals.heightmap = Fractal.generateFractal(Globals.heightmap, 200.0f, 0.0f, 20.0f, 2.0f);


		Race sheepRace = new Race("Sheep");
		Globals.races.add(sheepRace);
		Sheep sheep = new Sheep(15, 15, sheepRace, true);
		sheepRace.setSpeciesAt(15, 15, sheep);
		Thread sheepThread = new Thread(sheep);
		sheepThread.start();
		
		
		Sheep sheep1 = new Sheep(17, 15, sheepRace, false);
		sheepRace.setSpeciesAt(17, 15, sheep1);
		Thread sheepThread1 = new Thread(sheep1);
		sheepThread1.start();
		
		/*
		Sheep sheep2 = new Sheep(17, 10, sheepRace);
		sheepRace.setSpeciesAt(17, 10, sheep2);
		Thread sheepThread2 = new Thread(sheep2);
		sheepThread2.start();
		
		Sheep sheep3 = new Sheep(20, 10, sheepRace);
		sheepRace.setSpeciesAt(20, 10, sheep3);
		Thread sheepThread3 = new Thread(sheep3);
		sheepThread3.start();
		
		Sheep sheep4 = new Sheep(18, 7, sheepRace);
		sheepRace.setSpeciesAt(18, 7, sheep4);
		Thread sheepThread4 = new Thread(sheep4);
		sheepThread4.start();
		
		Sheep sheep5 = new Sheep(17, 23, sheepRace);
		sheepRace.setSpeciesAt(17, 23, sheep5);
		Thread sheepThread5 = new Thread(sheep5);
		sheepThread5.start();*/
		
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

		new Graphics3D();
	}


}

