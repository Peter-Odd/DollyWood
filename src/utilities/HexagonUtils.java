package utilities;
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
	public static int[][] neighborTiles(int x, int y) {
		int[][] tiles = new int[6][2];
		//Y-Tiles
		tiles[0][0] = x;
		tiles[0][1] = y+1;
		tiles[1][0] = x;
		tiles[1][1] = y-1;
		
		//X-Tiles (offset)
		tiles[2][0] = x+1;
		tiles[2][1] = y+(x%2);
		tiles[3][0] = x-1;
		tiles[3][1] = y+(x%2);
		
		//X-Tiles (no-offset)
		tiles[4][0] = x+1;
		tiles[4][1] = y+(x%2)-1;
		tiles[5][0] = x-1;
		tiles[5][1] = y+(x%2)-1;
		return tiles;
	}

}