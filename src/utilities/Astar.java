package utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import simulation.Race;


/**
 * Path finding algorithm A*
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */

public class Astar {

	static LinkedList<Node> openList = new LinkedList<>(); //contains nodes to visit, possibly sort after TotalCost
	static ArrayList<Node> closedList = new ArrayList<>(); //contains visited nodes
	final static int VERYHIGHVALUE = 999999;


	/**
	 * Calculate distance from (startX, startY) to (goalX, goalY)
	 * @param startX X-coordinate of start node
	 * @param startY Y-coordinate of start node
	 * @param goalX X-coordinate of goal node
	 * @param goalY Y-coordinate of goal node
	 * @return Distance from start node to goal node
	 */
	private static int calculateDistanceToGoal(int startX, int startY, int goalX, int goalY) {
	//	System.out.println("Distance: " + Math.abs((goalX-startX)+(goalY-startY)));
		return Math.abs((goalX-startX)+(goalY-startY));
	}

	/**
	 * Find node with lowest heuristic value in list
	 * @param list List containing nodes with heuristic values added by calculatePath
	 * @return Node with lowest heuristic value from list
	 */
	private static Node findLowestTotalCost(LinkedList<Node> list) {
		int heuristicPrev = VERYHIGHVALUE; 
		Node returnMe = null;

		for (Node l : list) {
			int tmpTotalCost = l.getTotalCost();
			if (tmpTotalCost < heuristicPrev) {
				heuristicPrev = tmpTotalCost;
				returnMe = l;
			}
		}
		//System.out.println(returnMe.toString());
		return returnMe;
	}

	/**
	 * Go through list to find path from start to goal
	 * @param list
	 * @return Stack with elements from head in list to just before null
	 */
	private static Stack<Node> tracePath(List<Node> list) {
		Stack<Node> resultStack = new Stack<>();
		Node cursor = list.get(0);

		while (cursor != null) {
			resultStack.push(cursor);
			cursor = cursor.getParent();
		}
		return resultStack;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private static boolean findSpecies(int x, int y) {
		boolean walkable = true;
		/*for (Race r : Globals.races) {
			if (r.getSpeciesAt(x, y) != null) {
				walkable = false;
				break;
			}
		}*/
		 return walkable;
	}
	
	private static int calculateMovementCost(int x, int y) {
		return (int) (1 + AstarDriver.world[x][y]);
		//return 1 + (int) (Globals.heightmap[x][y] / 75);
		
	}
	
	/**
	 * Calculates path from (startX, startY) to (goalX, goalY) within world.
	 * @param world world coordinates represented as a two dimensional array
	 * @param startX X-coordinate of start node
	 * @param startY Y-coordinate of start node
	 * @param goalX X-coordinate of goal node
	 * @param goalY Y-coordinate of goal node
	 * @return List with elements representing shortest path from start to goal
	 */
	public static Stack<Node> calculatePath(/*int[][] world, */int startX, int startY, int goalX, int goalY) {
	//	assert(startX <= world.length || goalX <= world.length || startY <= world[0].length || goalY <= world[0].length);

		/*if (startX == goalX && startY == goalY) {
			return null; //TBI
		}*/
		boolean goalFound = false;
		
		Node start = new Node(startX, startY, calculateDistanceToGoal(startX, startY, goalX, goalY), 0, null);
		openList.add(start);		

		do {
			//System.out.println("OpenList size: " + openList.size());
			for (int i=0; i<openList.size();i++) {
				System.out.println("openList, X: " + openList.get(i).getX() + ", Y: " + openList.get(i).getY());
			}
			Node currentNode = findLowestTotalCost(openList);
			openList.remove(currentNode);
			closedList.add(currentNode);
			//System.out.println("X: " + currentNode.getX() + " " + "Y: " + currentNode.getY());
			if (currentNode.getX() == goalX && currentNode.getY() == goalY) { //Goal found
				goalFound = true;
				break;
			}

			ArrayList<int[]> neighbors = HexagonUtils.neighborTiles(currentNode.getX(), currentNode.getY(), false);

				
			for (int[] neighbor : neighbors) {
				if ( findSpecies(neighbor[0], neighbor[1]) ) {
					//if water what to do?
					Node newNode = new Node(neighbor[0], neighbor[1], calculateDistanceToGoal(neighbor[0], neighbor[1] , goalX, goalY), calculateMovementCost(neighbor[0], neighbor[1]), currentNode);
					if (openList.contains(newNode)) 
					{ 	/*check to see if this path to that square is better, using G cost as the measure*/
						if ((currentNode.getMovementCost() + 1) < newNode.getMovementCost()) {
							newNode.setParent(currentNode);
						} else {/*Do nothing*/}

					} else {
						openList.add(newNode);
					}
				} else {/*Do nothing*/}
			}
		} while (openList.size() > 0);

		if (openList.size() == 0 && goalFound == false) { return null; }

		return tracePath(closedList);
	}

}
