package utilities;

import java.util.ArrayList;

import simulation.DayNightCycle;
import simulation.Race;
import simulation.Water;

public class Globals {
	/*
	 * width and height = 2^n + 1
	 */
	public static int width = 33; 
	public static int height = 33;
	public static int screenWidth = 1920; 
	public static int screenHeight = 1080;
	
	public static float[][] heightmap;
	public static float worldFractalMax = 200.0f;
	public static float worldFractalMin = 0.0f;
	public static float  worldFractalRange = 20.0f;
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
}
