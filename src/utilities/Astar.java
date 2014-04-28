package utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

//import java.util.Random;

public class Astar {

	LinkedList<Node> openList = new LinkedList<>(); //contains nodes to visit, sort after TotalCost
	ArrayList<Node> closedList = new ArrayList<>(); //contains visited nodes
	
	private int calculateDistance(int startX, int startY, int goalX, int goalY) {
		return Math.abs((goalX-startX)+(goalY-startY));
	}
	
	/**
	 * 
	 * @param world
	 * @param start
	 * @param goal
	 * @return
	 */
	public int[][] calculatePath(int[][] world, int startX, int startY, int goalX, int goalY) {
		/*	check that start and goal is inside world
			if start == goal return path directly
			
		*/
		
		Node start = new Node(calculateDistance(startX, startY, goalX, goalY), 0, null);
		Node goal = new Node(0, 0, null);
		boolean goalFound = false;
		int nodeX = startX;
		int nodeY = startY;
		Node currentNode = start;
		
		
		
		do {
			int[][] neighbors = HexagonUtils.neighborTiles(nodeX, nodeY, false);
			
			for (int[] neighbor : neighbors) {
				Node newNode = new Node(calculateDistance(neighbor[0], neighbor[1] , goalX, goalY), calculateDistance(nodeX, nodeY, neighbor[0], neighbor[1]), currentNode);
				openList.add(newNode);
				//ev. sort list
			}
			closedList.add(currentNode);
			
			
			
		} while (openList.size() > 0 || goalFound == true);
		

		return null;
	}

	

}







//public static float[][] generateArray() {
//float[][] world = new float[2][2];
//Random r = new Random();
//
//for (int i = 0; i < 2; i++) {
//	for (int j = 0; j < 2; j++) {
//		world[i][j] = r.nextFloat();
//	}
//}
//return world;
//}