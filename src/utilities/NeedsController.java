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
	
	public static ArrayList<NeedsControlled> getNeed(String need){
		return needs.get(need);
	}
	
	/**
	 * Don't know yet. TBI
	 * @author OSM Group 5 - DollyWood project
	 * @version 1.0
	 */
	public class Needs{
		private String need;
		private int ammount;
		public Needs(String need, int ammount){
			this.need = need;
			this.ammount = ammount;
		}
		public String getNeed() {
			return need;
		}
		public void setNeed(String need) {
			this.need = need;
		}
		public int getAmmount() {
			return ammount;
		}
		public void setAmmount(int ammount) {
			this.ammount = ammount;
		}
	}
	
	//TODO implement this class!
	public interface NeedsControlled{
		
	}
}
