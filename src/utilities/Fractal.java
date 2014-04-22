package utilities;

import java.util.Random;

public class Fractal {
	
	
	public static float getRandom(float max, float min){
		Random r = new Random();
		return r.nextFloat()/10 * max + min;
	}
	
	public static float[][] generateFractal(float[][] map, float min, float max){
		
		map = intitateCorners(map, max, min);
		
		return map;
	}
	
	public static float[][] intitateCorners(float[][] map, float min, float max){
		int largestX = map.length - 1;
		int largestY = map[0].length - 1;
		
		map[0][0] = getRandom(max, min);
		map[largestX][0] = getRandom(max, min);
		map[largestX][largestY] = getRandom(max, min);
		map[0][largestY] = getRandom(max, min);

		return map;
	}
	
	public static float[][] diamondStep(float[][] map, int xMax, int xMin, int yMax, int yMin){
		
		float upperLeft = map[xMin][yMax];
		float upperRight = map[xMax][yMax];
		float lowerLeft = map[xMin][yMin];
		float lowerRight = map[xMax][yMin];	
		float center = (upperLeft + upperRight + lowerLeft + lowerRight) / 4;
		
		// Assign center point of the square
		map[(xMax-xMin)/2][(yMax-yMin)/2] = center;
		
		return map;
	}
	
	public static float[][] squareStep(float[][] map, int xMax, int xMin, int yMax, int yMin){
		
		float upperLeft = map[xMin][yMax];
		float upperRight = map[xMax][yMax];
		float lowerLeft = map[xMin][yMin];
		float lowerRight = map[xMax][yMin];
		float center = (upperLeft + upperRight + lowerLeft + lowerRight) / 4;
		int xHalfSide = (xMax - xMin)/2;
		int yHalfSide = (yMax - yMin)/2;
		
		if(xMax == map.length-1 && yMax == map[0].length-1){
			if(xMin == 0 && yMin == 0){
				map[xMin][yHalfSide] = upperLeft + lowerLeft + center;
				map[xHalfSide][yMin] = upperLeft + upperRight + center;
			} else{
				map[xMin][yHalfSide] = upperLeft + lowerLeft + center + map[xMin - xHalfSide][yHalfSide];
				map[xHalfSide][yMin] = upperLeft + upperRight + center + map[xHalfSide][yMin - yHalfSide];
			}
			map[xHalfSide][yMax] = lowerLeft + lowerRight + center;
			map[xMax][yHalfSide] = upperRight + lowerRight + center;
		} else if(xMax == map.length-1 && yMin == 0){
			map[xMin][yHalfSide] = upperLeft + lowerLeft + center + map[xMin - xHalfSide][yHalfSide];
			map[xMax][yHalfSide] = upperRight + lowerRight + center;
			map[xHalfSide][yMin] = lowerRight + lowerLeft + center + map[xHalfSide][yMin - yHalfSide];
			map[xHalfSide][yMax] = upperRight + lowerRight + center;
		} else if(xMin == 0 && yMax == map[0].length){
			//map[][]
		}
		
		return map;
	}
	
	public static void print(float[][] map){
		for(int i=0;i<map.length;i++){
			for(int n=0;n<map.length-1;n++){
				System.out.print(map[i][n] + "|");
			}
			System.out.println(map[i][map.length-1]);
		}
	}
	
	
}

