package game;

import java.util.Random;

public class Minesweeper 
{                                                        
	public final static int SIZE = 10;
	public static enum CellState {UNEXPOSED, EXPOSED, SEALED};
	public static enum GameStatus {INPROGRESS, WON, LOST};

	protected CellState[][] cellStates = new CellState[SIZE][SIZE];
	private boolean[][] mines = new boolean[SIZE][SIZE];


	public Minesweeper(){
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++) {
				cellStates[i][j] = CellState.UNEXPOSED;
			}
		}
	}

	public void exposeCell(int row, int column) {   
		if (cellStates[row][column] == CellState.UNEXPOSED) {
			cellStates[row][column] = CellState.EXPOSED;
			if (!isAdjacent(row, column) && (!isMined(row, column))) 
			{
				exposeNeighbors(row, column);
			}
		}
	}

	void exposeNeighbors(int row, int column){	
		final int MAX = SIZE - 1;
		int minRow = (row == 0) ? 0 : row - 1;
		int maxRow = (row == MAX) ? MAX : row + 1;
		int minColumn = (column == 0) ? 0 : column - 1;
		int maxColumn = (column == MAX) ? MAX : column + 1;

		for (int rowNeighbor = minRow; rowNeighbor <= maxRow; rowNeighbor++){
			for (int columnNeighbor = minColumn; columnNeighbor <= maxColumn; columnNeighbor++){
				exposeCell(rowNeighbor, columnNeighbor);
			}
		}
	}

	public CellState getCellStatus(int row, int column){
		return cellStates[row][column];
	}

	public void toggleSeal(int row, int column) {
		if (cellStates[row][column] == CellState.SEALED)
			cellStates[row][column] = CellState.UNEXPOSED;
		else if (cellStates[row][column] == CellState.UNEXPOSED)
			cellStates[row][column] = CellState.SEALED;
	}

	boolean isMined(int row, int column){
		return mines[row][column];
	}

	public void setMine(int row, int column){
		mines[row][column] = true;
	}

	public boolean isAdjacent(int row, int column){
		final int MAX = SIZE - 1;
		int minRow = (row == 0) ? 0 : row - 1;
		int maxRow = (row == MAX) ? MAX : row + 1;
		int minColumn = (column == 0) ? 0 : column - 1;
		int maxColumn = (column == MAX) ? MAX : column + 1;
		if(mines[row][column] == true)
			return false;

		for (int rowNeighbor = minRow; rowNeighbor <= maxRow; rowNeighbor++){
			for (int columnNeighbor = minColumn; columnNeighbor <= maxColumn; columnNeighbor++){
				if (isMined(rowNeighbor, columnNeighbor))
					return true;
			}
		}
		return false;
	}

	public void placeMines(int numberOfMines){
		Random r = new Random();
		int row = 0; 
		int column = 0;
		int mineCounter = 0;
		while(mineCounter < numberOfMines)
		{
			row = r.nextInt(9);
			column = r.nextInt(9);
			if(mines[row][column] == true)
				continue;
			setMine(row, column);
			mineCounter++;
		}
	}

	public boolean[][] getMines(){
		return mines;
	}

	public GameStatus getGameStatus(){
		int sealedMines = 0;
		int exposedCells = 0;
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				if(cellStates[i][j] == CellState.EXPOSED && isMined(i, j))
					return GameStatus.LOST;
				if(cellStates[i][j] == CellState.EXPOSED)
					exposedCells++;
				if(cellStates[i][j] == CellState.SEALED && isMined(i, j))
					sealedMines++;
			}
		}
		if( exposedCells == 90 && sealedMines == 10)
			return GameStatus.WON;
		return GameStatus.INPROGRESS;
	}


	public int numberOfMinesAdjacent(int row, int column){
		int numbersOfMinesAdjacent = 0;
		final int MAX = SIZE - 1;
		int minRow = (row == 0) ? 0 : row - 1;
		int maxRow = (row == MAX) ? MAX : row + 1;
		int minColumn = (column == 0) ? 0 : column - 1;
		int maxColumn = (column == MAX) ? MAX : column + 1;

		for (int rowNeighbor = minRow; rowNeighbor <= maxRow; rowNeighbor++){
			for (int columnNeighbor = minColumn; columnNeighbor <= maxColumn; columnNeighbor++){
				if (isMined(rowNeighbor, columnNeighbor))
					numbersOfMinesAdjacent++;
			}
		}
		return numbersOfMinesAdjacent;
	}
}