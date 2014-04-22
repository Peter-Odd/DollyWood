package graphics;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import utilities.Globals;

public class Graphics2D extends JPanel{
	public Graphics2D(){
		JFrame frame = new JFrame("DollyWood");
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		this.repaint();
		frame.setVisible(true);
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(Color.PINK);
		int size = 10;
		Globals.heightmap[6][2] = 255.0f;
		for(int x = 0; x < Globals.width; x++){
			for(int y = 0; y < Globals.height; y++){
				g.setColor(new Color((int)(Globals.heightmap[x][y]),(int)(Globals.heightmap[x][y]),(int)(Globals.heightmap[x][y])));
				g.fillRect(x*size, y*size, size, size);
			}
		}
		try{
			Thread.sleep(1000);
		}
		catch(Exception e){
			System.exit(0);
		}
		//this.repaint();
	}
}
