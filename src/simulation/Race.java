package simulation;

import java.util.Map;

import utilities.Globals;

public class Race<Species> {
	private Species[][] species;
	private String specName;

	@SuppressWarnings("unchecked")
	public Race(String specName) {
		this.specName = specName;
		species = (Species[][])new Map[Globals.width][Globals.height];
	}

	public String getSpecies() {
		return specName;
	}

	public Species getSpeciesAt(int x, int y) {
		return species[x][y];
	}

	public boolean setSpeciesAt(int x, int y, Species s) {
		if (species[x][y] != null) {
			species[x][y] = s;
			return true;
		} 
		else {
			return false;
		}
	}
	
	

}

