package submarineGame;

import java.util.Random;

public class Submarine {
	
	public static Random random = new Random(); 
	
	private final int MAX_SIZE = 4;
	private int size;
	private Coordinate [] submarineLocation; 
	
	public Submarine(){
		setSize(); 
		setSubmarineLocation(); 
	}
	
	private void setSize() {
		size = random.nextInt(MAX_SIZE)+1;
//		size = 2; 
	}
	
	private void setSubmarineLocation(){
		submarineLocation = new Coordinate[size];
		//initiate to (0,0)
		for(int i = 0; i < submarineLocation.length; i++) {
			submarineLocation[i] = new Coordinate(0,0);
		}
	}
	
	public int getSize() {
		return size;
	}
	
	public Coordinate [] getSubmarineLocation() {
		return submarineLocation;
	}
	
	public void addLocation(Coordinate newLocation, int i) {
		submarineLocation[i] = newLocation;
	}

}
