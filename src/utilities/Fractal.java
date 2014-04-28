package utilities;

import java.util.Random;

public class Fractal {
	static Random r = new Random();
	
	public static float getRandom(float max, float min){
		return r.nextFloat() * (max-min) + min;
	}
	
	/** Fills map with floats which are calculated with the DiamondSquare algorithm. The floats will have a value
	 *  between 
	 * 
	 * @param map The array which will be filled.
	 * @param max 
	 * @param min
	 * @param randomRange
	 * @param randomDiv
	 * @return
	 */
	
	public static float[][] generateFractal(float[][] map, float max, float min, float randomRange, float randomDiv){
		
		map = intitateCorners(map, max, min, randomRange);
		map = diamondSquare(map, map.length-1, map.length-1, randomRange, randomDiv);
		
		return map;
	}
	
	public static float[][] intitateCorners(float[][] map, float max, float min, float randomRange){
		int largestX = map.length - 1;
		int largestY = map[0].length - 1;
		
		map[0][0] = getRandom((max-min)/2 + randomRange, (max-min)/2 - randomRange);
		map[largestX][0] = getRandom((max-min)/2 + randomRange, (max-min)/2 - randomRange);
		map[largestX][largestY] = getRandom((max-min)/2 + randomRange, (max-min)/2 - randomRange);
		map[0][largestY] = getRandom((max-min)/2 + randomRange, (max-min)/2 - randomRange);

		return map;
	}
	
	
	public static float[][] diamondSquare(float[][] map, int xSide, int ySide, float randomRange, float randomDiv){

		map = squareStep(map, xSide, ySide, randomRange);
		map = diamondStep(map, xSide, ySide, randomRange);
		
		if(xSide >= 4)
			map = diamondSquare(map, xSide/2, ySide/2, randomRange/randomDiv, randomDiv);
		
		
		return map;
	}
	
	public static float[][] squareStep(float[][] map, int xSide, int ySide, float randomRange){
		
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
	
	public static float[][] diamondStep(float[][] map, int xSide, int ySide, float randomRange){
		
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

