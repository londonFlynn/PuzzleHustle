package games;

import enums.PuzzleType;
import interfaces.ISolveable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Puzzle;
import models.User;

//NewPuzzlePublisher, Serializable, IExitable
public class MasterMind extends Puzzle implements ISolveable {
	public MasterMind(String filePath, PuzzleType PUZZLE_TYPE, User user) {
		super(filePath, PUZZLE_TYPE, user);
	}

	private MMLogic gameLogic = new MMLogic();
	private Circle[] circles = new Circle[6];
	private Color[] colors = { Color.RED, Color.BLUE, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.PURPLE };
	private Rectangle[] squares = new Rectangle[6];
	private double orgSceneX, orgSceneY;
	private Pane masterMind = null;
	private ImageView garbage = null;

	private void drawStats() {
		GridPane stats = new GridPane();
		Text text = new Text("Correct Color, Wrong Space: " + gameLogic.getRightColors());
		Text text2 = new Text("Correct Color, Right Space: " + gameLogic.getRightSpots());
		stats.add(text, 0, 0);
		stats.add(text2, 0, 1);
		masterMind.getChildren().add(stats);
		stats.setLayoutX(50);
		stats.setLayoutY(600);
	}

	private void resetStage(Boolean drawSquares) {
		masterMind.getChildren().clear();
		drawResetButton();
		drawSubmitButton();
		drawCircles();
		drawStats();
		if (drawSquares) {
			drawSquares();
		}
		drawStats();
		masterMind.getChildren().addAll(circles);
		masterMind.getChildren().addAll(squares);
	}

	private void drawResetButton() {
		Button reset = new Button("Reset");
		reset.setMinHeight(30);
		reset.setMinWidth(50);
		reset.setMaxHeight(30);
		reset.setMaxWidth(50);
		reset.setLayoutX(475);
		reset.setLayoutY(500);
		reset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				resetStage(true);
			}
		});
		masterMind.getChildren().add(reset);
	}

	private void drawSubmitButton() {
		Text errorMessage = new Text("You have to color all squares before submitting.");
		errorMessage.setX(375);
		errorMessage.setY(450);
		Button submit = new Button("Check Guess");
		submit.setMinHeight(30);
		submit.setMinWidth(100);
		submit.setMaxHeight(30);
		submit.setMaxWidth(50);
		submit.setLayoutX(475);
		submit.setLayoutY(600);
		submit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (gameLogic.checkGuess(squares)) {
					resetStage(false);
				} else {
					System.out.println("Failed");
					errorMessage.setVisible(true);
				}
			}
		});
		masterMind.getChildren().add(errorMessage);
		errorMessage.setVisible(false);
		masterMind.getChildren().add(submit);
	}

	private void drawOneCircle(Circle circle) {
		int x = 375;
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
			if (circle2.getBoundsInParent().intersects(garbage.getBoundsInParent())) {
				masterMind.getChildren().remove(circle2);
				System.out.println("Removed");
				Circle c = (Circle) (t.getSource());
				drawOneCircle(c);
			} else {
				for (Rectangle r : squares) {
					if (r.getBoundsInParent().intersects(circle2.getBoundsInParent())) {
						r.setFill(circle2.getFill());
						masterMind.getChildren().remove(circle2);
						System.out.println("Colored");
						Circle c = (Circle) (t.getSource());
						drawOneCircle(c);
					}
				}
			}
		});
	}

	private void drawCircles() {
		int xPos = 375;
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
		int xPos = 220;
		for (int i = 0; i < circles.length; i++) {
			Rectangle r = new Rectangle(xPos, 100, 60, 60);
			r.setFill(Color.BLACK);
			xPos += 100;
			squares[i] = r;
		}
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
		masterMind = new Pane();
		resetStage(true);
		drawStats();
//		scene.getStylesheets().add("/masterMind/application.css");
		gameLogic.createPuzzle();
		puzzlePane.getChildren().add(masterMind);
	}

	@Override
	public void showSolution() {
		// TODO Auto-generated method stub

	}

	@Override
	public Scene getScene() {
		if (scene == null) {
			super.getScene();
			setupPuzzlePane();
		}
		return scene;
	}

//	@Override
//	public void handle(ActionEvent arg0) {
//		//toggle boolean
//		isOn = !isOn;
//		
//		//based on boolean, change label style
//		bulb.setStyle(isOn ? ON : OFF);
//	}
}
