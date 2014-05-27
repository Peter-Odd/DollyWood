package tests;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.Test;

import simulation.Animal;
import simulation.Race;
import utilities.Astar;
import utilities.Globals;
import utilities.NeedsController;

public class AstarTest {
	public int width = 6;
 	public int height = 6;
	final float WALL = 999.9f;
	final float TREE = -1.0f;
 


	@Test
	public void testCalculatePath() {
	 	float[][] world ={ {WALL,	WALL,	WALL,	WALL, 	WALL, 	WALL},
			  	   {WALL,	0.0f,  	0.0f,	TREE,	0.0f, 	WALL}, 
			  	   {WALL,	0.0f,  	WALL,	WALL,	0.0f, 	WALL},
				   {WALL,	0.0f,  	0.0f,  	WALL, 	0.0f, 	WALL},
				   {WALL,	0.0f,  	0.0f,  	0.0f, 	0.0f, 	WALL},
				   {WALL,	WALL,	WALL,	WALL, 	WALL, 	WALL} };
		Globals.height = this.height;
		Globals.width = this.width;
		Globals.heightmap = world;
		
		Race tree = new Race("Tree");
		NeedsController.registerNeed("Tree", tree);
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				if(Globals.heightmap[x][y] == TREE){
					Globals.heightmap[x][y] = 0.0f;
					tree.setSpeciesAt(x, y, new Animal());
				}
		
		Deque<int[]> result = new ArrayDeque<>();
		result = Astar.calculatePath(1, 1, 1, 4);
		int desiredSize = 9;
		assertTrue(result.size() == desiredSize);
		Deque<int[]> desired = new ArrayDeque<>();
		desired.add(new int[]{1,1});
		desired.add(new int[]{2,1});
		desired.add(new int[]{3,1});
		desired.add(new int[]{4,2});
		desired.add(new int[]{4,3});
		desired.add(new int[]{4,4});
		desired.add(new int[]{3,4});
		desired.add(new int[]{2,4});
		desired.add(new int[]{1,4});
		for(int i = 0; i < desiredSize; i++){
			int[] desiredPos = desired.pop();
			int[] resultPos = result.pop();
			//System.out.println(resultPos[0] + ":" + resultPos[1]);
			assertArrayEquals(desiredPos, resultPos);
		}
	}
	
	@Test
	public void testBlockedPath() {
	 	float[][] world ={ {WALL,	WALL,	WALL,	WALL, 	WALL, 	WALL},
			  	   {WALL,	0.0f,  	0.0f,	WALL,	0.0f, 	WALL}, 
			  	   {WALL,	0.0f,  	WALL,	WALL,	0.0f, 	WALL},
				   {WALL,	0.0f,  	0.0f,  	TREE, 	0.0f, 	WALL},
				   {WALL,	0.0f,  	0.0f,  	WALL, 	0.0f, 	WALL},
				   {WALL,	WALL,	WALL,	WALL, 	WALL, 	WALL} };
		Globals.height = this.height;
		Globals.width = this.width;
		Globals.heightmap = world;
		
		Race tree = new Race("Tree");
		NeedsController.registerNeed("Tree", tree);
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				if(Globals.heightmap[x][y] == TREE){
					Globals.heightmap[x][y] = 0.0f;
					tree.setSpeciesAt(x, y, new Animal());
				}
		
		Deque<int[]> result = new ArrayDeque<>();
		result = Astar.calculatePath(1, 1, 1, 4);
		assertTrue(result.size() == 4);
		Deque<int[]> desired = new ArrayDeque<>();
		desired.add(new int[]{1,1});
		desired.add(new int[]{1,2});
		desired.add(new int[]{1,3});
		desired.add(new int[]{1,4});
		for(int i = 0; i < 4; i++){
			int[] desiredPos = desired.pop();
			int[] resultPos = result.pop();
			assertArrayEquals(desiredPos, resultPos);
		}
	}

}
