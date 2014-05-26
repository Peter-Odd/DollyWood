package utilities;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
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
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

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
	
	public static ArrayList<Race> races = new ArrayList<>();
	
	public static DayNightCycle dayNightCycle;
	
	public static Water water;
	
	/**
	 * 
	 * @return true if settingsframe is visible
	 */
	public static boolean visibleSettingsFrame(){
		return settingsFrame.isVisible();
	}
	
	private static boolean blocking;
	private static JFrame settingsFrame;
	/**
	 * Creates a window that holds all settings and statistics.
	 * @param blocking true if this window should be blocking. false if it should run in parallel with other code
	 * @param startup true if it is should not show statistics and show about text, false otherwise
	 * @param alwaysOnTop true if it should be locked above all other windows
	 */
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
			JScrollPane sp = new JScrollPane(aboutText);

			try {
				BufferedReader inStream = new BufferedReader(new InputStreamReader(new FileInputStream("res/about.txt"), "UTF-8"));
				String line;
				while((line = inStream.readLine()) != null){
					aboutText.append(line);
					aboutText.append("\n");
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
			aboutText.setWrapStyleWord(true);
			aboutText.setEditable(false);			
			panel.add(sp);
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
	/**
	 * Registers a graph to be showed in the statistics panel of the settings frame
	 * @param name name of the graph
	 * @param category category for the graph
	 * @param callFunction the function that the graph will call to collect data
	 * @param collectTime the sleep time(ms) in between collecting data.
	 */
	public static synchronized void registerGraph(String name, String category, Callable<Float> callFunction, long collectTime){
		boolean isRegistered = false;
		for(Graph g : graphList){
			if(g.getCategory().equals(category) && g.getName().equals(name)){
				isRegistered = true;
				break;
			}
		}
		if(!isRegistered){
			Graph g = new Graph(new Dimension(100,100), 25, collectTime, callFunction, name, category);
			graphList.add(g);
		}
	}
	
	/**
	 * Registers a setting to be shown in the Settings window
	 * @param name name of the setting
	 * @param category category of the setting
	 * @param min the minimum value that the setting can possibly have
	 * @param max the maximum value that the setting can possibly have
	 * @param current the default value of the setting
	 */
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
	
	/**
	 * Gets the value of the setting
	 * @param name The name of the setting to search for 
	 * @param category the category to search in
	 * @return The value of the searched setting or 0.0f, f none found.
	 */
	public static synchronized float getSetting(String name, String category){
		for(Setting s : settings){
			if(s.category.equals(category) && s.name.equals(name))
				return s.slider.getValue()/1000.0f;
		}
		return 0.0f;
	}
	
	/**
	 * Setting will hold a slider a name and a category.
	 * @author OSM Group 5 - DollyWood project
	 * @version 1.0
	 */
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
