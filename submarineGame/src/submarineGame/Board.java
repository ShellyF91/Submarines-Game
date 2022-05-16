package submarineGame;

public class Board {
	
	public final int ROWS = 12; 
	public final int COLUMNS = 22; 
	public char [][] mat; 
	
	public Board() {
		mat = new char [ROWS][COLUMNS];
		initializeBoard();
		addFrame();
	}
	
	private void addFrame() { 
		for(int i = 0; i < ROWS; i++) //left column
			mat[i][0] = '.';
		for(int i = 0; i < ROWS; i++) //right column
			mat[i][COLUMNS-1] = '.';
		for(int i = 0; i < COLUMNS; i++) //upper row
			mat[0][i] = '.';
		for(int i = 0; i < COLUMNS; i++) //lower row
			mat[ROWS-1][i] = '.';
	}

	public void initializeBoard() {
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLUMNS; j++)
				mat[i][j] = '0';
		}
	} 
	
	public void printBoard() {
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLUMNS; j++)
				System.out.print(mat[i][j] + " ");
			System.out.println("");
		}
	}
	
	public void changeToPlayerBoard() {
		for(int i = 1; i < ROWS-1; i++) {
			for(int j = 1; j < COLUMNS-1; j++)
				mat[i][j] = '?'; 
		}
	}
	


}
