package games;

import models.Cell;

public class MinesSurroundingLogic {
	private static Cell[][] workingBoard;
	private static int x;
	private static int y;
	
	public static int run(int x, int y, Cell[][] workingBoard) {
		MinesSurroundingLogic.x = x;
		MinesSurroundingLogic.y = y;
		MinesSurroundingLogic.workingBoard = workingBoard;
		int total = 0;		

		if (!workingBoard[x][y].isMine()) {
			total = checkTopLeft() ? total + 1 : total;
			total = checkTop() ? total + 1 : total;
			total = checkTopRight() ? total + 1 : total;
			total = checkLeft() ? total + 1 : total;
			total = checkRight() ? total + 1 : total;
			total = checkBottomLeft() ? total + 1 : total;
			total = checkBottom() ? total + 1 : total;
			total = checkBottomRight() ? total + 1 : total;
		} else {
			total = 9;
		}
		
		return total;
	}
	
	private static boolean checkTopLeft() {
		boolean result = false;
		if (x - 1 >= 0 && y - 1 >= 0) {
			result = workingBoard[x-1][y-1].isMine();
		}
		return result;
	}
	
	private static boolean checkTop() {
		boolean result = false;
		if (y - 1 >= 0) {
			result = workingBoard[x][y-1].isMine();
		}
		return result;
	}
	
	private static boolean checkTopRight() {
		boolean result = false;
		if (x + 1 < workingBoard.length && y - 1 >= 0) {
			result = workingBoard[x+1][y-1].isMine();
		}
		return result;
	}

	private static boolean checkLeft() {
		boolean result = false;
		if (x - 1 >= 0) {
			result = workingBoard[x-1][y].isMine();
		}
		return result;
	}

	private static boolean checkRight() {
		boolean result = false;
		if (x + 1 < workingBoard.length) {
			result = workingBoard[x+1][y].isMine();
		}
		return result;
	}

	private static boolean checkBottomLeft() {
		boolean result = false;
		if (x - 1 >= 0 && y + 1 < workingBoard[0].length) {
			result = workingBoard[x-1][y+1].isMine();
		}
		return result;
	}

	private static boolean checkBottom() {
		boolean result = false;
		if (y + 1 < workingBoard[0].length) {
			result = workingBoard[x][y+1].isMine();
		}
		return result;
	}

	private static boolean checkBottomRight() {
		boolean result = false;
		if (x + 1 < workingBoard.length && y + 1 < workingBoard[0].length) {
			result = workingBoard[x+1][y+1].isMine();
		}
		return result;
	}
}
