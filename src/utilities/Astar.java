package utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

//import java.util.Random;

/**
 * Path finding algorithm A*
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */

public class Astar {

	LinkedList<Node> openList = new LinkedList<>(); //contains nodes to visit, possibly sort after TotalCost
	ArrayList<Node> closedList = new ArrayList<>(); //contains visited nodes
	final int VERYHIGHVALUE = 999999;


	/**
	 * Calculate distance from (startX, startY) to (goalX, goalY)
	 * @param startX X-coordinate of start node
	 * @param startY Y-coordinate of start node
	 * @param goalX X-coordinate of goal node
	 * @param goalY Y-coordinate of goal node
	 * @return Distance from start node to goal node
	 */
	private int calculateDistanceToGoal(int startX, int startY, int goalX, int goalY) {
		return Math.abs((goalX-startX)+(goalY-startY));
	}

	/**
	 * Find node with lowest heuristic value in list
	 * @param list List containing nodes with heuristic values added by calculatePath
	 * @return Node with lowest heuristic value from list
	 */
	private Node findLowestHeuristicCost(LinkedList<Node> list) {
		int heuristicPrev = VERYHIGHVALUE; 
		Node returnMe = null;
		
		for (Node l : list) {
			int tmpTotalCost = l.getTotalCost();
			if (tmpTotalCost < heuristicPrev) {
				heuristicPrev = tmpTotalCost;
				returnMe = l;
			}
		}
		return returnMe;
	}

	/**
	 * Go through list to find path from start to goal
	 * @param list
	 * @return List with elements from head in list to just before null
	 */
	private List<Node> tracePath(List<Node> list) {
		List<Node> resultList = new LinkedList<>();
		Node cursor = list.get(0);
		
		while (cursor != null) {
			resultList.add(cursor);
			cursor = cursor.getParent();
		}
		return resultList;
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
	public List<Node> calculatePath(int[][] world, int startX, int startY, int goalX, int goalY) {
		assert(startX <= world.length || goalX <= world.length || startY <= world[0].length || goalY <= world[0].length);
		
		if (startX == goalX && startY == goalY) {
			return null; //TBI
		}
				
		Node start = new Node(startX, startY, calculateDistanceToGoal(startX, startY, goalX, goalY), 0, null);
		openList.add(start);			

		do {
			Node currentNode = findLowestHeuristicCost(openList);
			openList.remove(currentNode);
			closedList.add(currentNode);
			if (currentNode.getX() == goalX && currentNode.getY() == goalY) //Goal found
				break;

			int[][] neighbors = HexagonUtils.neighborTiles(currentNode.getX(), currentNode.getY(), false);

			for (int[] neighbor : neighbors) {
				if (/*neighbor is walkable or not in the closedList*/) { //<--- TBI
					//Kolla även heightmap (värde som säger om G-värdet ska ökas med ngt)
					//Om vatten ska G oxå ökas med ngt
					//Andra hinder? Var specas det om en ruta är walkable?
					Node newNode = new Node(neighbor[0], neighbor[1], calculateDistanceToGoal(neighbor[0], neighbor[1] , goalX, goalY), 1, currentNode);
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

		if (openList.size() == 0) { return null; }

		return tracePath(closedList);
	}

}
