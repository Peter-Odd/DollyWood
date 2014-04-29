package simulation;

import utilities.Globals;

public class Race {
	private Animal[][] species;
	private String specName;

	public Race(String specName) {
		this.specName = specName;
		species = new Animal[Globals.width][Globals.height];
	}

	public String getSpecies() {
		return specName;
	}

	public Animal getSpeciesAt(int x, int y) {
		return species[x][y];
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
}