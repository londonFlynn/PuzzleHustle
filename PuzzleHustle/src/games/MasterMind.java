package games;

import enums.PuzzleType;
import interfaces.ISolveable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.Puzzle;
import models.User;

@SuppressWarnings("serial")
public class MasterMind extends Puzzle implements ISolveable {
	public MasterMind(String filePath, User user) {
		super(filePath, PuzzleType.MASTERMIND, user);
	}

	private MMLogic gameLogic = new MMLogic();
	private Circle[] circles = null;
	private Color[] colors = { Color.RED, Color.BLUE, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.PURPLE,
			Color.CHOCOLATE, Color.HOTPINK, Color.BISQUE };
	private Rectangle[] squares = null;
	private double orgSceneX, orgSceneY;
	private Pane masterMind = null;
	private Text stats;
	private Button giveUp = new Button("Give Up");
	private boolean isDone = false;
	private boolean isWon = false;
	private int difficulty = 1;
	private int length = 1;
	private Label endMessage = new Label();

	private void drawStats() {
		stats = new Text("Correct Color, Wrong Space: " + gameLogic.getRightColors() + "\nCorrect Color, Right Space: "
				+ gameLogic.getRightSpots() + "\nGuesses Remaining: " + gameLogic.getGuessesLeft());
		stats.setX(10);
		stats.setY(20);
		stats.setId("stats");
	}

	private void gameWon() {
		isDone = true;
		isWon = true;
		setScore(gameLogic.getGuessesLeft() * difficulty);
		showSolution();
		setSolved(true);
	}

