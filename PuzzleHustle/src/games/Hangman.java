package games;

import enums.PuzzleType;
import interfaces.ISolveable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.Puzzle;
import models.User;

@SuppressWarnings("serial")
public class Hangman extends Puzzle implements ISolveable {

	private HMLogic gameLogic = new HMLogic();
	private Pane hangman = null;
	private Button giveUp = new Button("Give Up");
	private int gameMode = 0;
	private TextField guessBox;
	private Text puzzle;
	private Boolean wasStarted = false;
	private String phrase = "";
	private Rectangle head;
	private Rectangle body;
	private Rectangle rightArm;
	private Rectangle leftArm;
	private Rectangle leftLeg;
	private Rectangle rightLeg;
	private ImageView gallows;
	private ImageView hungman;
	private boolean wasWon = false;

	public Hangman(String filePath, User user) {
		super(filePath, PuzzleType.HANGMAN, user);
	}

	private void resetStage(boolean isFirstTime) {
		hangman.getChildren().clear();
		if (isFirstTime) {
			Label menuMessage = new Label();
			Label menuMessage2 = new Label();
			menuMessage.setText("How many players do you want?");
			menuMessage.setId("message");
			menuMessage2.setText("Note: 2 players will not count towards your stats");
			menuMessage2.setId("message2");
			hangman.getChildren().addAll(menuMessage, menuMessage2);
			menuMessage.setTranslateX(120);
			menuMessage.setTranslateY(50);
			menuMessage2.setTranslateX(267.5);
			menuMessage2.setTranslateY(150);
			drawStarterButtons();
		} else {
			startTimer();
			drawGiveUpButton();
			startGame();
		}
	}

