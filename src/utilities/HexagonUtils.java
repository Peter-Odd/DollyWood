package utilities;

import java.util.ArrayList;

/**
 * UtilityObject that will mostly help with converting a square map to a hexagonal map.
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */
public class HexagonUtils {
	/**
	 * Calculates neighboring points
	 * @param x X-centerPoint
	 * @param y Y-centerPoint
	 * @return Returns a two-dimensional array containing all points that touch (x,y) in a hexagonal grid
	 */
	public static ArrayList<int[]> neighborTiles(int x, int y, boolean includeCenter) {
		ArrayList<int[]> tiles = new ArrayList<>();
		int[] tile = new int[2];
		//Y-Tiles
		tile[0] = x;
		tile[1] = y+1;
		tiles.add(tile);
		tile = new int[2];
		tile[0] = x;
		tile[1] = y-1;
		tiles.add(tile);
		
		//X-Tiles (offset)
		tile = new int[2];
		tile[0] = x+1;
		tile[1] = y+(x%2);
		tiles.add(tile);
		tile = new int[2];
		tile[0] = x-1;
		tile[1] = y+(x%2);
		tiles.add(tile);
		
		//X-Tiles (no-offset)
		tile = new int[2];
		tile[0] = x+1;
		tile[1] = y+(x%2)-1;
		tiles.add(tile);
		tile = new int[2];
		tile[0] = x-1;
		tile[1] = y+(x%2)-1;
		tiles.add(tile);
		if(includeCenter){
			tile = new int[2];
			tile[0] = x;
			tile[1] = y;
			tiles.add(tile);
		}
		for(int[] tileIterator : tiles){
			/*tileIterator[0] %= Globals.width;
			tileIterator[1] %= Globals.height;
			if(tileIterator[0] < 0)
				tileIterator[0] = Globals.width - tileIterator[0]-2;
			if(tileIterator[1] < 0)
				tileIterator[1] = Globals.height - tileIterator[1]-2;*/
			tileIterator[0] %= AstarDriver.width;
			tileIterator[1] %= AstarDriver.height;
			if(tileIterator[0] < 0)
				tileIterator[0] = AstarDriver.width - tileIterator[0]-2;
			if(tileIterator[1] < 0)
				tileIterator[1] = AstarDriver.height - tileIterator[1]-2;
		}
		return tiles;
	}

}