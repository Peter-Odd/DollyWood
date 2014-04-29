package utilities;

import java.util.Random;

/** Generates floats in a 2D array using the DiamondSqaure algorithm.
 * 
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 *
 */

public class Fractal {
	static Random r = new Random();
	
	/** Generates a random value between max and min.
	 * 
	 * @param max The max value.
	 * @param min The min value
	 * @return A value between max and min.
	 */
	
	private static float getRandom(float max, float min){
		return r.nextFloat() * (max-min) + min;
	}
	
	/** Fills map with floats which are calculated with the DiamondSquare algorithm. The floats will have a value
	 *  between 0 and 255.
	 * 
	 * @param map The array which will be filled.
	 * @param max Used to calculate the max value for the initial corners.
	 * @param min Used to calculate the minimal value for the initial corners
	 * @param randomRange A value in this range will be added to every position in the array when the value is calculated
	 * @param randomDiv The randomRange will be divided by this every recursive call. 
	 * @return map filled with floats.
	 */
	
	public static float[][] generateFractal(float[][] map, float max, float min, float randomRange, float randomDiv){
		
		map = intitateCorners(map, max, min, randomRange);
		map = diamondSquare(map, map.length-1, map.length-1, randomRange, randomDiv);
		
		return map;
	}
	
	/** Fills the corners of map with a value between (max-min)/2 + randomRande and (max-min)/2 - randomRange.
	 * 
	 * @param map The array which will be used.
	 * @param max Used to calculate the values for the corners.
	 * @param min Used to calculate the values for the corners.
	 * @param randomRange Used to calculate the values for the corners.
	 * @return map with the corner spots filled with values.
	 */
	
	public static float[][] intitateCorners(float[][] map, float max, float min, float randomRange){
		int largestX = map.length - 1;
		int largestY = map[0].length - 1;
		
		map[0][0] = getRandom((max-min)/2 + randomRange, (max-min)/2 - randomRange);
		map[largestX][0] = getRandom((max-min)/2 + randomRange, (max-min)/2 - randomRange);
		map[largestX][largestY] = getRandom((max-min)/2 + randomRange, (max-min)/2 - randomRange);
		map[0][largestY] = getRandom((max-min)/2 + randomRange, (max-min)/2 - randomRange);

		return map;
	}
	
	
	/** Executes the diamond and square step of the Diamond-Square algorithm.
	 * 
	 * @param map The array which will be used.
	 * @param xSide The width of the squares, from corner to corner. Or the distance between the right and left edge of a diamond.
	 * @param ySide The height of the squares, from corner to corner. Or the distance between the upper and lower edge of a diamond.
	 * @param randomRange The random number which is added in every step will be within this range.
	 * @param randomDiv The randomRange is divided with this every recursive call.
	 * @return The array filled with floats, generated with Diamond-Square algorithm.
	 */
	
	public static float[][] diamondSquare(float[][] map, int xSide, int ySide, float randomRange, float randomDiv){

		map = diamondStep(map, xSide, ySide, randomRange);
		map = squareStep(map, xSide, ySide, randomRange);
		
		if(xSide >= 4)
			map = diamondSquare(map, xSide/2, ySide/2, randomRange/randomDiv, randomDiv);
		
		
		return map;
	}
	
	/** Executes the diamond step of the Diamond-Square algorithm. Taking a square of four points, generate a random value 
	 *  at the square midpoint, where the two diagonals meet. The midpoint value is calculated by averaging the four corner 
	 *  values, plus a random amount. This gives you diamonds when you have multiple squares arranged in a grid.
	 * 
	 * @param map The array which will be used.
	 * @param xSide The width of the squares.
	 * @param ySide The height of the squares.
	 * @param randomRange The random value will be in this range.
	 * @return map with diamonds calculated.
	 */
	
	public static float[][] diamondStep(float[][] map, int xSide, int ySide, float randomRange){
		
		float center = 0.0f;
		int xHalfSide = xSide/2;
		int yHalfSide = ySide/2;
		
		for(int x=0; x<map.length-1;x+=xSide){
			for(int y=0;y<map[0].length-1;y+=ySide){
				if(map[x+xHalfSide][y+yHalfSide] == 0.0f){
					center = ((map[x][y] +								// left upper corner
							 map[x + xSide][y]  + 						// right upper corner
							 map[x][y + ySide] + 						// left lower corner
							 map[x + xSide][y + ySide])/4.0f) +			// right lower corner
							 getRandom(randomRange*2, 0) - randomRange;
					
					if(center > 255.0f)
						center = 250.0f;
					
					if(center < 0.0f)
						center = 0.001f;
					
					map[x+xHalfSide][y+yHalfSide] = center;
				}
			}
		}
			
		return map;
	}
	
	/** Executes the square step of the Diamond-Square algorithm. Taking each diamond of four points, generate a 
	 * random value at the center of the diamond. Calculate the midpoint value by averaging the corner values, plus a 
	 * random amount generated in the same range as used for the diamond step. This gives you squares again.
	 * 
	 * @param map The array which will be used.
	 * @param xSide The distance between the right and left edge of a diamond.
	 * @param ySide The distance between the upper and lower edge of a diamond.
	 * @param randomRange The random value will be in this range.
	 * @return map with squares calculated.
	 */
	
	public static float[][] squareStep(float[][] map, int xSide, int ySide, float randomRange){
		
		float value = 0;
		float elements = 0.0f;
		int xHalfSide = xSide/2;
		int yHalfSide = ySide/2;
		int count = 0;
		for(int x=0;x<map.length;x+=xHalfSide){
			count++;
			for(int y=0+(((count)%2)*yHalfSide);y<map[0].length;y+=ySide){
				if(map[x][y] == 0.0f){
					elements = 0;
					value = 0;
					if((x - xHalfSide) >= 0){
						value += map[(x-xHalfSide)][y]; // left of center
						elements++;
					}
					if((x + xHalfSide) < map.length){
						value += map[(x+xHalfSide)][y]; // right of center
						elements++;
					}
					if((y - yHalfSide) >= 0){
						value += map[x][(y-yHalfSide)]; // above center
						elements++;
					}
					if((y + yHalfSide) < map[0].length){
						value += map[x][(y+yHalfSide)]; // below center
						elements++;
					}
					
					if(elements != 0)					
					value = (value/elements) + getRandom(randomRange*2, 0) - randomRange;
					
					if(value > 255.0f)
						value = 255.0f;
					
					if(value < 0.0f)
						value = 0.001f;
						
					if(map[x][y] == 0.0f)
					map[x][y] = value;
				}
			}
		}
		
	return map;
		
	}	
	
	/** Sets every float in map which are >max to max and every float which are <min to min.
	 * 
	 * @param map The array which will be used.
	 * @param max The max value.
	 * @param min The min value.
	 * @return map with the values modified.
	 */
	
	public static float[][] cutMap(float[][] map, float max, float min){
		
		for(int x=0;x<map.length;x++)
			for(int y=0;y<map[0].length;y++){
				if(map[x][y]<min)
					map[x][y] = min;
				
				else if(map[x][y]>max)
					map[x][y] = max;
			}
		return map;
	}

}

