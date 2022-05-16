package submarineGame;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Game {
	Scanner scanner = new Scanner(System.in);
	public Random random = new Random(); 
	
	private Board board;
	private Board playerBoard;
	private Submarine [] submarines = new Submarine[5];
	public int playerPoints; 
	public final int NUMBER_OF_GUESSES = 100;

	public Game() {
		board = new Board(); 
		LocateSubmarinesOnBoard();
		//player
		playerPoints = 1000; 
		playerBoard = new Board();
		playerBoard.changeToPlayerBoard();
	}

	public void play() {
		printStart();
		boolean isConsecutive = false;
		for(int i = 1; i <= NUMBER_OF_GUESSES && playerPoints > 0; i++) {
			isConsecutive = takeAGuess(i, isConsecutive);
			playerBoard.printBoard();
			printStatus(i); 
		}
		System.out.println("game Over!");
	}

	public void printStart(){
		System.out.println("********** Sunk The Submarine **********");
		System.out.println("");
		System.out.println("welcome! You have "+ NUMBER_OF_GUESSES +" guesses to find the 5 submarines on the board.");
		System.out.println("you have " + playerPoints + " points to begin with.");
		System.out.println("");
	}
	

	public boolean takeAGuess(int i, boolean isConsecutive) throws InputMismatchException{
		boolean result;
		System.out.println("########## guess number " + i + " ##########");
		System.out.println("please guess the row and column.");
		System.out.println("enter your guess for the row (1-10):");
		int x = 0, y = 0; 
		x = scanner.nextInt();
		scanner.nextLine();
		System.out.println("enter your guess for the column (1-20):");
		y = scanner.nextInt();
		scanner.nextLine();

		if(board.mat[x][y] == '1' || board.mat[x][y] == '2' || board.mat[x][y] == '3' ||
				   board.mat[x][y] == '4' || board.mat[x][y] == '5') {
					result = true;
					playerBoard.mat[x][y]= 'H';
					playerPoints = isConsecutive? playerPoints + 1000 : playerPoints + 200; 
				}
				else {
					result = false;
					playerBoard.mat[x][y]= 'm';
					playerPoints -= 10; 
				}
		return result;
	}
	
	
	private void printStatus(int i) {
		System.out.println("");
		System.out.println("you have " + playerPoints + " points. ");	
		System.out.println((100-i) + " guesses are left.");
		System.out.println("");
//		System.out.println("press 'q' to quit, any other key to keep going:");
//		char decision = '1';
//		Scanner scanner = new Scanner(System.in);
//		decision = (char)scanner.nextByte(); 
//		scanner.close();
//		if(decision == 'q') {
//			System.out.println("Goodbye!");
//			return false;
//		}
//		else
//			return true; 
	}


	/**
	 * the main function that responsible for locating the submarines on the board, step by step.
	 * it's called from the constructor, as part of making a new 'Game' instance. 
	 */
	private void LocateSubmarinesOnBoard() {
		boolean isPossibleAndLocated;
		Coordinate randomBoxOnBoard;
		int j; 
		//looping all the submarines
		for(int i = 0; i < submarines.length; i++) {
			isPossibleAndLocated = false;
			j = 0; 
			while( ! isPossibleAndLocated && j < 100) {
				//choose a random free box
			//TODO k to prevent infinite loop + to check it, and j the same
				do {
					randomBoxOnBoard = chooseRandomBoxOnBoard();
					System.out.println("chosen random box: " + randomBoxOnBoard.getX() + " , " + randomBoxOnBoard.getY());
				}
				while( ! isBoxFree(randomBoxOnBoard));
				System.out.println("is box free = " + isBoxFree(randomBoxOnBoard));
				//create a submarine of random size
				submarines[i] = new Submarine();
				System.out.println("submarine size: " + submarines[i].getSize());
				//try to locate the submarine in the box - 'true' means it is possible and also located. 
				isPossibleAndLocated = isPossibleToLocateSubmarineOnBox(submarines[i],randomBoxOnBoard, i);
				if( ! isPossibleAndLocated)
					deleteSubmarineFromBoard(submarines[i]);
				j++;
			}
		}
	}


	private boolean isPossibleToLocateSubmarineOnBox(Submarine submarine, Coordinate randomBoxOnBoard, int submarineIndex) {
		Coordinate currBox = randomBoxOnBoard;
		char submarineIndexC = (char) (submarineIndex + '0');
		//put the first brick on the free box, and save the coordinate in the submarine's data
		board.mat[currBox.getX()][currBox.getY()] = submarineIndexC;
		submarine.addLocation(currBox,0);
		boolean isNeighborBoxFree = false;
		int j;
		Coordinate neighborBox;
		for(int i = 1; i < submarine.getSize(); i++) {
			j = 0;
			while( ! isNeighborBoxFree && j < 50) {
				neighborBox = chooseRandomNeighborBox(currBox);
				isNeighborBoxFree = isBoxFree(neighborBox);
				if(isNeighborBoxFree) {
					board.mat[currBox.getX()][currBox.getY()] = submarineIndexC;
					submarine.addLocation(neighborBox, i);
				}
				j++;
			}
			//if out of while loop and no neighbor box was founded
			if( ! isNeighborBoxFree)
				return false;
		}
		markSubmarineBorders(submarine.getSubmarineLocation());
		return true;
	}

	private void deleteSubmarineFromBoard(Submarine submarine) {
		for(int i = 0; i < submarine.getSubmarineLocation().length; i++) {
			board.mat[submarine.getSubmarineLocation()[i].getX()][submarine.getSubmarineLocation()[i].getY()] = '0'; 
			submarine.getSubmarineLocation()[i] = null; 
		}
	}
	
	private Coordinate chooseRandomNeighborBox(Coordinate currBox) {
		Coordinate neighborBox = null; 
		int option = random.nextInt(4)+1;
		// match between an int option and a neighbor box
		switch(option) {
		case 1: 
			neighborBox = new Coordinate(currBox.getX()-1, currBox.getY());
			break;
		case 2: 
			neighborBox = new Coordinate(currBox.getX(), currBox.getY()+1);
			break;
		case 3:
			neighborBox = new Coordinate(currBox.getX()+1, currBox.getY());
			break;
		case 4: 
			neighborBox = new Coordinate(currBox.getX(), currBox.getY()-1);
			break;
//		default:
//			neighborBox = null;
		}
		return neighborBox;
	}
	
	private Coordinate chooseRandomBoxOnBoard() {
		//creates a random box on the board, excluding the frame of dots
		int x = random.nextInt(10)+1;
		int y = random.nextInt(20)+1;
		Coordinate box = new Coordinate(x,y); 
		return box;
	}
	
	
	/**
	 * checks the box in the board's matrix, to decide if a submarine's brick can be added there.
	 * @param Coordinate box
	 * @return true if the box is free, false if it's not. "free" means that the value of the box 
	 * is '0', and the values of the surrounding boxes are '0' or '.' 
	 */
	private boolean isBoxFree(Coordinate box) {
		System.out.println("x of box in isBoxFree: " + box.getX());
		System.out.println("y of box in isBoxFree:: " + box.getY());
		//check if it's a frame
		if(board.mat[box.getX()][box.getY()] == '.')
			return false;
		//checks the box itself
		if(board.mat[box.getX()][box.getY()] != '0')
			return false;
		//checks the upper box 
		if(board.mat[box.getX()-1][box.getY()] != '0' && board.mat[box.getX()-1][box.getY()] != '.')
			return false;
		//checks the lower box 
		if(board.mat[box.getX()+1][box.getY()] != '0' && board.mat[box.getX()-1][box.getY()] != '.')
			return false;
		//checks the left box
		if(board.mat[box.getX()][box.getY()-1] != '0' && board.mat[box.getX()-1][box.getY()] != '.')
			return false;
		//checks the right box
		if(board.mat[box.getX()][box.getY()+1] != '0' && board.mat[box.getX()-1][box.getY()] != '.')
			return false;
		
		return true; 
	}
	
	//borders handling
	
	private void markSubmarineBorders(Coordinate[] submarineLocation) {
		int x, y; 
		System.out.println("length of submarines location array: " + submarineLocation.length);
		for(int i = 0; i < submarineLocation.length; i++) {
			System.out.println("i = " + i);
			x = submarineLocation[i].getX();
			y = submarineLocation[i].getY();
			if(board.mat[x-1][y] == '0')
				board.mat[x-1][y] = 'X'; 
			if(board.mat[x][y+1] == '0')
				board.mat[x][y+1] = 'X'; 
			if(board.mat[x+1][y] == '0')
				board.mat[x+1][y] = 'X'; 
			if(board.mat[x][y-1] == '0')
				board.mat[x][y-1] = 'X'; 
		}
	}
	
//	private void deleteSubmarineBorders(Coordinate[] submarineLocation) {
//		int x, y; 
//		for(int i = 0; i < submarineLocation.length; i++) {
//			x = submarineLocation[i].getX();
//			y = submarineLocation[i].getY();
//			if(board.mat[x-1][y] == 'X')
//				board.mat[x][y] = '0'; 
//			if(board.mat[x][y+1] == 'X')
//				board.mat[x][y] = '0'; 
//			if(board.mat[x+1][y] == 'X')
//				board.mat[x][y] = '0'; 
//			if(board.mat[x][y-1] == 'X')
//				board.mat[x][y] = '0'; 
//		}
//	}


}
