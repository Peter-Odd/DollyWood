package utilities;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * UtilityObject that will mostly help with converting a square map to a hexagonal map.
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */
public class HexagonUtils {
	
	private static boolean contains(ArrayList<int[]> list, int[] elem){
		for(int[] e : list){
			if(e[0] == elem[0] && e[1] == elem[1])
				return true;
		}
		return false;
	}
	
	private static void remove(ArrayList<int[]> list, int[] elem){
		Iterator<int[]> it = list.iterator();
		while(it.hasNext()){
			int[] e = it.next();
			if(e[0] == elem[0] && e[1] == elem[1]){
				it.remove();
				break;
			}
		}
	}

	/**
	 * Calculates neighboring points
	 * @param x X-centerPoint
	 * @param y Y-centerPoint
	 * @param radius The radius of the search pattern
	 * @return Returns a two-dimensional array containing all points that are within the radius of the point(x,y)
	 */
	public static ArrayList<int[]> neighborTiles(int x, int y, int radius, boolean includeCenter){
		x %= Globals.width;
		y %= Globals.height;
		if(x < 0)
			x = Globals.width - x-2;
		if(y < 0)
			y = Globals.height - y-2;
		ArrayList<int[]> tiles = new ArrayList<int[]>();
		tiles.add(new int[]{x, y});
		ArrayList<int[]> workingList = null;
		for(int i = radius; i > 0; i--){
			@SuppressWarnings("unchecked")
			ArrayList<int[]> tileClone = (ArrayList<int[]>) tiles.clone();
			for(int[] tile : tileClone){
				workingList = neighborTiles(tile[0], tile[1], false);
				for(int[] neighbors : workingList){
					if(!contains(tiles, neighbors))
						tiles.add(neighbors);
				}
			}
		}
		if(!includeCenter){
			remove(tiles, new int[]{x, y});
		}
		return tiles;
	}
	
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
			tileIterator[0] %= Globals.width;
			tileIterator[1] %= Globals.height;
			if(tileIterator[0] < 0)
				tileIterator[0] = Globals.width - tileIterator[0]-2;
			if(tileIterator[1] < 0)
				tileIterator[1] = Globals.height - tileIterator[1]-2;
		}
		return tiles;
	}

}