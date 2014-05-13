package utilities;

import java.util.ArrayList;

import simulation.DayNightCycle;
import simulation.Race;
import simulation.Water;

public class Globals {
	/*
	 * width and height = 2^n + 1
	 */
	public static int width, height = 33;
	public static int screenWidth = 1920; 
	public static int screenHeight = 1080;
	
	public static float[][] heightmap = new float[width][height];
	public static float worldFractalMax, worldFractalMin, worldFractalRange, worldFractalDiv;
	
	public static int startingSheep, startingWolves, startingTrees;
	
	public static ArrayList<Race> races = new ArrayList<>();
	
	public static DayNightCycle dayNightCycle;
	
	public static Water water;
	public static float startingWaterAmmount = 0.5f;
	
	public static int waterSleepLength, grassSleepLength, treeSleepLength = 100;
	public static int dayNightSleepLength = 1000;
}
