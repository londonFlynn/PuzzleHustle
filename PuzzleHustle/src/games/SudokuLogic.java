package games;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class SudokuLogic implements Serializable {

	private static final long serialVersionUID = 1L;
	public final int CELL_LENGTH;
	// row, column; row, column
	private int[][][][] board;
	private int[][][][] checkBoard;
	private int[][][][] solution;
	public final int CELL_LENGTH_SQUARED;

	SudokuLogic(int cellLength) {
		if (cellLength > 4 || cellLength < 2) {
			throw new IllegalArgumentException(
					"The board size must be no less than 1 and no greater than 4. You input " + cellLength);
		}
		this.CELL_LENGTH = cellLength;
		CELL_LENGTH_SQUARED = cellLength * cellLength;
		board = new int[cellLength][cellLength][cellLength][cellLength];
	}

	private void changeNumber(int newNumber, int rowIn, int columnIn, int row, int column) {
		board[rowIn][columnIn][row][column] = newNumber;
	}

	int getCellValue(int rowIn, int columnIn, int row, int column) {
		return board[rowIn][columnIn][row][column];
	}
	
	int[][][][] getPuzzle() {
		int[][][][] currentState = new int[CELL_LENGTH][CELL_LENGTH][CELL_LENGTH][CELL_LENGTH];
		for (int i = 0; i < CELL_LENGTH; i++) {
			for (int j = 0; j < CELL_LENGTH; j++) {
				for (int k = 0; k < CELL_LENGTH; k++) {
					for (int l = 0; l < CELL_LENGTH; l++) {
						currentState[i][j][k][l] = getCellValue(i, j, k, l);
					}
				}
			}
		}
		return currentState;
	}
	int[][][][] getSolution() {
		int[][][][] currentState = new int[CELL_LENGTH][CELL_LENGTH][CELL_LENGTH][CELL_LENGTH];
		for (int i = 0; i < CELL_LENGTH; i++) {
			for (int j = 0; j < CELL_LENGTH; j++) {
				for (int k = 0; k < CELL_LENGTH; k++) {
					for (int l = 0; l < CELL_LENGTH; l++) {
						currentState[i][j][k][l] = solution[i][j][k][l];
					}
				}
			}
		}
		return currentState;
	}

	private void randomSolvedBoard() {
		clear();
		while (!randomSolvedBoardRecursion(0, 0, 0, 0));
	}

	private boolean randomSolvedBoardRecursion(int rowIn, int columnIn, int row, int column) {
		boolean canSolve = false;
		int random = 0;
		TriedNums tried = new TriedNums(CELL_LENGTH_SQUARED);
		boolean retry = false;
		do {
			changeNumber(0, rowIn, columnIn, row, column);
			retry = false;
			canSolve = true;
			do {
				random = ThreadLocalRandom.current().nextInt(0, CELL_LENGTH_SQUARED) + 1;
			} while (tried.tried(random) && !tried.triedAll());
			if (!tried.triedAll()) {
				changeNumber(random, rowIn, columnIn, row, column);
				boolean cellValid = cellIsValid(getPuzzle(), rowIn, columnIn, row, column);
				if (cellValid) {
					int nextRowIn = rowIn;
					int nextColumnIn = columnIn;
					int nextRow = row;
					int nextColumn = column + 1;
					if (nextColumn >= CELL_LENGTH) {
						nextColumn = 0;
						nextColumnIn++;
					}
					if (nextColumnIn >= CELL_LENGTH) {
						nextColumnIn = 0;
						nextRow++;
					}
					if (nextRow >= CELL_LENGTH) {
						nextRow = 0;
						nextRowIn++;
					}
					if (nextRowIn >= CELL_LENGTH
							|| randomSolvedBoardRecursion(nextRowIn, nextColumnIn, nextRow, nextColumn)) {
						canSolve = true;
					} else {
						retry = true;

					}
				} else {
					retry = true;
				}

			} else {
				canSolve = false;
			}
		} while (canSolve && retry);
		return canSolve;
	}

	private void clear() {
		for (int i = 0; i < CELL_LENGTH; i++) {
			for (int j = 0; j < CELL_LENGTH; j++) {
				for (int k = 0; k < CELL_LENGTH; k++) {
					for (int l = 0; l < CELL_LENGTH; l++) {
						changeNumber(0, i, j, k, l);
					}
				}
			}
		}
	}

	int[][][][] generatePuzzle() {
		int pruneAttempts = 22 - (CELL_LENGTH*CELL_LENGTH+CELL_LENGTH);
		randomSolvedBoard();
		solution = getPuzzle();
		for (int i = 0; i < pruneAttempts; i++) {
			pruneBoardRandomly();
		}
		return getPuzzle();
	}

	private void pruneBoardRandomly() {
		int[] lastIndexes = new int[4];
		int lastValue;
		boolean solveable;
		do {
			int[] indexes = { ThreadLocalRandom.current().nextInt(CELL_LENGTH),
					ThreadLocalRandom.current().nextInt(CELL_LENGTH), ThreadLocalRandom.current().nextInt(CELL_LENGTH),
					ThreadLocalRandom.current().nextInt(CELL_LENGTH) };
			lastValue = getCellValue(indexes[0], indexes[1], indexes[2], indexes[3]);
			lastIndexes = indexes;
			if (lastValue != 0) {
			changeNumber(0, indexes[0], indexes[1], indexes[2], indexes[3]);
			solveable = isSolveable();
			} else {
				solveable = true;
			}
		} while (solveable);
		changeNumber(lastValue, lastIndexes[0], lastIndexes[1], lastIndexes[2], lastIndexes[3]);
	}
	
	static boolean isSolved(int[][][][] board) {
		boolean solved = true;
		for (int i = 0; i < board.length && solved; i++) {
			for (int j = 0; j < board.length && solved; j++) {
				for (int k = 0; k < board.length && solved; k++) {
					for (int l = 0; l < board.length && solved; l++) {
						for (int m = 0; m < board.length && solved; m++) {
							for (int n = 0; n < board.length && solved; n++) {
								if (!(k == m && l == n)) {
									// row
									solved = board[i][k][j][l] == board[i][m][j][n] || board[i][k][j][l] == 0 ? false
											: solved;
									// colmn
									solved = board[k][i][l][j] == board[m][i][n][j] || board[k][i][l][j] == 0 ? false
											: solved;
									// cell
									solved = board[i][j][k][l] == board[i][j][m][n] || board[i][j][k][l] == 0 ? false
											: solved;
								}
							}
						}
					}
				}
			}
		}
		return solved;
	}

	
	private boolean isSolveable() {
		ArrayList<int[][][][]> boards = new ArrayList<int[][][][]>();
		boolean uniqueSolution = false;
		boolean solutionFound = false;
		boolean solvable = true;
		for (int i = 1; i <= CELL_LENGTH_SQUARED; i++) {
			checkBoard = getPuzzle();
			solvable = solveable(i, 0, 0, 0, 0) && isSolved(checkBoard);
			if (solvable) {
				solutionFound = true;
				int[][][][] newBoard = checkBoard;
				boards.add(newBoard);
			}
		}
		if (solutionFound && solvedBoardsAreEqual(boards)) {
			uniqueSolution = true;
		}
		return uniqueSolution;
	}

	private static boolean solvedBoardsAreEqual(ArrayList<int[][][][]> boards) {
		int cellLength = boards.get(0).length;
		boolean equal = true;
		for (int i = 1; i < boards.size() && equal; i++) {
			for (int j = 0; j < cellLength && equal; j++) {
				for (int k = 0; k < cellLength && equal; k++) {
					for (int l = 0; l < cellLength && equal; l++) {
						for (int m = 0; m < cellLength && equal; m++) {
							equal = boards.get(i)[j][k][l][m] == boards.get(i - 1)[j][k][l][m]
									&& boards.get(i - 1)[j][k][l][m] != 0 ? equal : false;
						}
					}
				}
			}
		}
		return equal;
	}

	private boolean solveable(int seed, int rowIn, int columnIn, int row, int column) {
		boolean canSolve = false;
		if (getCellValue(rowIn, columnIn, row, column) == 0) {
			for (int i = 0; i < CELL_LENGTH_SQUARED && !canSolve; i++) {
				checkBoard[rowIn][columnIn][row][column] = i + seed > CELL_LENGTH_SQUARED ? (i + seed) - CELL_LENGTH_SQUARED
						: i + seed;
				boolean cellValid = cellIsValid(checkBoard, rowIn, columnIn, row, column);
				if (cellValid) {
					if (solveableRecursion(seed, rowIn, columnIn, row, column)) {
						canSolve = true;
					} else {
						checkBoard[rowIn][columnIn][row][column] = 0;
					}
				} else {
					checkBoard[rowIn][columnIn][row][column] = 0;
				}
			}
		} else {
			canSolve = solveableRecursion(seed, rowIn, columnIn, row, column);
		}
		return canSolve;
	}

	private boolean solveableRecursion(int seed, int rowIn, int columnIn, int row, int column) {
		boolean canSolve = false;
		int nextRowIn = rowIn;
		int nextColumnIn = columnIn;
		int nextRow = row;
		int nextColumn = column + 1;
		if (nextColumn >= CELL_LENGTH) {
			nextColumn = 0;
			nextColumnIn++;
		}
		if (nextColumnIn >= CELL_LENGTH) {
			nextColumnIn = 0;
			nextRow++;
		}
		if (nextRow >= CELL_LENGTH) {
			nextRow = 0;
			nextRowIn++;
		}
		if (nextRowIn >= CELL_LENGTH || solveable(seed, nextRowIn, nextColumnIn, nextRow, nextColumn)) {
			canSolve = true;
		}
		return canSolve;
	}

	// for each row of cells, for each row in the cell, check each column of cells,
	// check each column in cell
	static boolean isValid(int[][][][] board) {
		boolean valid = true;
		for (int i = 0; i < board.length && valid; i++) {
			for (int j = 0; j < board.length && valid; j++) {
				for (int k = 0; k < board.length && valid; k++) {
					for (int l = 0; l < board.length && valid; l++) {
						for (int m = 0; m < board.length && valid; m++) {
							for (int n = 0; n < board.length && valid; n++) {
								if (!(k == m && l == n)) {
									// row
									valid = board[i][k][j][l] == board[i][m][j][n] && board[i][m][j][n] != 0 ? false
											: valid;
									// colmn
									valid = board[k][i][l][j] == board[m][i][n][j] && board[m][i][n][j] != 0 ? false
											: valid;
									// cell
									valid = board[i][j][k][l] == board[i][j][m][n] && board[i][j][m][n] != 0 ? false
											: valid;
								}
							}
						}
					}
				}
			}
		}
		return valid;
	}

	static boolean cellIsValid(int[][][][] board, int rowIn, int columnIn, int row, int column) {
		boolean valid = true;
		for (int i = 0; i < board.length && valid; i++) {
			for (int j = 0; j < board.length && valid; j++) {
				if (!(i == row && j == column)) {
					valid = board[rowIn][columnIn][i][j] == board[rowIn][columnIn][row][column]
							&& board[rowIn][columnIn][i][j] != 0 ? false : valid;
				}
				if (!(i == rowIn && j == row)) {
					valid = board[i][columnIn][j][column] == board[rowIn][columnIn][row][column]
							&& board[i][columnIn][j][column] != 0 ? false : valid;
				}
				if (!(i == columnIn && j == column)) {
					valid = board[rowIn][i][row][j] == board[rowIn][columnIn][row][column]
							&& board[rowIn][i][row][j] != 0 ? false : valid;
				}
			}
		}
		return valid;
	}

	@Override
	public String toString() {
		return toString(getPuzzle());
	}

	public static String toString(int[][][][] board) {
		int cellLength = board.length;
		StringBuilder sb = new StringBuilder();
		sb.append('_');
		for (int m = 0; m < cellLength; m++) {
			for (int n = 1; n < cellLength; n++) {
				sb.append("__________");
				switch (cellLength) {
				case 2:
					sb.append("____");
					break;
				case 4:
					break;
				default:
					sb.append('_');
				}
			}
			sb.append("__________");
		}
		for (int i = 0; i < cellLength; i++) {

			sb.append("\n");
			for (int j = 0; j < cellLength; j++) {
				sb.append('|');
				for (int k = 0; k < cellLength; k++) {
					for (int l = 0; l < cellLength; l++) {
						sb.append("\t");
					}
					sb.append("\t|");
				}
				sb.append("\n");

				sb.append('|');
				for (int k = 0; k < cellLength; k++) {
					for (int l = 0; l < cellLength; l++) {
						sb.append("\t");
						int number = board[i][k][j][l];
						if (number > 0 && number < 10) {
							sb.append(number);
						} else if (number >= 10) {
							char letter = (char) ('A' + (number - 10));
							sb.append(letter);
						}
					}
					sb.append("\t|");
				}
				sb.append("\n");

			}
			sb.append('|');
			for (int m = 0; m < cellLength; m++) {
				for (int n = 1; n < cellLength; n++) {
					sb.append("__________");
					switch (cellLength) {
					case 2:
						sb.append("____");
						break;
					case 4:
						break;
					default:
						sb.append('_');
					}
				}
				sb.append("_________|");
			}
		}
		return sb.toString();
	}

}
