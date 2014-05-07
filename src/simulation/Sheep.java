package simulation;


public class Sheep extends Animal implements  Runnable{
	
	int xPos;
	int yPos;
	Race sheep;
	public Sheep(int xPos, int yPos, Race sheep){
		super();
		this.xPos = xPos;
		this.yPos = yPos;
		this.sheep = sheep;
	}	
	
	public void run(){
		while(true){
			try {
			    Thread.sleep(2000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			sheep.moveSpecies(xPos, yPos, xPos+1, yPos+1);
			xPos += 1;
			yPos += 1;
		}
	}
}
