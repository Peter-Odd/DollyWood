package utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

import javax.swing.JPanel;
/**
 * Graph will paint a super advanced sheep graph that shows some form of statistical data.
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */
public class Graph extends JPanel implements Runnable{
	private static final long serialVersionUID = 1L;
	
	private LinkedList<Float> history;
	private Semaphore historyLock;
	private long collectTime;
	private Callable<Float> collectFunction;
	private Float maxVal = 0.0f;
	private String name;
	private String category;
	
	/**
	 * 
	 * @param preferredSize The size that the graph will prefer to take as a minimum bound
	 * @param historyLength How many points back that the graph will save
	 * @param collectTime the sleep time(ms) between each data collect
	 * @param collectFunction the function to collect data from
	 * @param name name of the graph
	 * @param category category of the graph
	 */
	public Graph(Dimension preferredSize, int historyLength, long collectTime, Callable<Float> collectFunction, String name, String category){
		history = new LinkedList<>();
		this.historyLock = new Semaphore(1);
		for(int i = 0; i < historyLength; i++)
			history.addFirst(new Float(0.0f));
		this.collectFunction = collectFunction;
		this.collectTime = collectTime;
		this.setPreferredSize(preferredSize);
		this.name = name;
		this.category = category;
		Thread t = new Thread(this);
		t.start();
	}
	
	public String getName(){
		return name;
	}
	
	public String getCategory(){
		return category;
	}
	
	/**
	 * Overrides JPanel.paintComponent() to draw a graph
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(Color.BLACK);
		g.setColor(Color.ORANGE);
		g.drawRect(0, 0, super.getWidth(), super.getHeight());//Draw bound-box
		int centerLine = super.getHeight()/2;
		g.setColor(Color.GREEN);
		g.drawLine(0, centerLine, super.getWidth(), centerLine);//Draw mid-line
		
		g.setColor(Color.CYAN);
		int xSpace = super.getWidth()/(history.size()-1);
		float ySpace = (float)(centerLine/maxVal);
		int count = 0;
		try {
			historyLock.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<Float> it = history.iterator();
		if(it.hasNext()){
			Float prev = (Float) it.next();
			while(it.hasNext()){
				Float current = (Float) it.next();
				g.drawLine(super.getWidth()-(count*xSpace), (int)(centerLine-(prev*ySpace)), super.getWidth()-((count+1)*xSpace), (int)(centerLine-(current*ySpace)));
				prev = current;
				count++;
			}
		}
		historyLock.release();
		String topString = name + ": " + (char)(177) + String.format("%.4g%n", maxVal) + "  Currently: " + String.format("%.4g%n", history.getFirst());

        FontMetrics fm = g.getFontMetrics();
        Rectangle2D rect = fm.getStringBounds(topString, g);
		int textXPos = 8;
		int textYPos = centerLine+14;
		
		g.setColor(Color.BLACK);
		g.fillRect(textXPos, textYPos-fm.getAscent(), (int)rect.getWidth(), (int)rect.getHeight());
		g.setColor(Color.DARK_GRAY);
		g.drawRect(textXPos, textYPos-fm.getAscent(), (int)rect.getWidth(), (int)rect.getHeight());
		g.setColor(Color.LIGHT_GRAY);
		g.drawChars(topString.toCharArray(), 0, topString.length(), textXPos, textYPos);
	}

	/**
	 * start point for graph thread.
	 * this will collect the data when needed
	 */
	public void run() {
		while(true){
			try {
				historyLock.acquire();
				history.addFirst(collectFunction.call());
				history.removeLast();
				if(Math.abs(history.getFirst()) > Math.abs(maxVal))
					maxVal = Math.abs(history.getFirst());
				historyLock.release();
				this.repaint();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				Thread.sleep(collectTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}