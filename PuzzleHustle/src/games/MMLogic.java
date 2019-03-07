package games;

import java.io.Serializable;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

@SuppressWarnings("serial")
public class MMLogic implements Serializable {
	private Color[] colors = { Color.RED, Color.BLUE, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.PURPLE,
			Color.CHOCOLATE, Color.HOTPINK, Color.BISQUE };
	private Color[] puzzle = new Color[colors.length];
	private int rightSpots = 0;
	private int rightColors = 0;
	private int guessesLeft = 20;
	private boolean canCheck;
	int length = 0;

	public Color[] getAnswer() {
		Color[] tempArr = new Color[length];
		for (int i = 0; i < length; i++) {
			tempArr[i] = puzzle[i];
		}
		return tempArr;
	}

	public void createPuzzle(int length) {
		this.length = length;
		Random rng = new Random();
		for (int i = 0; i < length; i++) {
			int ranNum = rng.nextInt(length);
			puzzle[i] = colors[ranNum];
//			puzzle[i] = Color.RED;
		}
		for (int i = 0; i < length; i++) {
			System.out.println(colors[i]);
		}
		System.out.println("\n");
		for (int i = 0; i < length; i++) {
			System.out.println(puzzle[i]);
		}

	}

	public void checkGuess(Rectangle[] squares) {
		checkIfFullGuess(squares);
		if (canCheck) {
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
			if (rightSpots == length) {
				guessesLeft++;
			}
			guessesLeft--;
		}
	}

	private void checkIfFullGuess(Rectangle[] squares) {
		canCheck = true;
		for (Rectangle r : squares) {
			canCheck = r.getFill() != Color.BLACK;
			if (!canCheck) {
				break;
			}
		}
	}

	public int getRightSpots() {
		return rightSpots;
	}

	public int getRightColors() {
		return rightColors;
	}

	public int getGuessesLeft() {
		return guessesLeft;
	}

	public void setNumOfTries(int numOfTries) {
		this.guessesLeft = numOfTries;
	}

	public boolean canCheck() {
		return canCheck;
	}
}
