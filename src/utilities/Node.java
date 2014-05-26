package utilities;

/**
 * Node representation
 * @author OSM Group 5 - DollyWood project
 * @versin 1.0
 */
public class Node {
	private int Heuristic; 		//Distance from node to goal, Manhattan method (H)
	private int MovementCost; 	//Shortest path from start to this node (G)
	private int TotalCost; 		//Heuristic + MovementCost (F)
	private Node Parent; 		//Parent of this node
	private int X; 				//Position X-coordinate
	private int Y; 				//Position Y-coordinate
	
	/**
	 * 
	 * @param X Node's X-coordinate
	 * @param Y Node's Y-coordinate
	 * @param heuristic Distance from node to goal, Manhattan method(dX+dY)
	 * @param movementCost Shortest path from start node to this node
	 * @param parent Parent of current node
	 */
	public Node(int X, int Y, int heuristic, int movementCost, Node parent) {
		this.X = X;
		this.Y = Y;
		Heuristic = heuristic;
		setMovementCost(movementCost);
		setTotalCost(heuristic + movementCost);
		setParent(parent);
	}

	public Node getParent() {
		return Parent;
	}

	public void setParent(Node parent) {
		Parent = parent;
	}

	public void recalculateTotalCost() {
		TotalCost = Heuristic + MovementCost;
	}
	
	public int getTotalCost() {
		return TotalCost;
	}

	public void setTotalCost(int totalCost) {
		TotalCost = totalCost;
	}

	public int getMovementCost() {
		return MovementCost;
	}

	public void setMovementCost(int movementCost) {
		MovementCost = movementCost;
	}

	public int getX() {
		return X;
	}

	public int getY() {
		return Y;
	}

}
