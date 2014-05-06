package utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
	static ArrayList<Node> obstacleList = new ArrayList<>(); //contains unwalkable objects

	static int count = 0;
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
		return ( Math.abs(goalX-startX) + Math.abs(goalY-startY) );
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
			if (tmpTotalCost < heuristicPrev && !closedList.contains(l)) {
				heuristicPrev = tmpTotalCost;
				returnMe = l;
			}
		}
		System.out.println("Lowest total cost: " + returnMe.getTotalCost());
		return returnMe;
	}

	/**
	 * Go through list to find path from start to goal
	 * @param list
	 * @return Stack with elements from head in list to just before null
	 */
	private static Stack<Node> tracePath(int position) {
		System.out.println("GOAL FOUND, EUREKA!");

		Stack<Node> resultStack = new Stack<>();
		Node cursor = closedList.get(position);

		while (cursor != null) {
			System.out.println("(" + cursor.getX() + "," + cursor.getY() + ")." );	
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
		// 	System.out.println("WORLD AT X, Y: " + x + ", " + y + ": " + (int) (AstarDriver.world[x][y]));
		return (int) (AstarDriver.world[x][y]);
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

	public static Stack<Node> calculatePath(int startX, int startY, int goalX, int goalY) {
		Node start = new Node(startX, startY, calculateDistanceToGoal(startX, startY, goalX, goalY), 0, null);
		openList.add(start);
		boolean goalFound = false;

		do {
			Node currentNode = findLowestTotalCost(openList);
			openList.remove(currentNode);
			
			if (currentNode.getX() == goalX && currentNode.getY() == goalY) {
				//goal found
				closedList.add(currentNode);
				goalFound = true;
				break;
			}

			ArrayList<int[]> neighbors = HexagonUtils.neighborTiles(currentNode.getX(), currentNode.getY(), false);

			for (int[] neighbor : neighbors) {
				Node newNode = new Node(neighbor[0], neighbor[1], calculateDistanceToGoal(neighbor[0], neighbor[1], goalX, goalY), currentNode.getMovementCost() + calculateMovementCost(neighbor[0],  neighbor[1]) + 1, currentNode);

				boolean existsInOpenList = true;

				for (Node nodeInOpenList : openList) {
					if (nodeInOpenList.getX() == newNode.getX() && nodeInOpenList.getY() == newNode.getY()) { //check X&Y-value n and newNode
						if (nodeInOpenList.getMovementCost() < newNode.getMovementCost()) {
							//node exists in openList, do not add it to the openList.
							//nothing to do, exit foreach-loop
							existsInOpenList = false;
							break;
						} else {
							//node exists in openList, do not add it to the openList.
							//update movementCost and its parent
							nodeInOpenList.setParent(newNode);
							nodeInOpenList.setMovementCost(1 + newNode.getMovementCost());
							existsInOpenList = false;
							break;
						}
					}	
				}
				if (existsInOpenList) {
					openList.add(newNode);
				}
			}
			closedList.add(currentNode);
			
		} while (!openList.isEmpty());
		
		if (!goalFound && openList.isEmpty()) {
			return null; //TBI
		}
		
		return tracePath(closedList.size() - 1);
			
	}

	//	public static Stack<Node> calculatePath(int startX, int startY, int goalX, int goalY) {
	//		boolean goalFound = false;
	//		Node start = new Node(startX, startY, calculateDistanceToGoal(startX, startY, goalX, goalY), 0, null);
	//		openList.add(start);
	//
	//		do {
	//			Node currentNode = findLowestTotalCost(openList);
	//			openList.remove(currentNode);
	//
	//			if (currentNode.getX() == goalX && currentNode.getY() == goalY) {
	//				closedList.add(currentNode);
	//				goalFound = true;
	//				break;
	//			}
	//
	//			ArrayList<int[]> neighbors = HexagonUtils.neighborTiles(currentNode.getX(), currentNode.getY(), false);
	//
	//			for (int[] neighbor : neighbors) {
	//				//				System.out.println("X: " + neighbor[0] + " Y: " + neighbor[1]);
	//				//				System.out.println("Movement: " + calculateMovementCost(neighbor[0], neighbor[1]));
	//				Node newNode = new Node(neighbor[0], neighbor[1], calculateDistanceToGoal(neighbor[0], neighbor[1], goalX, goalY), currentNode.getMovementCost() + 1 + calculateMovementCost(neighbor[0], neighbor[1]), currentNode);
	//
	//				//				if (calculateMovementCost(neighbor[0],  neighbor[1]) > 990) {
	//				//					newNode.setParent(null);
	//				//					closedList.add(newNode);
	//				//					continue;
	//				//				}
	//
	//				boolean existsInOpenList = false;
	//
	//				for (Node n : openList) {
	//
	//					if (n.getX() == newNode.getX() && n.getY() == newNode.getY()) {
	//						if (n.getMovementCost() < newNode.getMovementCost()) {
	//							existsInOpenList = true; 
	//							break;
	//						}
	//						else {
	//							n.setParent(newNode.getParent());
	//							n.setMovementCost(newNode.getMovementCost());
	//							//System.out.println("Total cost (before): " + n.getTotalCost());
	//							n.recalculateTotalCost();
	//							//System.out.println("Total cost (after): " + n.getTotalCost());
	//							existsInOpenList = true;
	//							break;
	//						}
	//					}
	//				}
	//
	//				if (!existsInOpenList) {  //node doesnt exist in the openList yet.. lets add it.
	//					//	if (newNode.getTotalCost() < 100) 
	//					openList.add(newNode); 
	//				} else 
	//				{/*do nothing*/};
	//			}
	//			if (currentNode.getTotalCost() < 100) { 
	//				closedList.add(currentNode);
	//			}
	//
	//		} while (openList.size() > 0);
	//
	//
	//		if (openList.size() == 0 && goalFound == false) {
	//			return null;  //TBI, what to return if goal not found...? Will this ever happen?
	//		}
	//
	//		return tracePath(closedList.size() - 1); //start to trace from the back, where the goal-node can be found...
	//
	//	}



	//	public static Stack<Node> calculatePath(int startX, int startY, int goalX, int goalY) {
	//		//	assert(startX <= world.length || goalX <= world.length || startY <= world[0].length || goalY <= world[0].length);
	//
	//		/*if (startX == goalX && startY == goalY) {
	//			return null; //TBI
	//		}*/
	//		boolean goalFound = false;
	//
	//		Node start = new Node(startX, startY, calculateDistanceToGoal(startX, startY, goalX, goalY), 0, null);
	//		openList.add(start);		
	//
	//		do {
	//
	//			Node currentNode = findLowestTotalCost(openList);
	//
	//			openList.remove(currentNode);
	//			closedList.add(currentNode);
	//
	//			if (currentNode.getX() == goalX && currentNode.getY() == goalY) { //Goal found
	//				goalFound = true;
	//				System.out.println("ClosedList: " + closedList.size());
	//				System.out.println("GOAL FOUND");
	//				break;
	//			}
	//
	//			ArrayList<int[]> neighbors = HexagonUtils.neighborTiles(currentNode.getX(), currentNode.getY(), false);
	//
	//			for (int[] neighbor : neighbors) {
	//				//if ( findSpecies(neighbor[0], neighbor[1]) ) {
	//					//if water what to do?
	//					Node newNode = new Node(neighbor[0], neighbor[1], calculateDistanceToGoal(neighbor[0], neighbor[1] , goalX, goalY), currentNode.getMovementCost() + calculateMovementCost(neighbor[0], neighbor[1]), currentNode);
	//
	//			
	//					
	//					
	//					for (Node n : openList) {
	//						
	//						if (n.getX() == newNode.getX() && n.getY() == newNode.getY()) {
	//							//node exists in openList, ev. update parent and calculate new movement cost and total cost
	//							System.out.println("CurrentNode movementcost: " + currentNode.getMovementCost());
	//							System.out.println("newNode movementcost: " + newNode.getMovementCost());
	//							if ((currentNode.getMovementCost() + 1) < newNode.getMovementCost()) {
	//								//TODO calculate new MovementCost and TotalValue
	//								newNode.setParent(currentNode);
	//								break;
	//							}
	//						}
	//						
	//					}
	//					openList.add(newNode);
	//
	//
	//
	//				/*} else { //belongs to if (findSpecies...)
	//					//species exists on neighbor[0], neighbor[1]. What to do? TBI
	//				}*/
	//			}
	//
	//		} while (openList.size() > 0);
	//
	//		if (openList.size() == 0 && goalFound == false) { return null; }
	//
	//		return tracePath(closedList);
	//	}


}
