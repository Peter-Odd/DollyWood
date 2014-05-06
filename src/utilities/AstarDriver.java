package utilities;

import java.util.Deque;

public class AstarDriver {

	public static int width = 6;
	public static int height = 6;
	final static float WALL = 999.9f;

	static float[][] world ={ {WALL,	WALL,	WALL,	WALL, 	WALL, 	WALL},
						  	  {WALL,	0.0f,  	0.0f,	WALL,	0.0f, 	WALL}, 
						  	  {WALL,	0.0f,  	WALL,	WALL,	0.0f, 	WALL},
						  	  {WALL,	0.0f,  	0.0f,  	WALL, 	0.0f, 	WALL},
						  	  {WALL,	0.0f,  	0.0f,  	0.0f, 	0.0f, 	WALL},
						  	  {WALL,	WALL,	WALL,	WALL, 	WALL, 	WALL} };
	 
	public static void main(String[] args) {
		Deque<Node> path = Astar.calculatePath(1, 1, 1, 4);
		
		for (Node node : path) {
			System.out.println("(" + node.getX() + "," + node.getY() + ")");
		}
	}

}
