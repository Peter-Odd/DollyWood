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
	public static int[][] neighborTiles(int x, int y, boolean includeCenter) {
		int[][] tiles;
		if(!includeCenter)
			tiles = new int[6][2];
		else
			tiles = new int[7][2];
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
		if(includeCenter){
			tiles[6][0] = x;
			tiles[6][1] = y;
		}
		for(int i = 0; i < tiles.length; i++){
			tiles[i][0] %= Globals.width;
			tiles[i][1] %= Globals.height;
			if(tiles[i][0] < 0)
				tiles[i][0] = Globals.width - tiles[i][0] - 1;
			if(tiles[i][1] < 0)
				tiles[i][1] = Globals.height - tiles[i][1] - 1;
		}
		return tiles;
	}

}