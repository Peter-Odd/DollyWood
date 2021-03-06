package utilities;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * NeedsController will be the handle to all needs.
 * Any object will be able to use this class to get an object that will fulfill their needs.
 * The result of this is that the object asking does not need to know what it is receiving, since it know that it is capable of fulfilling a specific need.
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */
public class NeedsController {
	
	private static HashMap<String, ArrayList<NeedsControlled>> needs = new HashMap<String, ArrayList<NeedsControlled>>();
	
	/**
	 * Clears the list of needs.
	 * Be careful using this function as some other classes might depend on a full needs list.
	 * The function was made for test cases that needed resetting between tests.
	 */
	public static void reset(){
		needs = new HashMap<>();
	}
	
	/**
	 * Registers a controller as a provider of a specific need
	 * @param need The need that is provided
	 * @param ref The controller that provides
	 */
	public static void registerNeed(String need, NeedsControlled ref){
		if(needs.containsKey(need)){
			needs.get(need).add(ref);
		}
		else{
			ArrayList<NeedsControlled> arrayList = new ArrayList<NeedsControlled>();
			arrayList.add(ref);
			needs.put(need, arrayList);
		}
	}
	
	/**
	 * Returns a list of NeedsControlled objects capable of fulfilling the need, empty list if none found
	 * @param need The need that is searched for
	 * @return A list of NeedsControlled objects.
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<NeedsControlled> getNeed(String need){
		if(needs.containsKey(need))
			return (ArrayList<NeedsControlled>) needs.get(need).clone();
		else
			return new ArrayList<NeedsControlled>();
	}
	
	public interface NeedsControlled{
		//This will kill a sheep and return 1.0f if the sheep is an adult. if called on grass, it will return however much grass there is on that tile and remove that tile of grass
		public float getNeed(Needs need, int x, int y);
		public float peekNeed(Needs need, int x, int y);
	}
}