	private void createPuzzle() {
		gameLogic.game(gameMode, phrase);
		puzzle = new Text(gameLogic.printGuesses());
		guessBox = new TextField();
		guessBox.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (guessBox.getText().length() >= 1) {
					if (guessBox.getText().length() > 1) {
						guessBox.setText(guessBox.getText().substring(0, 1));
					}
					char c = guessBox.getText().charAt(0);
					if ((c < 65 || c > 122) || (c >= 91 && c <= 96)) {
						guessBox.setText("");
					}
				}
			}

		});
		guessBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (guessBox.getText().length() >= 1) {
					gameLogic.setGuess(guessBox.getText().charAt(0));
					guessBox.setText("");
					checkGuess();
					if (gameLogic.checkIfWon()) {
						gameWon();
					}
				}
			}
		});

		hangman.getChildren().addAll(puzzle, guessBox);
		guessBox.setTranslateY(400);
		guessBox.setTranslateX(20);
		guessBox.setMaxWidth(30);
		puzzle.setId("puzzle");
		puzzle.setTranslateY(420);
		puzzle.setTranslateX(10);
	}

	protected void checkGuess() {
		if (gameLogic.checkGuess()) {
			puzzle.setText(gameLogic.printGuesses());
			drawMan(gameLogic.getTries());
		} else {
			showSolution();
		}
		setScore(gameLogic.getScore());
	}

	private void drawMan(int losses) {
		switch (losses) {
		case 6:
			hangman.getChildren().remove(rightLeg);
		case 5:
			hangman.getChildren().remove(leftLeg);
		case 4:
			hangman.getChildren().remove(rightArm);
		case 3:
			hangman.getChildren().remove(leftArm);
		case 2:
			hangman.getChildren().remove(body);
		case 1:
			hangman.getChildren().remove(head);
		}
	}

	private void startGame() {
		Image gallowPic = new Image("File:Gallows.png");
		Image hungmanPic = new Image("File:hungman.png");
		gallows = new ImageView(gallowPic);
		hungman = new ImageView(hungmanPic);
		head = new Rectangle();
		head = new Rectangle(575, 31, 60, 116);
		body = new Rectangle(575, 147, 52, 87);
		rightArm = new Rectangle(555, 150, 20, 80);
		leftArm = new Rectangle(627, 157, 15, 73);
		leftLeg = new Rectangle(595, 230, 40, 90);
		rightLeg = new Rectangle(555, 230, 40, 90);
		head.setId("cover");
		body.setId("cover");
		rightArm.setId("cover");
		leftArm.setId("cover");
		rightLeg.setId("cover");
		leftLeg.setId("cover");
		hangman.getChildren().addAll(gallows, hungman, head, body, rightArm, leftArm, leftLeg, rightLeg);

		gallows.setFitHeight(500);
		gallows.setPreserveRatio(true);
		gallows.setTranslateY(-50);
		gallows.setTranslateX(250);
		hungman.setFitHeight(250);
		hungman.setPreserveRatio(true);
		hungman.setTranslateY(75);
		hungman.setTranslateX(535);

		createPuzzle();
	}

	private void drawStarterButtons() {
		Button button1 = new Button("1");
		Button button2 = new Button("2");
		button1.setId("starterButton");
		button2.setId("starterButton");
		button1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gameMode = 1;
				wasStarted = true;
				resetStage(false);
			}
		});
		button2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gameMode = 2;
				wasStarted = true;
				getPhrase();
			}
		});
		hangman.getChildren().addAll(button1, button2);
		button1.setTranslateX(290);
		button1.setTranslateY(200);
		button2.setTranslateX(590);
		button2.setTranslateY(200);
	}

	private void getPhrase() {
		hangman.getChildren().clear();
		LimitedTextField phraseBox = new LimitedTextField();
		phraseBox.setPromptText("Type the phrase to guess! (no more than 35 characters)");
		phraseBox.setMaxLength(35);
		phraseBox.setId("phraseBox");
		phraseBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (phraseBox.getText().trim().length() >= 1) {
					phrase = phraseBox.getText();
					resetStage(false);
				}
			}
		});

		hangman.getChildren().add(phraseBox);
		phraseBox.setTranslateY(200);
		phraseBox.setTranslateX(100);
	}

	private void drawGiveUpButton() {
		display.getLeftSidebar().getChildren().remove(giveUp);
		giveUp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showSolution();
			}
		});
		display.getLeftSidebar().getChildren().add(giveUp);
	}

	private void gameWon() {
		wasWon = true;
		showSolution();
		System.out.println(elapsedTimeTotal);
		if (gameMode == 1) {
			setScore(gameLogic.getScore());
			setSolved(true);
		}
	}

	@Override
	public void showSolution() {
		Label menuMessage3 = new Label();
		menuMessage3.setText("You WON!!");
		menuMessage3.setId("message3");
		menuMessage3.setTranslateX(130);
		menuMessage3.setTranslateY(150);
		hangman.getChildren().remove(guessBox);
		puzzle.setText("The answer was " + "\" " + gameLogic.showAnswer() + "\"" + "\nYou guessed: "
				+ gameLogic.getAllGuesses().trim());
		puzzle.setTranslateY(470);
		if (!wasWon) {
			drawMan(6);
			menuMessage3.setText("You lose");
		}
		hangman.getChildren().add(menuMessage3);
		pauseTimer();
	}

	@Override
	public void showInstructions() {
		if (wasStarted) {
			pauseTimer();
		}
		hangman.getChildren().clear();
		Text instructions = new Text("Type your guessed character in the box. "
				+ "After guessing, your guess will either show up under the \""
				+ "correct guesses so far\" section, or \nthe \"wrong guesses so "
				+ "far\" section. If you guess incorrectly 6 times, you lose. " + "Guess every character to win!");
		instructions.setId("instructions");
		instructions.setY(100);
		instructions.setX(10);
		hangman.getChildren().add(instructions);
	}

	@Override
	public void hideInstructions() {
		if (wasStarted) {
			hangman.getChildren().clear();
			puzzle.setText(gameLogic.printGuesses());
			drawMan(gameLogic.getTries());
			hangman.getChildren().addAll(gallows, hungman, head, body, rightArm, leftArm, leftLeg, rightLeg, puzzle,
					guessBox);
			drawMan(gameLogic.getTries());
		} else {
			hangman.getChildren().clear();
			resetStage(true);
		}
	}

	@Override
	protected void setupPuzzlePane() {
		hangman = new Pane();
		resetStage(true);
		puzzlePane.getChildren().add(hangman);
		puzzlePane.getStylesheets().add("views/HM.css");
	}

	@Override
	public Scene getScene() {
		if (scene == null) {
			super.getScene();
			setupPuzzlePane();
		}
		return scene;
	}

	@Override
	public void setScore(float score) {
		this.score = score;
		if (score > user.getHighScore(PUZZLE_TYPE) && wasWon) {
			user.setHighScore(PUZZLE_TYPE, score);
			highScoreLabel.textProperty().set("High Score: " + user.getHighScore(PUZZLE_TYPE));
		}
		scoreLabel.textProperty().set("Score: " + getScore());
	}

}
