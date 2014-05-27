package tests;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.Test;

import utilities.Astar;
import utilities.Globals;
import utilities.NeedsController;

public class AstarTest {
//	public static int width = 6;
// 	public static int height = 6;
	final static float WALL = 999.9f;
 
// 	static float[][] world ={ {WALL,	WALL,	WALL,	WALL, 	WALL, 	WALL},
// 						  	  {WALL,	0.0f,  	0.0f,	WALL,	0.0f, 	WALL}, 
// 						  	  {WALL,	0.0f,  	WALL,	WALL,	0.0f, 	WALL},
// 						  	  {WALL,	0.0f,  	0.0f,  	WALL, 	0.0f, 	WALL},
// 						  	  {WALL,	0.0f,  	0.0f,  	0.0f, 	0.0f, 	WALL},
// 						  	  {WALL,	WALL,	WALL,	WALL, 	WALL, 	WALL} };

	
	@Test
	public void testCalculatePath() {
//		Globals.heightmap = new float[Globals.width][Globals.height];
//		Globals.heightmap[0][0] = WALL;
//		Globals.heightmap[0][1] = WALL;
//		Globals.heightmap[0][2] = WALL;
//		Globals.heightmap[0][3] = WALL;
//		Globals.heightmap[0][4] = WALL;
//		Globals.heightmap[0][5] = WALL;
//		
//		Globals.heightmap[1][0] = WALL;
//		Globals.heightmap[2][0] = WALL;
//		Globals.heightmap[3][0] = WALL;
//		Globals.heightmap[4][0] = WALL;
//		Globals.heightmap[5][0] = WALL;
//		
//		Globals.heightmap[1][5] = WALL;
//		Globals.heightmap[2][5] = WALL;
//		Globals.heightmap[3][5] = WALL;
//		Globals.heightmap[4][5] = WALL;
//		Globals.heightmap[5][5] = WALL;
//		
//		Globals.heightmap[5][1] = WALL;
//		Globals.heightmap[5][2] = WALL;
//		Globals.heightmap[5][3] = WALL;
//		Globals.heightmap[5][4] = WALL;
//		
//		Globals.heightmap[2][2] = WALL;
//		Globals.heightmap[3][1] = WALL;
//		Globals.heightmap[3][2] = WALL;
//		Globals.heightmap[3][3] = WALL;
		
		Deque<int[]> result = new ArrayDeque<>();
	//	result = Astar.calculatePath(1, 1, 1, 4);

		//assertTrue(result.getFirst() ==);
	//	System.out.println(result.getFirst());
	}

}
