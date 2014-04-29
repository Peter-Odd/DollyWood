package utilities;

import java.util.ArrayList;

import simulation.DayNightCycle;
import simulation.Race;
import simulation.Water;

public class Globals {
	/*
	 * width and height = 2^n + 1
	 */
	public static int width, height;
	public static int screenWidth, screenHeight;
	public static float[][] heightmap; 
	public static ArrayList<Race> races = new ArrayList<>();
	
	public static DayNightCycle dayNightCycle;
	public static Water water;
}
