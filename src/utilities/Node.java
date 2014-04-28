package utilities;

public class Node {
	@SuppressWarnings("unused")
	private float Heuristic; //Distance from node to goal
	private float MovementCost; //Shortest path from start to this node
	private float TotalCost; //Heuristic + MovementCost
	private Node Parent; //Parent of this node
	
	public Node(float heuristic, float movementCost, Node parent) {
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

	public float getTotalCost() {
		return TotalCost;
	}

	public void setTotalCost(float totalCost) {
		TotalCost = totalCost;
	}

	public float getMovementCost() {
		return MovementCost;
	}

	public void setMovementCost(float movementCost) {
		MovementCost = movementCost;
	}
	
	
}
