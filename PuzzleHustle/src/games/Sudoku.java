package games;

import java.util.ArrayList;

import controllers.Puzzle;
import controllers.PuzzleType;
import controllers.User;

public class Sudoku extends Puzzle{
	private int[][][][] board;
	private ArrayList<Integer>[][][][] notes;

	public Sudoku(String filePath, PuzzleType PUZZLE_TYPE, User user) {
		super(filePath, PUZZLE_TYPE, user);
	}
	
	private void enterNote(int note, int rowIn, int columnIn, int row, int column) {
		
	}
	
	private void enterNumber(int newNumber, int rowIn, int columnIn, int row, int column) {
		
	}
	private void removeNote(int note, int rowIn, int columnIn, int row, int column) {
		
	}
	private void removeNumber(int rowIn, int columnIn, int row, int column) {
		
	}
	private int[][][][] getBoard() {
		int[][][][] currentState = new int[board.length][board.length][board.length][board.length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				for (int k = 0; k < board.length; k++) {
					for (int l = 0; l < board.length; l++) {
						currentState[i][j][k][l] = board[i][j][k][l];
					}
				}
			}
		}
		return currentState;
	}
	private ArrayList<Integer>[][][][] getNotes() {
		return notes;
	}

	@Override
	public void showInstructions() {
		
	}

}
