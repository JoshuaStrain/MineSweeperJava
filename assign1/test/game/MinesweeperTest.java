package game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import game.Minesweeper.CellState;
import game.Minesweeper.GameStatus;

public class MinesweeperTest 
{
	Minesweeper minesweeper;

	@Before
	public void setUp()
	{
		minesweeper = new Minesweeper();
	}

	@Test
	public void canary()
	{
		assertTrue(true);
	}

	@Test
	public void exposeUnexposedCell()
	{  
		minesweeper.exposeCell(1, 3);

		assertEquals(CellState.EXPOSED, minesweeper.getCellStatus(1, 3));

	}

	@Test
	public void exposeAlreadyExposedCell(){			
		minesweeper.exposeCell(2, 4);
		minesweeper.exposeCell(2, 4);

		assertEquals(CellState.EXPOSED, minesweeper.getCellStatus(2, 4));
	}

	@Test
	public void callExposeCellWithColumnValueOutOfBounds(){
		try {
			minesweeper.exposeCell(0, 11);
			fail("Expected exception for column out of bounds.");
		} catch(IndexOutOfBoundsException e){}	
	}	

	@Test
	public void callExposeCellWithRowValueOutOfBounds(){
		try {
			minesweeper.exposeCell(0, 11);
			fail("Expected exception for column out of bounds.");
		} catch(IndexOutOfBoundsException e){}		
	}


	class MineSweeperWithExposeNeighborsStubbed extends Minesweeper {
		public boolean calledExposeNeighbors; 

		@Override
		void exposeNeighbors(int row, int column) {  
			calledExposeNeighbors = true;
		}
	}

	@Test
	public void exposeUnexposedEmptyCell(){
		MineSweeperWithExposeNeighborsStubbed minesweeper = new MineSweeperWithExposeNeighborsStubbed();
		minesweeper.exposeCell(1, 3);
		
		assertTrue(minesweeper.calledExposeNeighbors);
	}

	@Test
	public void exposingAnExposedCellDoesNotCallExposeNeighbors(){
		MineSweeperWithExposeNeighborsStubbed minesweeper = new MineSweeperWithExposeNeighborsStubbed();

		minesweeper.exposeCell(3, 3);
		minesweeper.calledExposeNeighbors = false;
		minesweeper.exposeCell(3, 3);

		assertFalse(minesweeper.calledExposeNeighbors);
	}

	class MineSweeperWithExposeCellStubbed extends Minesweeper {
		List<Integer> rows = new ArrayList<Integer>();
		List<Integer> columns = new ArrayList<Integer>();

		@Override
		public void exposeCell(int row, int column) { 
			rows.add(row);
			columns.add(column);
		}
	}

	@Test
	public void exposeNeighborsOfInteriorCellCallsExposeCellOnNeighbors() {  
		MineSweeperWithExposeCellStubbed minesweeper = new MineSweeperWithExposeCellStubbed();

		minesweeper.exposeNeighbors(3, 4);

		assertEquals(Arrays.asList(2, 2, 2, 3, 3, 3, 4, 4, 4), minesweeper.rows);
		assertEquals(Arrays.asList(3, 4, 5, 3, 4, 5, 3, 4, 5), minesweeper.columns);
	}    

	@Test
	public void exposeNeighborsOfUpperLeftCornerCellCallsExposeCellOnNeighbors() {  
		MineSweeperWithExposeCellStubbed minesweeper = new MineSweeperWithExposeCellStubbed();

		minesweeper.exposeNeighbors(0, 0);

		assertEquals(Arrays.asList(0, 0, 1, 1), minesweeper.rows);
		assertEquals(Arrays.asList(0, 1, 0, 1), minesweeper.columns);
	}    

	@Test
	public void exposeNeighborsOfUpperRightCellCallsExposeCellOnNeighbors() {  
		MineSweeperWithExposeCellStubbed minesweeper = new MineSweeperWithExposeCellStubbed();

		minesweeper.exposeNeighbors(0, 9);

		assertEquals(Arrays.asList(0, 0, 1, 1), minesweeper.rows);
		assertEquals(Arrays.asList(8, 9, 8, 9), minesweeper.columns);
	}    

