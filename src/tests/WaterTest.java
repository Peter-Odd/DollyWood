package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import simulation.Water;
import utilities.Globals;

public class WaterTest {

	@SuppressWarnings("deprecation")
	@Test
	public void downFlow() {
		Globals.heightmap = new float[Globals.width][Globals.height];
		Globals.heightmap[1][1] = 255.0f;
		Globals.registerSetting("Cloud count", "Cloud", 0, 0, 0);
		Globals.registerSetting("Sleep", "Water", 0, 0, 0);
		Water water = new Water();
		Thread waterThread = new Thread(water);
		waterThread.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("Cannot delay testThread");
		}
		waterThread.stop();
		
		assertFalse(water.getGroundWaterLevel(0, 0) < water.getGroundWaterLevel(1, 1));
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void dissipation() {
		Globals.heightmap = new float[Globals.width][Globals.height];
		Globals.registerSetting("Cloud count", "Cloud", 0, 0, 0);
		Globals.registerSetting("Sleep", "Water", 0, 0, 0);
		Globals.registerSetting("Starting water", "Water", 0, 1, 0.5f);
		Water water = new Water();
		Thread waterThread = new Thread(water);
		waterThread.start();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("Cannot delay testThread");
		}
		water.drawGroundWaterLevel(1, 1, 0.5f);
		assertTrue(water.getGroundWaterLevel(1, 1) < 0.1f);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("Cannot delay testThread");
		}
		waterThread.stop();
		
		assertFalse(water.getGroundWaterLevel(1, 1) == water.getAverageGroundWater());
	}

}
