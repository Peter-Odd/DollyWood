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
		ArrayList<Thread> threadsToStart = new ArrayList<>();
		
		Globals.water = new Water();
		Thread waterThread = new Thread(Globals.water);
		threadsToStart.add(waterThread);
		Globals.dayNightCycle = new DayNightCycle(0.1f);
		Thread dayNightThread = new Thread(Globals.dayNightCycle);
		threadsToStart.add(dayNightThread);
		
		Random rng = new Random();
		Race sheepRace = new Race("Sheep");
		Globals.races.add(sheepRace);
		for(int i = 0; i < Globals.startingSheep; i++){
			Sheep sheep = new Sheep(rng.nextInt(Globals.width), rng.nextInt(Globals.height), sheepRace);
			sheepRace.setSpeciesAt(sheep.xPos, sheep.yPos, sheep);
			Thread sheepThread = new Thread(sheep);
			threadsToStart.add(sheepThread);
		}

		Race wolfRace = new Race("Wolf");
		Globals.races.add(wolfRace);
		for(int i = 0; i < Globals.startingWolves; i++){
			Wolf wolf = new Wolf(rng.nextInt(Globals.width), rng.nextInt(Globals.height), wolfRace);
			wolfRace.setSpeciesAt(wolf.xPos, wolf.yPos, wolf);
			Thread wolfThread = new Thread(wolf);
			threadsToStart.add(wolfThread);
		}

		
		Race treeRace = new Race("Tree");
		Globals.races.add(treeRace);
		for(int i = 0; i < Globals.startingTrees; i++){
			int x = rng.nextInt(Globals.width);
			int y = rng.nextInt(Globals.height);
			Tree tree = new Tree(Globals.treeSleepLength, x, y, treeRace);	
			treeRace.setSpeciesAt(x, y, tree);
			Thread treeThread = new Thread(tree);
			threadsToStart.add(treeThread);
		}

		Grass grass = new Grass();
		Thread grassThread = new Thread(grass);
		Globals.races.add(grass);
		threadsToStart.add(grassThread);

		Globals.registerSetting("Field of view", "Graphics", 10, 90, 70);
		Globals.registerSetting("Render distance", "Graphics", 1, 50, 15);
		Globals.createSettingsFrame(true, true, false);

		Globals.heightmap = new float[Globals.width][Globals.height];
		Globals.heightmap = Fractal.generateFractal(Globals.heightmap, Globals.worldFractalMax, Globals.worldFractalMin, Globals.worldFractalRange, Globals.worldFractalDiv);
		for(Thread t : threadsToStart)
			t.start();
		new Graphics3D();
	}

}
