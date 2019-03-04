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
import javafx.scene.text.Text;
import models.Puzzle;
import models.User;

@SuppressWarnings("serial")
public class Hangman extends Puzzle implements ISolveable {

	private HMLogic gameLogic = new HMLogic();
	private Pane hangman = null;
	private Text stats;
	private Button giveUp = new Button("Give Up");
	private boolean isDone = false;
	private int gameMode = 0;
	private TextField guessBox;

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
			drawGiveUpButton();
			startGame();
		}
	}

	Text puzzle;

	private void createPuzzle() {
		gameLogic.game();
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
		} else {
			hangman.getChildren().remove(guessBox);
			puzzle.setText("The answer was " + "\" " + gameLogic.showAnswer() + "\"" + "\nYou guessed: "
					+ gameLogic.getAllGuesses().trim());
			puzzle.setTranslateY(470);
		}
	}

	private void startGame() {
		Image gallowPic = new Image("File:Picture1.png");
		ImageView gallows = new ImageView(gallowPic);
		hangman.getChildren().add(gallows);

		gallows.setFitHeight(500);
		gallows.setPreserveRatio(true);
		gallows.setTranslateY(-50);
		gallows.setTranslateX(250);
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
				resetStage(false);
			}
		});
		button2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gameMode = 2;
				resetStage(false);
			}
		});
		hangman.getChildren().addAll(button1, button2);
		button1.setTranslateX(290);
		button1.setTranslateY(200);
		button2.setTranslateX(590);
		button2.setTranslateY(200);
	}

	private void drawGiveUpButton() {
		display.getLeftSidebar().getChildren().remove(giveUp);
		giveUp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showSolution();
				isDone = true;
			}
		});
		display.getLeftSidebar().getChildren().add(giveUp);
	}

	private void drawStats() {

	}

	@Override
	public void showSolution() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showInstructions() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hideInstructions() {

	}

	@Override
	protected void setupPuzzlePane() {
		hangman = new Pane();
		drawStats();
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

}
