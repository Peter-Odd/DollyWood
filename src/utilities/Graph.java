package utilities;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
/**
 * A graph will plot the values coming from a provided function in a graph with time going along the x-axis.
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 *
 */
public class Graph extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private BoundFloatBuffer buffer;
	
	private Color backgroundColor = new Color(0,0,0);
	private Color gridColor = new Color(100,100,100);
	private int gridSpace = 40;

	private Color graphLineColor = new Color(255,255,255);
	
	public Graph(int bufferLength){
		buffer = new BoundFloatBuffer(bufferLength);
	}

	public Graph(Dimension preferredSize, int bufferLength){
		this(bufferLength);
		this.setPreferredSize(preferredSize);
	}
	
	/**
	 * Adds a new value to the graph
	 * @param value
	 */
	public void addPoint(float value){
		buffer.addPoint(value);
	}
	
	public void paintComponent(Graphics g){
		int width = this.getWidth();
		int height = this.getHeight();
		this.setBackground(backgroundColor);
		
		//Grids
		g.setColor(gridColor);
		//Vertical grids
		for(int i = 0; i < width; i+=gridSpace){
			g.drawLine(i, 0, i, height);
		}
		//Horizontal grids
		for(int i = 0; i < height; i+=gridSpace){
			g.drawLine(0, i, width, i);
		}
		
		//Draw line-graph
		float yScale = 1.0f;
		g.setColor(graphLineColor);
		float prevValue = buffer.getValue();
		int xPos = 0;
		while(!buffer.readIsFirst()){
			float currentValue = buffer.getValue();
			g.drawLine(xPos, (int)(prevValue*yScale)+(height/2), (xPos+=gridSpace), (int)(currentValue*yScale)+(height/2));
		}
	}
	
	private class BoundFloatBuffer{
		private Node readNode;
		private Node writeNode;
		public BoundFloatBuffer(int bufferLength){
			Node newNode = new Node(null, null, 0.0f);
			Node first = newNode;
			for(int i = 1; i < bufferLength; i++){
				newNode.setNext(new Node(null, newNode, 0.0f));
				newNode = newNode.getNext();
			}
			newNode.setNext(first);
			writeNode = first;
			readNode = first;
		}
		
		private void addPoint(float value){
			writeNode.setValue(value);
			writeNode = writeNode.getNext();
		}
		
		public boolean readIsFirst(){
			return writeNode.equals(readNode);
		}
		
		public float getValue(){
			float returnVal = readNode.getValue();
			readNode = readNode.getPrev();
			return returnVal;
		}
		
		private class Node{
			private float value;
			private Node next;
			private Node prev;
			public Node(Node next, Node prev, float value) {
				this.setNext(next);
				this.setPrev(prev);
				this.setValue(value);
			}
			public Node getNext() {
				return next;
			}
			public void setNext(Node next) {
				this.next = next;
			}
			public float getValue() {
				return value;
			}
			public void setValue(float value) {
				this.value = value;
			}
			public Node getPrev() {
				return prev;
			}
			public void setPrev(Node prev) {
				this.prev = prev;
			}
		}
	}
}