	private void resetStage(Boolean drawSquares) {
		masterMind.getChildren().clear();
		drawResetButton();
		drawGiveUpButton();
		drawSubmitButton();
		drawCircles();
		drawStats();
		if (drawSquares) {
			drawSquares();
		}
		masterMind.getChildren().add(stats);
		masterMind.getChildren().addAll(circles);
		masterMind.getChildren().addAll(squares);
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

	private void drawResetButton() {
		Button reset = new Button("Reset");
		reset.setId("resetButton");
		reset.setLayoutX(445);
		reset.setLayoutY(460);
		reset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				resetStage(true);
			}
		});
		masterMind.getChildren().add(reset);
	}

	private void checkSubmission() {
		Text errorMessage = new Text("You have to color all squares before submitting.");
		masterMind.getChildren().add(errorMessage);
		errorMessage.setVisible(false);
		errorMessage.setId("message");
		errorMessage.setX(340);
		errorMessage.setY(250);
		gameLogic.checkGuess(squares);
		if (gameLogic.canCheck()) {
			if (gameLogic.getRightSpots() == length) {
				gameWon();
			} else if (gameLogic.getGuessesLeft() == 0) {
				showSolution();
			} else {
				resetStage(false);
			}

		} else {
			errorMessage.setVisible(true);
		}
	}

	private void drawSubmitButton() {
		Button submit = new Button("Check Guess");
		submit.setId("submitButton");
		submit.setLayoutX(405);
		submit.setLayoutY(400);
		submit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				checkSubmission();
			}
		});
		masterMind.getChildren().add(submit);
	}

	private void drawOneCircle(Circle circle) {
		int x = 355;
		if (circle.getFill() == Color.BLUE) {
			x += 50;
		} else if (circle.getFill() == Color.ORANGE) {
			x += 100;
		} else if (circle.getFill() == Color.YELLOW) {
			x += 150;
		} else if (circle.getFill() == Color.GREEN) {
			x += 200;
		} else if (circle.getFill() == Color.PURPLE) {
			x += 250;
		}
		if (difficulty == 1) {
			x += 75;
		} else if (difficulty == 3) {
			x -= 75;
		}

		Circle circle2 = new Circle(x, 350, 20, circle.getFill());
		circle2.setCursor(Cursor.HAND);
		masterMind.getChildren().add(circle2);
		circle2.setOnMousePressed((t) -> {
			orgSceneX = t.getSceneX();
			orgSceneY = t.getSceneY();

			Circle c = (Circle) (t.getSource());
			c.toFront();
		});
		circle2.setOnMouseDragged((t) -> {
			double offsetX = t.getSceneX() - orgSceneX;
			double offsetY = t.getSceneY() - orgSceneY;

			Circle c = (Circle) (t.getSource());

			c.setCenterX(c.getCenterX() + offsetX);
			c.setCenterY(c.getCenterY() + offsetY);

			orgSceneX = t.getSceneX();
			orgSceneY = t.getSceneY();
		});
		circle2.setOnMouseReleased((t) -> {
			for (Rectangle r : squares) {
				if (r.getBoundsInParent().intersects(circle2.getBoundsInParent())) {
					r.setFill(circle2.getFill());
					masterMind.getChildren().remove(circle2);
					System.out.println("Colored");
					Circle c = (Circle) (t.getSource());
					drawOneCircle(c);
				}
			}
		});
	}

	private void drawCircles() {
		int xPos = 355;
		if (difficulty == 1) {
			xPos += 75;
		} else if (difficulty == 3) {
			xPos -= 75;
		}
		for (int i = 0; i < circles.length; i++) {
			Circle circle = new Circle(xPos, 350, 20, colors[i]);
			circle.setCursor(Cursor.HAND);
			circle.setOnMousePressed((t) -> {
				orgSceneX = t.getSceneX();
				orgSceneY = t.getSceneY();
				Circle c = (Circle) (t.getSource());
				c.toFront();
			});
			circle.setOnMouseDragged((t) -> {
				double offsetX = t.getSceneX() - orgSceneX;
				double offsetY = t.getSceneY() - orgSceneY;

				Circle c = (Circle) (t.getSource());

				c.setCenterX(c.getCenterX() + offsetX);
				c.setCenterY(c.getCenterY() + offsetY);

				orgSceneX = t.getSceneX();
				orgSceneY = t.getSceneY();
			});
			circle.setOnMouseReleased((t) -> {
				for (Rectangle r : squares) {
					if (r.getBoundsInParent().intersects(circle.getBoundsInParent())) {
						r.setFill(circle.getFill());
						masterMind.getChildren().remove(circle);
						System.out.println("Colored");
						Circle c = (Circle) (t.getSource());
						drawOneCircle(c);
					}
				}
			});
			xPos += 50;
			circles[i] = circle;
		}
	}

	private void drawSquares() {
		int xPos = 200;
		if (difficulty == 1) {
			xPos += 150;
		} else if (difficulty == 3) {
			xPos -= 150;
		}
		for (int i = 0; i < circles.length; i++) {
			Rectangle r = new Rectangle(xPos, 100, 60, 60);
			r.setFill(Color.BLACK);
			xPos += 100;
			squares[i] = r;
		}
	}

	private void drawDifficultyPage() {
		Label menuMessage = new Label();
		menuMessage.setText("Please choose a difficulty");
		menuMessage.setId("message2");
		Button button1 = new Button("Easy");
		Button button2 = new Button("Medium");
		Button button3 = new Button("Hard");
		button1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				difficulty = 1;
				length = 3;
				beginGame();
			}
		});
		button2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				difficulty = 2;
				length = 6;
				beginGame();
			}
		});
		button3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				difficulty = 3;
				length = 9;
				beginGame();
			}
		});
		masterMind.getChildren().addAll(button1, button2, button3, menuMessage);
		menuMessage.setTranslateX(200);
		menuMessage.setTranslateY(100);
		button1.setTranslateX(205);
		button1.setTranslateY(250);
		button2.setTranslateX(405);
		button2.setTranslateY(250);
		button3.setTranslateX(605);
		button3.setTranslateY(250);
	}

	private void beginGame() {
		masterMind.getChildren().clear();
		circles = new Circle[length];
		squares = new Rectangle[length];
		startTimer();
		resetStage(true);
		drawStats();
		gameLogic.createPuzzle(length);
	}

	@Override
	public void showInstructions() {
		pauseTimer();
		masterMind.getChildren().clear();
		Text instructions = new Text("Drag the colored circles to the squares to color them. After "
				+ "coloring all of the squares, you can check your guess. The "
				+ "top left corner\nof the screen will tell how many colors are "
				+ "both correct and in the correct square, and will also tell"
				+ " you how many colors are correct\nbut in the incorrect square."
				+ "\n\nEasy difficulty has 3 colors to guess, Medium has 6, and Hard has 9."
				+ "\nWith each difficulty, you get 20 guesses to figure out which colors go in which "
				+ "square to match the secret pattern."
				+ "\n\nThe score is based on the amount of tries left after winning. If on medium difficulty, "
				+ "the tries are doubled to produce the score. If on\nhard difficulty, the tries are tripled.");
		instructions.setId("instructions");
		instructions.setY(100);
		instructions.setX(10);
		masterMind.getChildren().add(instructions);
	}

	@Override
	public void hideInstructions() {
		startTimer();
		if (!isDone) {
			masterMind.getChildren().clear();
			resetStage(true);
		} else {
			showSolution();
		}
	}

	@Override
	protected void setupPuzzlePane() {
		masterMind = new Pane();
		drawDifficultyPage();
		puzzlePane.getChildren().add(masterMind);
		puzzlePane.getStylesheets().add("views/MM.css");
	}

	@Override
	public void showSolution() {
		endMessage.setTranslateX(380);
		endMessage.setTranslateY(250);
		endMessage.setId("endMessage");
		if (isWon) {
			endMessage.setText("You WON!!");
		} else {
			endMessage.setText("You Lose");
		}
		masterMind.getChildren().clear();
		drawSquares();
		masterMind.getChildren().addAll(squares);
		masterMind.getChildren().add(endMessage);
		Color[] answer = gameLogic.getAnswer();
		for (int i = 0; i < squares.length; i++) {
			squares[i].setFill(answer[i]);
		}
		pauseTimer();
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
		if (score > user.getHighScore(PUZZLE_TYPE) && isWon) {
			user.setHighScore(PUZZLE_TYPE, score);
			highScoreLabel.textProperty().set("High Score: " + user.getHighScore(PUZZLE_TYPE));
		}
		scoreLabel.textProperty().set("Score: " + getScore());
	}
}
