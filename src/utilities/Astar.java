package utilities;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

import simulation.Race;
import utilities.NeedsController.NeedsControlled;



/**
 * Path finding algorithm A*
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */

public class Astar {
	final static int VERYHIGHVALUE = 999999;
	private static Race blocking;
	
	/**
	 * Resets the blocking race to null.
	 * Note that while you could call this alot it will affect the performance of the system.
	 * The function was made for test cases that needed resetting between tests.
	 */
	public static void reset(){
		blocking = null;
	}
	/**
	 * Calculate distance from (startX, startY) to (goalX, goalY) using Manhattan version.
	 * @param startX X-coordinate of start node
	 * @param startY Y-coordinate of start node
	 * @param goalX X-coordinate of goal node
	 * @param goalY Y-coordinate of goal node
	 * @return Distance from start node to goal node
	 */
	private static int calculateDistanceToGoal(int startX, int startY, int goalX, int goalY) {
		return ( Math.abs(goalX-startX) + Math.abs(goalY-startY) );
	}

	/**
	 * Find node with lowest heuristic value in list
	 * @param list List containing nodes with heuristic values added by calculatePath
	 * @return Node with lowest heuristic value from list
	 */
	private static Node findLowestTotalCost(LinkedList<Node> list, ArrayList<Node> closedList) {
		int heuristicPrev = VERYHIGHVALUE; 
		Node returnMe = null;

		for (Node l : list) {
			int tmpTotalCost = l.getTotalCost();
			if (tmpTotalCost < heuristicPrev && !closedList.contains(l)) {
				heuristicPrev = tmpTotalCost;
				returnMe = l;
			}
		}
		return returnMe;
	}

	/**
	 * Go through list to find path from start to goal
	 * @param position start position
	 * @param closedList the list that contains the start Node
	 * @return Stack with elements from head in list to just before null
	 */
	private static Deque<int[]> tracePath(int position, ArrayList<Node> closedList) {
		Deque<int[]> resultStack = new ArrayDeque<>();
		if(closedList.size() > position-1 && position > 0){
			Node cursor = closedList.get(position);
	
			while (cursor != null) {
				resultStack.addFirst(new int[]{cursor.getX(), cursor.getY()});
				cursor = cursor.getParent();
			}
		}
		
		return resultStack;
	}	

	/**
	 * Checks if species on x and y-coordinate
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return false if species on (x,y)-coordinate, true otherwise
	 */
	public static boolean noSpecies(int x, int y) {
		if(blocking == null){
			ArrayList<NeedsControlled> tmp = NeedsController.getNeed("Tree");
			if(tmp != null && tmp.size() > 0)
				blocking = (Race)tmp.get(0);
		}
		if(blocking.containsAnimal(x, y)){
			return false;
		}
		return true;
		
	}

	/**
	 * Calculates movement cost depending on heightmap found in Globals.heightmap
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return Cost for moving to (x,y)-coordinate
	 */
	private static int calculateMovementCost(int x, int y) {
		//return (int) (AstarDriver.world[x][y]); //for test purpose only
		return (int) (Globals.heightmap[x][y] / 20); //use this when used in DollyWood
	}

	/**
	 * Calculates path from (startX, startY) to (goalX, goalY) within hexagonal world found in Globals.
	 * @param startX X-coordinate of start node
	 * @param startY Y-coordinate of start node
	 * @param goalX X-coordinate of goal node
	 * @param goalY Y-coordinate of goal node
	 * @return Deque with elements representing shortest path from start to goal, null if goal not found.
	 */

	public static Deque<int[]> calculatePath(int startX, int startY, int goalX, int goalY) {
		LinkedList<Node> openList = new LinkedList<>(); //contains nodes to visit, possibly sort after TotalCost
		ArrayList<Node> closedList = new ArrayList<>(); //contains visited nodes
		Node start = new Node(startX, startY, calculateDistanceToGoal(startX, startY, goalX, goalY), 0, null);
		openList.add(start);
		boolean goalFound = false;


		//		USE BELOW TO TIME THE PATHFINDER
		//		long timeStart = System.currentTimeMillis();

		do {
			Node currentNode = findLowestTotalCost(openList, closedList);
			openList.remove(currentNode);
			
			if(currentNode == null){
				goalFound = true;
				break;
			}
			
			if (currentNode.getX() == goalX && currentNode.getY() == goalY) {
				//goal found
				closedList.add(currentNode);
				goalFound = true;
				break;
			}

			ArrayList<int[]> neighbors = HexagonUtils.neighborTiles(currentNode.getX(), currentNode.getY(), false);
			for (int[] neighbor : neighbors) {
				if(noSpecies(neighbor[0], neighbor[1])){
					Node newNode = new Node(neighbor[0], neighbor[1], calculateDistanceToGoal(neighbor[0], neighbor[1], goalX, goalY), currentNode.getMovementCost() + calculateMovementCost(neighbor[0],  neighbor[1]) + 1, currentNode);
	
					boolean existsInOpenList = false;
	
					for (Node nodeInOpenList : openList) {
						if (nodeInOpenList.getX() == newNode.getX() && nodeInOpenList.getY() == newNode.getY()) { //check X&Y-value for n and newNode
							if (nodeInOpenList.getMovementCost() < newNode.getMovementCost()) {
								//node exists in openList, do not add it to the openList.
								//nothing to do, exit foreach-loop
								existsInOpenList = true;
								break;
							} else {
								//node exists in openList, do not add it to the openList.
								//update movementCost and its parent
								nodeInOpenList.setParent(newNode.getParent());
								nodeInOpenList.setMovementCost(1 + newNode.getMovementCost());
								existsInOpenList = true;
								break;
							}
						}	
					}
	
					if (!existsInOpenList) {
						openList.add(newNode);
					}
				}
			}
			closedList.add(currentNode);

		} while (!openList.isEmpty());

		if (!goalFound && openList.isEmpty()) {
			return null; 
		}
		//		USE BELOW TO TIME THE PATHFINDER
		//		System.out.println("TIME TAKEN: " + (System.nanoTime() - time));
		//		System.out.println("OPEN LIST SIZE: " + openList.size() + ", ClosedList size: " + closedList.size() + ", TIME TAKEN: " + (int)(System.currentTimeMillis() - timeStart));

		return tracePath(closedList.size() - 1, closedList);
	}
}
