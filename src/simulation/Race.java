package simulation;


import utilities.Globals;

public class Race {
	private Animal[][] species;
	private String specName;

	public Race(String specName) {
		this.specName = specName;
		species = new Animal[Globals.height][Globals.width];
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
		Animal animal = getAndRemoveSpeciesAt(currentX, currentY);
		if(species[newX][newY] == null){
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
	
}

