package simulation;



import java.util.ArrayList;
import java.util.Random;

import utilities.*;
import graphics.*;
public class Main {

	public static void main(String[] args) {
		new Main();
	}

	public Main() {

		Globals.heightmap = new float[Globals.width][Globals.height];
		Globals.heightmap = Fractal.generateFractal(Globals.heightmap, Globals.worldFractalMax, Globals.worldFractalMin, Globals.worldFractalRange, Globals.worldFractalDiv);




		Globals.heightmap = Fractal.generateFractal(Globals.heightmap, 200.0f, 0.0f, 20.0f, 2.0f);


		ArrayList<Thread> threadsToStart = new ArrayList<>();

		
		/* Register settings below */
		Globals.registerSetting("Number of trees", "Tree", 0, 1000, 40);
		Globals.registerSetting("Tree sleep (ms)", "Tree", 0, 1500, 400);
		Globals.registerSetting("Tree seed amount", "Tree", 0.0f, 1.0f, 0.3f);
		Globals.registerSetting("Tree time of life (sec)", "Tree", 0, 500, 200);
		
		Globals.registerSetting("Number of sheep", "Sheep", 2, 40, 5);
		Globals.registerSetting("Sheep sleep", "Sheep", 0, 1500, 500);
		Globals.registerSetting("Sheep thirst", "Sheep", 0.1f, 1, 0.5f);
		Globals.registerSetting("Sheep hunger", "Sheep", 0.1f, 1, 0.5f);
		
		Globals.registerSetting("Number of wolves", "Wolf", 2, 40, 5);
		Globals.registerSetting("Wolves sleep", "Wolf", 0, 1500, 500);
		Globals.registerSetting("Wolves thirst", "Wolf", 0.1f, 1, 0.5f);
		Globals.registerSetting("Wolves hunger", "Wolf", 0.1f, 1, 1.0f);

		

		Globals.water = new Water();
		Thread waterThread = new Thread(Globals.water);
		threadsToStart.add(waterThread);
		Globals.dayNightCycle = new DayNightCycle(0.1f);
		Thread dayNightThread = new Thread(Globals.dayNightCycle);
		threadsToStart.add(dayNightThread);
		

		Grass grass = new Grass();
		Thread grassThread = new Thread(grass);
		Globals.races.add(grass);
		threadsToStart.add(grassThread);

		Globals.registerSetting("Field of view", "Graphics", 10, 90, 70);
		Globals.registerSetting("Render distance", "Graphics", 1, 50, 25);
		Globals.createSettingsFrame(true, true, false);

		Random rng = new Random();
		
		Race sheepRace = new Race("Sheep");
		Globals.races.add(sheepRace);
		for(int i = 0; i < (int)Globals.getSetting("Number of sheep", "Sheep"); i++){
			Sheep sheep = new Sheep(rng.nextInt(Globals.width), rng.nextInt(Globals.height), sheepRace);
			sheepRace.setSpeciesAt(sheep.xPos, sheep.yPos, sheep);
			Thread sheepThread = new Thread(sheep);
			threadsToStart.add(sheepThread);
		}
		
		/*Sheep sheep = new Sheep(15, 18, sheepRace, true);
		sheepRace.setSpeciesAt(15, 18, sheep);
		Thread sheepThread = new Thread(sheep);
		threadsToStart.add(sheepThread);
		
		Sheep sheep2 = new Sheep(20, 20, sheepRace, false);
		sheepRace.setSpeciesAt(20, 20, sheep2);
		Thread sheepThread2 = new Thread(sheep2);
		threadsToStart.add(sheepThread2);
		*/
		
		/*Race wolfRace = new Race("Wolf");
		Globals.races.add(wolfRace);
		for(int i = 0; i < (int)Globals.getSetting("Number of wolves", "Wolf"); i++){
			Wolf wolf = new Wolf(rng.nextInt(Globals.width), rng.nextInt(Globals.height), wolfRace);
			wolfRace.setSpeciesAt(wolf.xPos, wolf.yPos, wolf);
			Thread wolfThread = new Thread(wolf);
			threadsToStart.add(wolfThread);
		}
		*/
		Race treeRace = new Race("Tree");
		Globals.races.add(treeRace);
		for(int i = 0; i < Globals.getSetting("Number of trees", "Tree"); i++){
			int x = rng.nextInt(Globals.width);
			int y = rng.nextInt(Globals.height);
			Tree tree = new Tree(x, y, treeRace);	
			treeRace.setSpeciesAt(x, y, tree);
			Thread treeThread = new Thread(tree);
			threadsToStart.add(treeThread);
		}
		

		
		Globals.heightmap = new float[Globals.width][Globals.height];
		Globals.heightmap = Fractal.generateFractal(Globals.heightmap, Globals.worldFractalMax, Globals.worldFractalMin, Globals.worldFractalRange, Globals.worldFractalDiv);
		for(Thread t : threadsToStart)
			t.start();
		new Graphics3D();
	}

}
