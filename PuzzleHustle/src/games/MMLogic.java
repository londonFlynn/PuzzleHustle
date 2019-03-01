package games;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MMLogic {
	private Color[] colors = { Color.RED, Color.BLUE, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.PURPLE };
	private Color[] puzzle = new Color[colors.length];
	private int rightSpots = 0;
	private int rightColors = 0;

	public void createPuzzle() {
		Random rng = new Random();
		for (int i = 0; i < 6; i++) {
			int ranNum = rng.nextInt(colors.length);
			puzzle[i] = colors[ranNum];
		}
		for (int i = 0; i < 6; i++) {
			System.out.println(colors[i]);
		}
		System.out.println("\n");
		for (int i = 0; i < 6; i++) {
			System.out.println(puzzle[i]);
		}
		
	}

	public boolean checkGuess(Rectangle[] squares) {
		boolean canCheck = true;
		for (Rectangle r : squares) {
			canCheck = r.getFill() != Color.BLACK;
			if (!canCheck) {
				return false;
			}
		}
		rightColors = 0;
		rightSpots = 0;
		boolean[] flags = new boolean[squares.length];
		for (int i = 0; i < squares.length; i++) {
			flags[i] = squares[i].getFill() == puzzle[i];
		}
		for (int i = 0; i < squares.length; i++) {
			if (squares[i].getFill() == puzzle[i]) {
				rightSpots++;
			} else {
				for (int j = 0; j < squares.length; j++) {
					if (!flags[j] && squares[i].getFill() == puzzle[j]) {
						rightColors++;
						break;
					}
				}
			}
		}
		return canCheck;
	}

	public int getRightSpots() {
		return rightSpots;
	}

	public int getRightColors() {
		return rightColors;
	}
}
