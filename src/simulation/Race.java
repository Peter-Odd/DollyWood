package simulation;

import utilities.Globals;
import utilities.HexagonUtils;
import utilities.Needs;
import utilities.NeedsController;
import utilities.NeedsController.NeedsControlled;

public class Race implements NeedsControlled{
	private Animal[][] species;
	private String specName;

	public Race(String specName) {
		this.specName = specName;
		species = new Animal[Globals.height][Globals.width];

		if(specName == "Sheep"){
			NeedsController.registerNeed("Meat", this);
		}else if(specName == "Tree"){
			NeedsController.registerNeed("Water", this);
		}
	}

	public String getSpecies() {
		return specName;
	}

	public Animal getSpeciesAt(int x, int y) {
		return species[x][y];
	}

	public Animal getAndRemoveSpeciesAt(int x, int y){
		Animal animal = species[x][y];
		species[x][y] = null;
		return animal;
	}

	public boolean moveSpecies(int currentX, int currentY, int newX, int newY){

		if(species[newX][newY] == null){
			Animal animal = getAndRemoveSpeciesAt(currentX, currentY);
			species[newX][newY] = animal;
			return true;
		} else {
			return false;
		}
	}

	public boolean setSpeciesAt(int x, int y, Animal s) {
		if (species[x][y] == null) {
			species[x][y] = s;
			return true;
		} 
		else {
			return false;
		}
	}

	public boolean containsAnimal(int x, int y){
		if(species[x][y] != null){
			return true;
		} else {
			return false;
		}
	}


	@Override
	public float getNeed(Needs need, int x, int y) {
		if(need.getNeed().equals("Meat") && containsAnimal(x, y)){
			getAndRemoveSpeciesAt(x, y);
			return 1.0f;
		} else if (need.getNeed().equals("Water")) {
			for (int[] neighbor : HexagonUtils.neighborTiles(x, y, true)) {
				if (containsAnimal(neighbor[0], neighbor[1])) {
					return -0.2f;
				}
			}
			return 0.0f;
		} 
		return 0.0f;
	}

}

