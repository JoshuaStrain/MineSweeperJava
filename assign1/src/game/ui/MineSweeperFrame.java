package game.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import game.Minesweeper;
import game.Minesweeper.CellState;
import game.Minesweeper.GameStatus;

@SuppressWarnings("serial")
public class MineSweeperFrame extends JFrame implements WindowStateListener{
	private static final int SIZE = 10;
	private MineSweeperCell grid[][]; 
	private Minesweeper myminesweeper;

	@Override
	protected void frameInit(){
		super.frameInit();
		setTitle("Minesweeper");
		setLayout(new GridLayout(SIZE, SIZE));
		myminesweeper = new Minesweeper();
		grid = new MineSweeperCell[SIZE][SIZE];

		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				MineSweeperCell cell = new MineSweeperCell(i, j);
				getContentPane().add(cell);
				grid[i][j] = cell;
				cell.addMouseListener( new CellClickedHandler());
			}
		}
		myminesweeper.placeMines(10);

		setSize(700, 600);
		addWindowListener(new WindowCloseHandler());
	}

	public static void main(String[] args){
		JFrame myminesweeper = new MineSweeperFrame(); 
		myminesweeper.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myminesweeper.setVisible(true);
	}

  public void windowStateChanged(WindowEvent event){
  	int state = event.getNewState();
  	if (state == WindowEvent.WINDOW_DEICONIFIED)
  		this.repaint();
  		

  }
	
	private class WindowCloseHandler extends WindowAdapter{
		@Override
		public void windowClosing( WindowEvent event){
			System.exit(0); 
		}
	}

	private class CellClickedHandler extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent event){
			MineSweeperCell mycell = (MineSweeperCell) event.getSource();
			cellClicked(mycell.row, mycell.column, event.getButton());
		}
	}	

	private void cellClicked(int row, int column, int mouseKey){
		if(mouseKey == MouseEvent.BUTTON1){
			leftClickLogic(row, column);
		}
		else if (mouseKey == MouseEvent.BUTTON3){
			rightClickLogic(row, column);
		}
	}

	public void leftClickLogic(int row, int column){
		if (!(myminesweeper.getCellStatus(row, column) == CellState.SEALED)){
			myminesweeper.exposeCell(row, column);
			grid[row][column].setEnabled(false);
			grid[row][column].removeMouseListener(grid[row][column].getMouseListeners()[0]);
			exposeNeighbors(row, column);
			if( myminesweeper.getGameStatus() == GameStatus.LOST )
				grid[row][column].setText("Bomb");
			checkWinOrLose();
		}
		else{
			myminesweeper.toggleSeal(row, column);
			grid[row][column].setText("");
		}
	}
	
	public void rightClickLogic(int row, int column){
		myminesweeper.toggleSeal(row, column);
		grid[row][column].setText("Sealed");
	}

	public void checkWinOrLose(){
		if( myminesweeper.getGameStatus() == GameStatus.LOST ){
			JOptionPane.showMessageDialog(rootPane, "You Lost!");
			System.exit(0);
		}
		if( myminesweeper.getGameStatus() == GameStatus.WON ){
			JOptionPane.showMessageDialog(rootPane, "You Won!");
			System.exit(0);
		}
	}

	public void exposeNeighbors(int row, int column){
		grid[row][column].setBackground(Color.WHITE);
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				if(myminesweeper.getCellStatus(i, j) == CellState.EXPOSED){
					grid[i][j].setBackground(Color.WHITE);
					if( myminesweeper.numberOfMinesAdjacent(i, j) > 0)
						grid[i][j].setText( String.valueOf(myminesweeper.numberOfMinesAdjacent(i, j)));
				}
			}
		}
	}


}