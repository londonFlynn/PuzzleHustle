package games;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

public class HMLogic {

	private char guess;
	private char[] phraseChars;
	private char[] correctGuesses;
	private char[] correctGuesses2;
	private char[] wrongGuesses;
	private String phrase;
	private char[] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	private String[] words = null;
	private int count = 0;
	private int score = 0;

	private void createDictionary() {
		String text = "";
		try {
			FileReader fr = new FileReader("Dictionary.txt");
			BufferedReader reader = new BufferedReader(fr);
			String line = reader.readLine();
			while (line != null) {
				if (line.indexOf("\n") == -1) {
					text += line + ",";
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		words = text.split(",");
	}

	private String getRandomThing() {
		createDictionary();
		Random rng = new Random();
		int index = rng.nextInt(words.length);
		return words[index];
	}

	public String showAnswer() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < phraseChars.length; i++) {
			sb.append(phraseChars[i] + " ");
		}
		return sb.toString();
	}

	public String getAllGuesses() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < correctGuesses2.length; i++) {
			if (correctGuesses2[i] != ' ') {
				sb.append(correctGuesses2[i] + " ");
			}
		}
		for (int i = 0; i < wrongGuesses.length; i++) {
			sb.append(wrongGuesses[i] + " ");
		}
		return sb.toString();
	}

	public void game(int gameMode, String newPhrase) {
		if (gameMode == 1) {
			this.phrase = getRandomThing();
		} else {
			phrase = newPhrase;
		}

		phraseChars = new char[phrase.length()];
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
			phraseChars[i] = phrase.charAt(i);
			guess = phraseChars[i];
			if (!arrContainsLetter(alphabet)) {
				correctGuesses[i] = phraseChars[i];
			}
		}
	}

	public boolean checkGuess() {

		if (((arrContainsLetter(correctGuesses) || (arrContainsLetter(wrongGuesses))))) {
			count--;
		} else if (!arrContainsLetter(phraseChars)) {
			score--;
			wrongGuesses[count] = guess;
		} else if (arrContainsLetter(phraseChars)) {
			score++;
			count--;
		}
		count++;
		if (count >= 6) {
			return false;
		}
		return true;
	}

	private boolean arrContainsLetter(char[] array) {
		boolean containsLetter = false;
		for (int i = 0; i < array.length; i++) {
			containsLetter = array[i] == guess;
			if (array == phraseChars) {
				if (containsLetter) {
					correctGuesses[i] = guess;
				} else {
					containsLetter = ((array[i] + 32) == guess);
					if (containsLetter) {
						correctGuesses[i] = array[i];
					} else {
						containsLetter = ((array[i] - 32) == guess);
						if (containsLetter) {
							correctGuesses[i] = array[i];
						}
					}
				}
			} else if (array == alphabet) {
				if (containsLetter) {
					return true;
				} else {
					containsLetter = ((array[i] + 32) == guess);
					if (containsLetter) {
						return true;
					} else {
						containsLetter = ((array[i] - 32) == guess);
						if (containsLetter) {
							return true;
						}
					}
				}
			}
		}
		for (int i = 0; i < array.length; i++) {
			containsLetter = array[i] == guess;
			if (array == phraseChars) {
				if (containsLetter) {
					correctGuesses2[i] = guess;
					return true;
				} else {
					containsLetter = ((array[i] + 32) == guess);
					if (containsLetter) {
						correctGuesses2[i] = array[i];
						return true;
					} else {
						containsLetter = ((array[i] - 32) == guess);
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
					containsLetter = ((array[i] + 32) == guess);
					if (containsLetter) {
						return true;
					} else {
						containsLetter = ((array[i] - 32) == guess);
						if (containsLetter) {
							return true;
						}
					}
				}
			}
		}
		return containsLetter;
	}

	public void setGuess(char guess) {
		this.guess = guess;
	}

	public String printGuesses() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nCorrect guesses so far: ");
		for (int a = 0; a < correctGuesses.length; a++) {
			sb.append(correctGuesses[a] + " ");
		}
		sb.append("\nWrong guesses so far: ");
		for (int i = 0; i < wrongGuesses.length; i++) {
			sb.append(wrongGuesses[i] + " ");
		}
		sb.append("\n");
		return sb.toString();
	}

	public int getTries() {
		return count;
	}
	
	public int getScore() {
		return score;
	}

	public boolean checkIfWon() {
		boolean won = false;
		for (int i = 0; i < correctGuesses.length; i++) {
			won = correctGuesses[i] == phraseChars[i];
			if (!won) {
				return false;
			}
		}
		return won;
	}
}
