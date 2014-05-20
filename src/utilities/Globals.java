package utilities;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import simulation.DayNightCycle;
import simulation.Race;
import simulation.Water;

public class Globals {
	/*
	 * width and height = 2^n + 1
	 */
	public static int width = 65;
	public static int height = 65;
	public static int screenWidth = 1920; 
	public static int screenHeight = 1080;
	
	public static float[][] heightmap;
	public static float worldFractalMax = 200.0f;
	public static float worldFractalMin = 0.0f;
	public static float  worldFractalRange = 70.0f;
	public static float  worldFractalDiv = 2.0f;
	
	public static int startingSheep = 5;
	public static int startingWolves = 5;
	public static int startingTrees = 20;
	
	public static ArrayList<Race> races = new ArrayList<>();
	
	public static DayNightCycle dayNightCycle;
	
	public static Water water;
	public static float startingWaterAmmount = 0.5f;
	
	public static int waterSleepLength = 100;
	public static int grassSleepLength = 100;
	public static int treeSleepLength = 400;
	public static int dayNightSleepLength = 1000;
	
	public static boolean visibleSettingsFrame(){
		return settingsFrame.isVisible();
	}
	
	private static boolean blocking;
	private static JFrame settingsFrame;
	public static void createSettingsFrame(boolean blocking, boolean startup, boolean alwaysOnTop){
		Globals.blocking = blocking;
		settingsFrame = new JFrame("Settings");
		settingsFrame.setLayout(new BorderLayout());
		JButton closeButton = new JButton("Let's go to DollyWood!");
		closeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Globals.blocking = false;
				settingsFrame.setVisible(false);
			}
		});
		JTabbedPane tabbPane = new JTabbedPane();
		
		if(startup){
			JPanel panel = new JPanel(){
				private static final long serialVersionUID = 1L;
				public void paintComponent(Graphics g){
					try {
						BufferedImage image = ImageIO.read(new File("res/BANNER.PNG"));
						g.drawImage(image, 0, 0, 512, 256, null);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			panel.setLayout(new GridLayout(2,2));
			panel.add(new JLabel());
			JTextArea aboutText = new JTextArea();
			try {
				BufferedReader inStream = new BufferedReader(new FileReader(new File("res/about.txt")));
				String line;
				while((line = inStream.readLine()) != null){
					aboutText.append(line);
				}
				inStream.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			aboutText.setLineWrap(true);
			aboutText.setEditable(false);
			panel.add(aboutText);
			panel.setPreferredSize(new Dimension(512, 512));
			
			tabbPane.add("home", panel);
		}
		//Statistics
		if(!startup){
			JTabbedPane statisticsTabPane = new JTabbedPane();
			for(Graph g : graphList){
				boolean added = false;
				for(int i = 0; i < statisticsTabPane.getTabCount(); i++){
					if(statisticsTabPane.getTitleAt(i).equals(g.getCategory())){
						JPanel panel = ((JPanel)(statisticsTabPane.getComponentAt(i)));
						if(panel != null){
							panel.add(g);
						}
						added = true;
					}
				}
				if(!added){
					JPanel panel = new JPanel();
					panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
					statisticsTabPane.add(panel);
					statisticsTabPane.setTitleAt(statisticsTabPane.getTabCount()-1, g.getCategory());
					panel.add(g);
				}
			}
			tabbPane.add("Statistics", statisticsTabPane);
		}
		
		for(Setting s : settings){
			boolean added = false;
			JSlider slider = s.slider;
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			slider.setMajorTickSpacing((s.slider.getMaximum())/4);
			Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
			for(int i = 0; i <= 4; i++){
				labelTable.put(new Integer(((s.slider.getMaximum())/4)*i), new JLabel(((s.slider.getMaximum()/4000.0f)*i) + ""));
			}
			//labelTable.put(new Integer(0), new JLabel("0.0"));
			slider.setLabelTable(labelTable);
			//slider.setMinorTickSpacing(10);
			for(int i = 0; i < tabbPane.getTabCount(); i++){
				if(tabbPane.getTitleAt(i).equals(s.category)){
					JPanel panel = ((JPanel)(tabbPane.getComponentAt(i)));
					if(panel != null){
						panel.add(new JLabel(s.name));
						panel.add(slider);
					}
					added = true;
				}
			}
			if(!added){
				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				tabbPane.add(panel);
				tabbPane.setTitleAt(tabbPane.getTabCount()-1, s.category);
				panel.add(new JLabel(s.name));
				panel.add(slider);
			}
		}
		
		settingsFrame.add(tabbPane, BorderLayout.CENTER);
		if(Globals.blocking){
			settingsFrame.add(closeButton, BorderLayout.SOUTH);
		}
		
		settingsFrame.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		settingsFrame.setLocation(screenSize.width/2-(settingsFrame.getWidth()/2), screenSize.height/2-(settingsFrame.getHeight()/2));
		settingsFrame.setVisible(true);
		settingsFrame.setAlwaysOnTop(alwaysOnTop);
		if(startup)
			settingsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			Robot robot = new Robot();
			robot.mouseMove(screenSize.width/2, screenSize.height/2);
		} catch (AWTException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		while(Globals.blocking){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private static ArrayList<Setting> settings = new ArrayList<>();
	private static ArrayList<Graph> graphList = new ArrayList<>();
	
	public static synchronized void registerGraph(String name, String category, Callable<Float> callFunction, long collectTime){
		boolean isRegistered = false;
		for(Graph g : graphList){
			if(g.getName().equals(name) && g.getCategory().equals(category)){
				isRegistered = true;
				break;
			}
		}
		if(!isRegistered){
			Graph g = new Graph(new Dimension(100,100), 25, collectTime, callFunction, name, category);
			graphList.add(g);
		}
	}
	
	public static synchronized void registerSetting(String name, String category, float min, float max, float current){
		boolean isRegistered = false;
		for(Setting s : settings){
			if(s.name.equals(name) && s.category.equals(category)){
				isRegistered = true;
				break;
			}
		}
		if(!isRegistered)
			settings.add(new Setting(name, category, new JSlider((int)(min*1000), (int)(max*1000), (int)(current*1000))));
	}
	
	public static synchronized float getSetting(String name, String category){
		for(Setting s : settings){
			if(s.name.equals(name) && s.category.equals(category))
				return s.slider.getValue()/1000.0f;
		}
		return 0.0f;
	}
	
	private static class Setting{
		public String name;
		public String category;
		public JSlider slider;
		public Setting(String name, String category, JSlider slider){
			this.name = name;
			this.category = category;
			this.slider = slider;
		}
	}
}