package utilities;

/**
 * A need that holds the string name of the need, and a value of said need.
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */
public class Needs{
	private String need;
	private float ammount;
	public Needs(String need, float f){
		this.need = need;
		this.ammount = f;
	}
	public String getNeed() {
		return need;
	}
	public void setNeed(String need) {
		this.need = need;
	}
	public float getAmmount() {
		return ammount;
	}
	public void setAmmount(float ammount) {
		this.ammount = ammount;
	}
}