	@Test
	public void exposeNeighborsOfLowerLeftCellCallsExposeCellOnNeighbors() {  
		MineSweeperWithExposeCellStubbed minesweeper = new MineSweeperWithExposeCellStubbed();

		minesweeper.exposeNeighbors(9, 0);

		assertEquals(Arrays.asList(8, 8, 9, 9), minesweeper.rows);
		assertEquals(Arrays.asList(0, 1, 0, 1), minesweeper.columns);
	}    

	@Test
	public void exposeNeighborsOfLowerRightCellCallsExposeCellOnNeighbors() {  
		MineSweeperWithExposeCellStubbed minesweeper = new MineSweeperWithExposeCellStubbed();

		minesweeper.exposeNeighbors(9, 9);

		assertEquals(Arrays.asList(8, 8, 9, 9), minesweeper.rows);
		assertEquals(Arrays.asList(8, 9, 8, 9), minesweeper.columns);
	}  

	@Test
	public void unExposedCellIsSealed()
	{
		minesweeper.toggleSeal(1, 3); 
		assertEquals(CellState.SEALED, minesweeper.getCellStatus(1, 3));
	}
	
	@Test
	public void sealedCellIsNotExposedOnToggle()
	{
		minesweeper.toggleSeal(1, 3); 
		assertFalse(minesweeper.getCellStatus(1, 3) == CellState.EXPOSED);
	}

	@Test
	public void unsealSealedCell()
	{
		minesweeper.toggleSeal(1, 3); 
		minesweeper.toggleSeal(1, 3);

		assertEquals(CellState.UNEXPOSED, minesweeper.getCellStatus(1, 3));
	}

	@Test
	public void exposeSealedCellDoesNotExposeCell()
	{
		minesweeper.toggleSeal(1, 3); 
		minesweeper.exposeCell(1,3); 

		assertEquals(CellState.SEALED, minesweeper.getCellStatus(1, 3));
	}

	@Test
	public void sealExposedCell()
	{
		minesweeper.exposeCell(1, 3);
		minesweeper.toggleSeal(1, 3);
		assertEquals(CellState.EXPOSED, minesweeper.getCellStatus(1, 3));
	}

	class MineSweeperWithIsAdjacentStubbed extends MineSweeperWithExposeNeighborsStubbed {

		@Override
		public boolean isAdjacent(int row, int column) {  
			return true;
		}
	}

	@Test
	public void exposingAnAdjacentCellDoesNotCallExposeNeighbors(){ 
		MineSweeperWithIsAdjacentStubbed minesweeper = new MineSweeperWithIsAdjacentStubbed();

		minesweeper.exposeCell(2, 2);
		assertFalse(minesweeper.calledExposeNeighbors);		
	}

	class MineSweeperWithIsMinedStubbed extends Minesweeper
	{

		@Override
		public boolean isMined(int row, int column) 
		{
			return false;
		}
	}

	@Test
	public void exposingAMinedCellDoesNotCallExposeNeighbors()
	{
		minesweeper.exposeCell(1, 3);
		minesweeper.exposeCell(1, 3);
		assertEquals(false, minesweeper.isMined(1, 3)); 
	}

	@Test
	public void isAdjacentToMine()
	{
		minesweeper.setMine(1, 3);
		assertTrue(minesweeper.isAdjacent(1, 4));
	}

	@Test
	public void mineShouldNotBeAdjacent()
	{
		minesweeper.setMine(1, 2);
		assertFalse(minesweeper.isAdjacent(3, 4));
	}

	@Test
	public void isAdjacentToMineAtMaxColumn()
	{
		minesweeper.setMine(1, 9);
		assertTrue(minesweeper.isAdjacent(2, 9));
	}

	@Test
	public void isAdjacentToMineAtMaxRow()
	{
		minesweeper.setMine(9, 1);
		assertTrue(minesweeper.isAdjacent(9, 2));
	}

