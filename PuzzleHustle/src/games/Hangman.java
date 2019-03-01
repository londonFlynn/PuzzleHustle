package games;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import enums.PuzzleType;
import interfaces.ISolveable;
import models.Puzzle;
import models.User;

public class Hangman extends Puzzle implements ISolveable {

	public Hangman(String filePath, PuzzleType PUZZLE_TYPE, User user) {
		super(filePath, PUZZLE_TYPE, user);
		// TODO Auto-generated constructor stub
	}

	private static char c;
	private static char[] wordChars;
	private static char[] correctGuesses;
	private static char[] correctGuesses2;
	private static char[] wrongGuesses;
	private static boolean valid = false;
	private String phrase;
	private static char[] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	private static String[] words = null;

	public static void createDictionary() {
		String text = "";
		try {
			FileReader fr = new FileReader("words2.txt");
			BufferedReader reader = new BufferedReader(fr);
			String line = reader.readLine();
			while (line != null) {
				if (line.indexOf("'") == -1) {
					text += line + ",";
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			System.out.println("There was a problem reading the file.\n" + e.getMessage());
		}
		words = text.split(",");
	}

	private String getRandomThing() {
		Random rng = new Random();
		int index = rng.nextInt(words.length);
		return words[index];
	}

	private void game() {

		System.out.println(
				"Hi! Welcome to Hangman. Would you like to play one player or two player?\nFor one player, a random word will be selected for you to guess!\nFor two player, player1 will insert a word or phrase (up to 8 words) and player2 will try to guess it.");
//		int gameMode = ConsoleIO.promptForMenuSelection(new String[] { "One Player", "Two Player" }, false, false);
		int gameMode = 0;
		do {
			do {
				if (gameMode == 2) {
					// TODO add the multiplayer option
//					phrase = ConsoleIO.promptForInput(
//							"What phrase would you like to use? At least 3 characters, and no more than 8 words.",
//							false).trim();
				} else {
					phrase = getRandomThing();
				}
				int amountOfSpaces = 0;
				for (int i = 0; i < phrase.length(); i++) {
					if (phrase.charAt(i) == ' ') {
						amountOfSpaces++;
					}
				}

			} while (!valid);

			wordChars = new char[phrase.length()];
			correctGuesses = new char[phrase.length()];
			correctGuesses2 = new char[phrase.length()];
			wrongGuesses = new char[6];

			for (int i = 0; i < correctGuesses.length; i++) {
				correctGuesses[i] = '_';
			}
			for (int i = 0; i < correctGuesses.length; i++) {
				correctGuesses2[i] = ' ';
			}

			for (int i = 0; i < phrase.length(); i++) {
				wordChars[i] = phrase.charAt(i);
				c = wordChars[i];
				if (!arrContainsLetter(alphabet)) {
					correctGuesses[i] = wordChars[i];
				}
			}
			for (int i = 0; i < correctGuesses.length; i++) {
				c = '_';
				valid = arrContainsLetter(correctGuesses) && correctGuesses.length >= 3;
			}
			if (!valid) {
				// TODO get rid of this, but still check validity.... somehow
//				System.out.println("Sorry, that's invalid!");
			}
		} while (!valid);

		printGuesses();

		for (int i = 0; i < wordChars.length + 6; i++) {
			if (wrongGuesses[5] == 0) {

				// TODO use the gui to refactor this whole thing.
				int input = 0;
				if (input == 2) {

					do {
						// TODO refactor to take from GUI textbox
//						c = ConsoleIO.promptForChar("\nPlace a guess", 'A', 'z');
						c = 'A';
						valid = arrContainsLetter(alphabet);
						if (!valid) {
							System.out.println("You have to guess a letter!");
						}
					} while (!valid);

					if (((arrContainsLetter(correctGuesses) || (arrContainsLetter(wrongGuesses))))) {
						printGuesses();
						System.out.println("\nYou already guessed that!");
						i -= 1;
					} else if (arrContainsLetter(wordChars)) {
						printGuesses();
						System.out.println("\nYou guessed one right!\n");
						i -= 1;
						if (checkIfWon()) {
							// TODO idk, tbh
//							hangingMan();
//							System.out.println("\n\nYou won!\n");
//							i = wordChars.length + 6;
//							playAgain();
						}
					} else {
						wrongGuesses[i] = c;
						printGuesses();
						System.out.println("\nYour guess was incorrect. ");
					}
				} else if (input == 1) {
					// TODO ew
//					if (input2.equalsIgnoreCase(phrase)) {
//						System.out.println("You win!");
//						i = wordChars.length + 6;
//					}
				}
			}
		}
	}

	private static boolean arrContainsLetter(char[] array) {
		boolean containsLetter = false;
		for (int i = 0; i < array.length; i++) {
			containsLetter = array[i] == c;
			if (array == wordChars) {
				if (containsLetter) {
					correctGuesses[i] = c;
				} else {
					containsLetter = ((array[i] + 32) == c);
					if (containsLetter) {
						correctGuesses[i] = array[i];
					} else {
						containsLetter = ((array[i] - 32) == c);
						if (containsLetter) {
							correctGuesses[i] = array[i];
						}
					}
				}
			} else if (array == alphabet) {
				if (containsLetter) {
					return true;
				} else {
					containsLetter = ((array[i] + 32) == c);
					if (containsLetter) {
						return true;
					} else {
						containsLetter = ((array[i] - 32) == c);
						if (containsLetter) {
							return true;
						}
					}
				}
			}
		}
		for (int i = 0; i < array.length; i++) {
			containsLetter = array[i] == c;
			if (array == wordChars) {
				if (containsLetter) {
					correctGuesses2[i] = c;
					return true;
				} else {
					containsLetter = ((array[i] + 32) == c);
					if (containsLetter) {
						correctGuesses2[i] = array[i];
						return true;
					} else {
						containsLetter = ((array[i] - 32) == c);
						if (containsLetter) {
							correctGuesses2[i] = array[i];
							return true;
						}
					}
				}
			} else {
				if (containsLetter) {
					return true;
				} else {
					containsLetter = ((array[i] + 32) == c);
					if (containsLetter) {
						return true;
					} else {
						containsLetter = ((array[i] - 32) == c);
						if (containsLetter) {
							return true;
						}
					}
				}
			}
		}
		return containsLetter;
	}

	private static void printGuesses() {

		// TODO refactor this with the GUI

		System.out.print("\nCorrect guesses so far: ");
		for (int a = 0; a < correctGuesses.length; a++) {
			System.out.print(correctGuesses[a] + " ");
		}
		System.out.print("\nWrong guesses so far: ");
		for (int i = 0; i < wrongGuesses.length; i++) {
			System.out.print(wrongGuesses[i] + " ");
		}
		System.out.println("\n");
	}

	private boolean checkIfWon() {
		boolean won = false;
		for (int i = 0; i < correctGuesses.length; i++) {
			won = correctGuesses[i] == wordChars[i];
			if (!won) {
				return false;
			}
		}
		return won;
	}

	@Override
	public void showInstructions() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hideInstructions() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setupPuzzlePane() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showSolution() {
		// TODO Auto-generated method stub

	}
}
