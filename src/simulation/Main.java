package simulation;



import java.util.Random;

import utilities.*;
import graphics.*;
public class Main {

	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		Globals.heightmap = Fractal.generateFractal(Globals.heightmap, Globals.worldFractalMax, Globals.worldFractalMin, Globals.worldFractalRange, Globals.worldFractalDiv);

		Random rng = new Random();
		Race sheepRace = new Race("Sheep");
		Globals.races.add(sheepRace);
		for(int i = 0; i < Globals.startingSheep; i++){
			Sheep sheep = new Sheep(rng.nextInt(Globals.width), rng.nextInt(Globals.height), sheepRace);
			sheepRace.setSpeciesAt(sheep.xPos, sheep.yPos, sheep);
			Thread sheepThread = new Thread(sheep);
			sheepThread.start();
		}
		
		Globals.water = new Water();
		Thread waterThread = new Thread(Globals.water);
		waterThread.start();
		Grass grass = new Grass();
		Thread grassThread = new Thread(grass);
		Globals.races.add(grass);
		grassThread.start();
		Globals.dayNightCycle = new DayNightCycle(0.1f, Globals.dayNightSleepLength);
		Thread dayNightThread = new Thread(Globals.dayNightCycle);
		dayNightThread.start();


		/* Test environment for tree */
		float[][] treeArray = new float[Globals.height][Globals.width];
		Random random;
		random = new Random();

		for (int x = 0; x < Globals.height; x++) {
			for (int y = 0; y < Globals.width; y++) {
				treeArray[x][y] = random.nextFloat();
			}
		}

		Race treeRace = new Race("Tree");
		Globals.races.add(treeRace);
		
		for (int x = 0; x < Globals.height; x++) {
			for  (int y = 0; y < Globals.width; y++) {
				if (treeArray[x][y] > 0.98) {
					Tree tree = new Tree(400, x, y, treeRace);	
					treeRace.setSpeciesAt(x, y, tree);
					Thread treeThread = new Thread(tree);
					treeThread.start();
				}
			}
		}
		
		
		new Graphics3D();
	}


}