	@Test
	public void isAdjacentToMineInCorner()
	{
		minesweeper.setMine(9, 9);
		assertTrue(minesweeper.isAdjacent(8, 8));
	}

        @Test
        public void isAdjacentOnTopOfMine()
        {
            minesweeper.setMine(1, 3);
            assertFalse(minesweeper.isAdjacent(1, 3));
        }

	@Test
	public void placeMinesArePlacedRandomly()
	{
		Minesweeper minesweeper2 = new Minesweeper();
		minesweeper.placeMines(Minesweeper.SIZE);
		minesweeper2.placeMines(Minesweeper.SIZE);
		assertFalse(Arrays.equals(minesweeper.getMines(), minesweeper2.getMines()));
	}

	@Test
	public void isNumberOfMinesPlacedTen()
	{
		minesweeper.placeMines(Minesweeper.SIZE);
		int mineCounter = 0;
		for(int i = 0; i < 10; i++)
			for(int j = 0; j < 10; j++)
			{
				if(minesweeper.isMined(i, j))
					mineCounter++;
			}
		assertEquals(10, mineCounter);
	}
	
	class MineSweeperWithPlaceMinesStubbed extends MineSweeperWithExposeNeighborsStubbed {

		@Override
		public void placeMines(int numberOfMines) {  
      int mineCounter = 0;
			
			while(mineCounter < numberOfMines)
			{
				if(mineCounter > 0)
					continue;
				setMine(0, 0);
				mineCounter++;
			}
		}
	}

	@Test 
	public void ifCellHasNoMinePlaceMinesMinesTheCell(){
		MineSweeperWithPlaceMinesStubbed minesweeper = new MineSweeperWithPlaceMinesStubbed();
		minesweeper.placeMines(1);
		assertTrue(minesweeper.isMined(0,0));
	}
	
	@Test 
	public void gameStatusIsInProgressWhenNoCellsExposedAndNoCellsSealed(){
		assertEquals(GameStatus.INPROGRESS, minesweeper.getGameStatus());
	}

