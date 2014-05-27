package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FractalTest {

	
	private float[][] testFractal = new float[65][65];
	
	@Before
	public void generateTestFractal(){
		testFractal = utilities.Fractal.generateFractal(testFractal, 200, 1, 70, 2);
	}
	
	@Test
	public void testGenerateFractal() {
		for(int x = 0; x<testFractal.length;x++){
			for(int y = 0; y<testFractal.length;y++){
				assertTrue("Value is less than 0", testFractal[x][y]>=0.0f);
				assertTrue("Value is more than 255", testFractal[x][y]<=255.0f);
			}
		}
	}
	
	@Test
	public void testCutMap(){
		float[][] testCut = utilities.Fractal.cutMap(testFractal, 400, -20);
		
		for(int x = 0; x<testCut.length;x++){
			for(int y = 0; y<testCut.length;y++){
				assertTrue("Value changed after cutting with values which should not affect the values in the map.", testFractal[x][y] == testCut[x][y]);
			}
		}
		
		testCut = utilities.Fractal.cutMap(testFractal, 160, 90);
		
		for(int x = 0; x<testCut.length;x++){
			for(int y = 0; y<testCut.length;y++){
				assertTrue("Value is less than minimum", testCut[x][y]>=90.0f);
				assertTrue("Value is more than maximum", testCut[x][y]<=160.0f);
			}
		}
	}

}
