package utilities;

import java.util.Stack;

public class AstarDriver {

	public static int width = 5;
	public static int height = 6;
	final static float WALL = 999.9f;

	/*	static float[][] world ={ {0.0f,WALL,0.0f,0.0f},
						      {0.0f,WALL,0.0f,0.0f}, 
						      {0.0f,WALL,0.0f,0.0f},
						      {0.0f,0.0f,  0.0f,0.0f} }; */

	static float[][] world ={ {WALL,	WALL,	WALL,	WALL, 	WALL	, WALL},
						  	  {WALL,	0.0f,  	0.0f,	WALL,	0.0f	, WALL}, 
						  	  {WALL,	0.0f,  	0.0f,	WALL,	0.0f	, WALL},
						  	  {WALL,	0.0f,  	0.0f,  	0.0f, 	0.0f	, WALL},
						  	  {WALL,	WALL,	WALL,	WALL, 	WALL	, WALL} };
	 
	/*
	static float[][] world ={ {10.0f,	0.0f,	0.0f,	0.0f, 	0.0f, 	0.0f},
							  {20.0f,	0.0f,  	0.0f,	0.0f,	0.0f, 	0.0f}, 
							  {30.0f,	0.0f,  	0.0f,	021.0f,	0.0f, 	04.0f},
							  {40.0f,	0.0f,  	0.0f,  	0.0f, 	0.0f, 	0.0f},
							  {50.0f,	0.0f,	0.0f,	0.0f, 	0.0f, 	0.0f} };
	*/

	public static void main(String[] args) {

		//System.out.println(world.length);
		//System.out.println(world[0].length);

		/*	for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				System.out.println(world[i][j]);
			}
		}*/
//		System.out.println("----");
//		for (int y = 0; y < world.length; y++) {
//			for (int x = 0; x < world[0].length; x++) {
//				System.out.println("(" + x + "," + y + "): " + world[y][x]);
//			}
//		}


		Stack<Node> path = Astar.calculatePath(1, 1, 1, 4);

//		System.out.println("Path size: " + path.size());

//		for(Node n : path) {
//
//			System.out.println("X: " + n.getX() + ", Y: " + n.getY());
//		}



	}

}
