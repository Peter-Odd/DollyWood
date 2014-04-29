package utilities;

public class AstarDriver {
	
	public static int width = 4;
	public static int height = 4;
	
	static float[][] world ={ {0.0f,100.0f,0.0f,0.0f},
						      {0.0f,100.0f,0.0f,0.0f}, 
						      {0.0f,100.0f,0.0f,0.0f},
						      {0.0f,0.0f,  0.0f,0.0f} };

	
	public static void main(String[] args) {

		
	/*	for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				System.out.println(world[i][j]);
			}
		}*/
		
		for(Node n : Astar.calculatePath(0, 0, 3, 2)) {
			System.out.println("X: " + n.getX() + ", Y: " + n.getY());
		}

		

	}

}
