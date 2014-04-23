package utilities;

import java.util.Random;

public class Fractal {
	static Random r = new Random();
	
	public static float getRandom(float max, float min){
		return r.nextFloat() * (max-min) + min;
	}
	
	public static float[][] generateFractal(float[][] map, float max, float min){
		
		map = intitateCorners(map, max, min);
		map = diamondSquare(map, map.length-1, map.length-1, 0.0f);
		
		return map;
	}
	
	public static float[][] intitateCorners(float[][] map, float max, float min){
		int largestX = map.length - 1;
		int largestY = map[0].length - 1;
		
		map[0][0] = getRandom(max, min);
		map[largestX][0] = getRandom(max, min);
		map[largestX][largestY] = getRandom(max, min);
		map[0][largestY] = getRandom(max, min);

		return map;
	}
	
	public static float[][] diamondSquare(float[][] map, int xHalfSide, int yHalfSide, float randomRange){

		map = diamondStep(map, xHalfSide, yHalfSide, randomRange);
		map = squareStep(map, xHalfSide, yHalfSide, randomRange);
		
		if(xHalfSide >= 2){
			diamondSquare(map, xHalfSide/2, yHalfSide/2, randomRange/2);
		} else
			return map;
		
		return map;
	}
	
	public static float[][] diamondStep(float[][] map, int xHalfSide, int yHalfSide, float randomRange){
		
		float center = 0;
		
		for(int x=xHalfSide; x<map.length-1;x+=xHalfSide){
			for(int y=yHalfSide;y<map[0].length-1;y+=yHalfSide){
				if(map[x][y] == 0){
					center = ((map[x + xHalfSide][y + yHalfSide] +
							 map[x + xHalfSide][y - yHalfSide] + 
							 map[x - xHalfSide][y + yHalfSide] + 
							 map[x - xHalfSide][y - yHalfSide])/4.0f);// +
							 //5.0f;//getRandom(randomRange*2, 0) - randomRange;
					
					if(center > 255.0f)
						center = 250.0f;
					
					if(center < 0.0f)
						center = 0.0f;
					
					map[x][y] = center;
				}
			}
		}
			
		return map;
	}
	
	public static float[][] squareStep(float[][] map, int xHalfSide, int yHalfSide, float randomRange){
		
		float value = 0;
		float elements = 0.0f;
		
		for(int x=0;x<map.length;x+=xHalfSide){
			for(int y=0;y<map[0].length;y+=yHalfSide){
				
				if(map[x][y] == 0.0){
					elements = 0;
					value = 0;
					if((x - xHalfSide) >= 0){
						value += map[x - xHalfSide][y];
						elements++;
					}
					if((x + xHalfSide) < map.length){
						value += map[x + xHalfSide][y];
						elements++;
					}
					if((y - yHalfSide) >= 0){
						value += map[x][y - yHalfSide];
						elements++;
					}
					if((y + yHalfSide) < map[0].length){
						value += map[x][y + yHalfSide];
						elements++;
					}
					
					if(elements != 0.0f)					
					value = (value/elements);// + 5.0f;//getRandom(randomRange, 0) - randomRange;
					
					if(value > 255.0f)
						value = 255.0f;
					
					if(value < 0.0f)
						value = 0.0f;
						
					map[x][y] = value;
				}
			}
		}
		
	return map;
		
	}		

}

