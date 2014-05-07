package simulation;



import java.util.Random;

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




		Globals.heightmap = Fractal.generateFractal(Globals.heightmap, 200.0f, 0.0f, 25.0f, 3.0f);


		Race sheepRace = new Race("Sheep");
		Globals.races.add(sheepRace);
		Sheep sheep = new Sheep(15, 15, sheepRace, 0.5f);
		sheepRace.setSpeciesAt(15, 15, sheep);
		Thread sheepThread = new Thread(sheep);
		sheepThread.start();
		
		Sheep sheep1 = new Sheep(16, 20, sheepRace, 0.5f);
		sheepRace.setSpeciesAt(16, 20, sheep1);
		Thread sheepThread1 = new Thread(sheep1);
		sheepThread1.start();
		
		Sheep sheep2 = new Sheep(17, 10, sheepRace, 0.5f);
		sheepRace.setSpeciesAt(17, 10, sheep2);
		Thread sheepThread2 = new Thread(sheep2);
		sheepThread2.start();
		//for(int[] position : HexagonUtils.neighborTiles(6,6, false)){
		//	sheep.setSpeciesAt(position[0], position[1], new Sheep());
		//}

	
		/* Test environment for tree */
		/*Race treeRace = new Race("Tree");
		Globals.races.add(treeRace);
		Tree tree = new Tree(10, 10, treeRace);
		treeRace.setSpeciesAt(10, 10, tree);
		Thread treeThread = new Thread(tree);
		treeThread.start();*/

		float[][] treeArray = new float[Globals.height][Globals.width];
		Random random;
		random = new Random();

		for (int x = 0; x < Globals.height; x++) {
			for (int y = 0; y < Globals.width; y++) {
				treeArray[x][y] = random.nextFloat();
			}
		}
		
		Tree tree = new Tree(1000);	
		Race treeRace = new Race("Tree");

		for (int x = 0; x < Globals.height; x++) {
			for  (int y = 0; y < Globals.width; y++) {

				if (treeArray[x][y] > 0.99) {
					//if (treeRace.getSpeciesAt(x, y) == null) { //if water

					Globals.races.add(treeRace);
					treeRace.setSpeciesAt(x, y, tree);
					Thread treeThread = new Thread(tree);
					treeThread.start();
					//}
				}



			}
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

		new Graphics3D();
	}


}