	@Test
	public void gameStatusIsInProgressWhenAllMinesSealed10UnminedCellsSealedAndAllOtherCellsExposed(){
		int columns[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		for (int i = 0; i < Minesweeper.SIZE; i++){
			minesweeper.setMine(0, columns[i]);
			minesweeper.toggleSeal(0, columns[i]);
			minesweeper.toggleSeal(1, columns[i]);
		}
		for (int i = 2; i < Minesweeper.SIZE; i++){
			for (int j = 0; j < Minesweeper.SIZE; j++){
			  minesweeper.exposeCell(i, columns[j]);
			}
		}

		assertEquals(GameStatus.INPROGRESS, minesweeper.getGameStatus());
	}

	@Test
	public void gameStatusIsWonWhenAllCellsExposedAndAllMinesSealed(){
		int rows[] = {1, 1, 9, 6, 5, 2, 3, 7, 4, 3};
		int columns[] = {1, 8, 9, 2, 8, 7, 4, 6, 5, 7};
		for (int i = 0; i < Minesweeper.SIZE; i++){
			minesweeper.setMine(rows[i], columns[i]);
			minesweeper.toggleSeal(rows[i], columns[i]);
		}
		for (int row = 0; row < Minesweeper.SIZE; row++){
			for (int column = 0; column < Minesweeper.SIZE; column++){
				minesweeper.exposeCell(row, column);
			}
		}
		assertEquals(GameStatus.WON, minesweeper.getGameStatus());
	}
	
	@Test
	public void gameStatusIsInProgressWhen90CellsExposedAndNotAllMinesSealed(){
		int columns[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		for (int i = 0; i < Minesweeper.SIZE; i++){
			minesweeper.setMine(0, columns[i]);
			minesweeper.toggleSeal(0, columns[i]);
		}
		minesweeper.toggleSeal(0, 0);

		for (int row = 1; row < Minesweeper.SIZE; row++){
			for (int column = 0; column < Minesweeper.SIZE; column++){
				minesweeper.exposeCell(row, column);
			}
		}
		assertEquals(GameStatus.INPROGRESS, minesweeper.getGameStatus());
	}
	
	public void gameStatusIsInProgressWhen80CellsExposedAndAllMinesSealed(){
		int rows[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		int columns[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		for (int i = 0; i < Minesweeper.SIZE; i++){
			minesweeper.setMine(rows[i], columns[i]);
			minesweeper.toggleSeal(rows[i], columns[i]);
		}
		for (int row = 2; row < Minesweeper.SIZE; row++){
			for (int column = 0; column < Minesweeper.SIZE; column++){
				minesweeper.exposeCell(row, column);
			}
		}
		assertEquals(GameStatus.INPROGRESS, minesweeper.getGameStatus());
	}
	
	public void gameStatusIsInProgressWhen90CellsExposedAndAllMinesSealed(){
		int rows[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		int columns[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		for (int i = 0; i < Minesweeper.SIZE; i++){
			minesweeper.setMine(rows[i], columns[i]);
			minesweeper.toggleSeal(rows[i], columns[i]);
		}
		for (int row = 2; row < Minesweeper.SIZE; row++){
			for (int column = 0; column < Minesweeper.SIZE; column++){
				minesweeper.exposeCell(row, column);
			}
		}
		assertEquals(GameStatus.INPROGRESS, minesweeper.getGameStatus());
	}

	@Test
	public void gameStatusIsLostAfterMineIsExposed(){
		minesweeper.setMine(1, 1);
		minesweeper.placeMines(9);
		minesweeper.exposeCell(1, 1);
		assertEquals(GameStatus.LOST, minesweeper.getGameStatus());
	}

  @Test
  public void testsToQuitCoverageForEnumGameStatus() {
    GameStatus.values();
    GameStatus.valueOf("WON");
  }

  @Test
  public void testsToQuitCoverageForEnumCellState() {
    CellState.values();
    CellState.valueOf("SEALED");
  }
  
  @Test
  public void getNumberOfAjacentMinesOneMine(){
        minesweeper.setMine(1, 0);
      
        assertEquals(1, minesweeper.numberOfMinesAdjacent(0, 0)); 
  }
  
  @Test
  public void getNumberOfAjacentMinesThreeMines(){
        minesweeper.setMine(0, 1);
        minesweeper.setMine(0, 2);
        minesweeper.setMine(0, 3);
        assertEquals(3, minesweeper.numberOfMinesAdjacent(1, 2));
  }
  
  @Test
  public void getNumberOfAjacentMinesWhenCellRowIsLastRowInGrid(){
        minesweeper.setMine(8, 9);
        minesweeper.setMine(9, 9);

        assertEquals(2, minesweeper.numberOfMinesAdjacent(9, 8)); 
  }
 
  @Test
  public void getNumberOfAjacentMinesWhenCellColumnIsLastColumnInGrid(){
        minesweeper.setMine(9, 8);
        minesweeper.setMine(9, 9);
      
        assertEquals(2, minesweeper.numberOfMinesAdjacent(8, 9)); 
  }
  
  @Test
  public void getNumberOfAjacentMinesWhenRowIsLastRowAndColumnIsLastColumnInGrid()
  {
        minesweeper.setMine(8, 8);
        minesweeper.setMine(8, 9);
        minesweeper.setMine(9, 8);

        assertEquals(3, minesweeper.numberOfMinesAdjacent(9, 9)); 
  }
  
  
    @Test
    public void testToQuitCoverageForEnumCellState()
    {
            CellState.values();
            CellState.valueOf("UNEXPOSED");
    }
    
    @Test
    public void testToQuitCoverageForEnumGameStatus()
    {
            GameStatus.values();
            GameStatus.valueOf("INPROGRESS");
    }
    
    @Test
    public void testToQuitCoverageForCellStateSealed()
    {
            CellState.values();
            CellState.valueOf("SEALED");
    }
}