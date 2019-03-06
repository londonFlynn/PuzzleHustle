package games;

import java.util.Random;

import controllers.MusicManager;
import enums.PuzzleType;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import models.Puzzle;
import models.User;

@SuppressWarnings("serial")
public class TwentyFourtyEight extends Puzzle {
	private Tile[][] tiles = new Tile[4][4];
	private int[][] valueCheck = new int[4][4];
	private Random rng = new Random();
	private Label gameOver = new Label("\r\rGameOver\r\rFinal Score: " + getScore());
	private GridPane gridPane = new GridPane();
	private boolean isOver = false;

	public TwentyFourtyEight(String filePath, User user) {
		
		super(filePath, PuzzleType.TWO048, user);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void showInstructions() {
		pauseTimer();
		puzzlePane.setAlignment(Pos.TOP_CENTER);
		Label instructions = new Label(
				"\rHow To Play: \rUse the W,A,S,D keys to slide the tiles\r and combine like numbers\rTry to get the highest score you can!");
		instructions.getStyleClass().add("instructions");
		puzzlePane.getChildren().clear();
		puzzlePane.getChildren().add(instructions);

	}

	@Override
	public void hideInstructions() {
		if(!isOver) {
			
			startTimer();
		}
		puzzlePane.setAlignment(Pos.CENTER);
		puzzlePane.getChildren().clear();
		if (isOver) {
			puzzlePane.getChildren().add(gameOver);
		} else {
			puzzlePane.getChildren().add(gridPane);
		}

	}

	@Override
	protected void setupPuzzlePane() {
		run();
		puzzlePane.getChildren().add(gridPane);
		startTimer();

	}

	@Override
	public Scene getScene() {
		if (scene == null) {
			super.getScene();
			setupPuzzlePane();
		}
		return scene;
	}

	// 2048 specific methods:
	public void run() {
		gameOver.setAlignment(Pos.CENTER);
		gameOver.getStyleClass().add("instructions");
		try {
			initGrid(gridPane);
			randomizeStart();
			scene.setOnKeyPressed(e -> {
				if (e.getCode() == KeyCode.A) {
					valueCheck = copiedValues();
					swipeLeft();
					if(hasChanged()) {
						MusicManager.playSlide();
					}
				} else if (e.getCode() == KeyCode.D) {
					valueCheck = copiedValues();
					swipeRight();
					if(hasChanged()) {
						MusicManager.playSlide();
					}
				} else if (e.getCode() == KeyCode.W) {
					valueCheck = copiedValues();
					swipeUp();
					if(hasChanged()) {
						MusicManager.playSlide();
					}
				} else if (e.getCode() == KeyCode.S) {
					valueCheck = copiedValues();
					swipeDown();
					if(hasChanged()) {
						MusicManager.playSlide();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void reset() {
//		for (int i = 0; i < tiles.length; i++) {
//			for (int j = 0; j < tiles[i].length; j++) {
//				tiles[i][j].setValue(0);
//			}
//		}
//		randomizeStart();
		pauseTimer();
		puzzlePane.setAlignment(Pos.TOP_CENTER);
		puzzlePane.getChildren().clear();
		gameOver.setAlignment(Pos.TOP_CENTER);
		gameOver.setText("\r\rGameOver\r\rFinal Score: " + getScore());
		puzzlePane.getChildren().add(gameOver);
	}

	private void checkLoss() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				if (tiles[i][j].getValue() == 0) {
					return;
				}
			}
		}
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				if (j < tiles.length - 1 && i < tiles.length - 1) {
					if (tiles[i + 1][j].getValue() == tiles[i][j].getValue()
							|| tiles[i][j + 1].getValue() == tiles[i][j].getValue()) {
						return;
					}
				} else if (j == tiles.length - 1 && i < tiles.length - 1) {
					if (tiles[i + 1][j].getValue() == tiles[i][j].getValue()) {
						return;
					}
				} else if (j < tiles.length - 1 && i == tiles.length - 1) {
					if (tiles[i][j + 1].getValue() == tiles[i][j].getValue()) {
						return;
					}
				} else {
					isOver = true;
					reset();
					return;
				}
			}
		}
	}

	private void swipeUp() {
		boolean hasChanged = false;
		fillUp();
		for (Tile[] tArr : tiles) {
			for (int i = 0; i < tArr.length; i++) {
				if (tArr[i].getValue() != 0) {
					if (i - 1 >= 0) {
						if (tArr[i - 1].getValue() == tArr[i].getValue()) {
							tArr[i - 1].setValue(tArr[i - 1].getValue() + tArr[i].getValue());
							tArr[i].setValue(0);
							addScore(tArr[i - 1].getValue() + tArr[i].getValue());
							ScaleTransition transition = new ScaleTransition(Duration.seconds(.2),tArr[i-1].getLabel());
							transition.setFromY(1.2);
							transition.setToY(1);
							transition.setFromX(1.2);
							transition.setToX(1);
							transition.play();
							fillUp();
						} else if (tArr[i - 1].getValue() == 0) {
							tArr[i - 1].setValue(tArr[i].getValue());
							tArr[i].setValue(0);
							fillUp();
						}
					}
				}
			}
		}
		hasChanged = hasChanged();
		if (hasChanged == true) {
			spawnTile();
		}
		checkLoss();
	}

	private void swipeDown() {
		boolean hasChanged = false;
		fillDown();
		for (Tile[] tArr : tiles) {
			for (int i = tArr.length - 1; i >= 0; i--) {
				if (tArr[i].getValue() != 0) {
					if (i + 1 <= tArr.length - 1) {
						if (tArr[i + 1].getValue() == tArr[i].getValue()) {
							tArr[i + 1].setValue(tArr[i + 1].getValue() + tArr[i].getValue());
							tArr[i].setValue(0);
							addScore(tArr[i + 1].getValue() + tArr[i].getValue());
							ScaleTransition transition = new ScaleTransition(Duration.seconds(.2),tArr[i+1].getLabel());
							transition.setFromY(1.2);
							transition.setToY(1);
							transition.setFromX(1.2);
							transition.setToX(1);
							transition.play();
							fillDown();
						} else if (tArr[i + 1].getValue() == 0) {
							tArr[i + 1].setValue(tArr[i].getValue());
							tArr[i].setValue(0);
							fillDown();
						}
					}
				}
			}
		}
		hasChanged = hasChanged();
		if (hasChanged == true) {
			spawnTile();
		}
		checkLoss();
	}

	private void swipeLeft() {
		boolean hasChanged = false;
		fillLeft();
		for (int i = 0; i < tiles[0].length; i++) {
			for (int j = 0; j < tiles.length; j++) {
				if (tiles[j][i].getValue() != 0) {
					if (j - 1 >= 0) {
						if (tiles[j - 1][i].getValue() == tiles[j][i].getValue()) {
							tiles[j - 1][i].setValue(tiles[j - 1][i].getValue() + tiles[j][i].getValue());
							tiles[j][i].setValue(0);
							addScore(tiles[j - 1][i].getValue() + tiles[j][i].getValue());
							ScaleTransition transition = new ScaleTransition(Duration.seconds(.2),tiles[j-1][i].getLabel());
							transition.setFromY(1.2);
							transition.setToY(1);
							transition.setFromX(1.2);
							transition.setToX(1);
							transition.play();
							fillLeft();
						} else if (tiles[j - 1][i].getValue() == 0) {
							tiles[j - 1][i].setValue(tiles[j][i].getValue());
							tiles[j][i].setValue(0);
							fillLeft();
						}
					}
				}
			}
		}
		hasChanged = hasChanged();
		if (hasChanged == true) {
			spawnTile();
		}
		checkLoss();
	}

	private void swipeRight() {
		boolean hasChanged = false;
		fillRight();
		for (int i = tiles[0].length - 1; i >= 0; i--) {
			for (int j = tiles.length - 1; j >= 0; j--) {
				if (tiles[j][i].getValue() != 0) {
					if (j + 1 < tiles.length) {
						if (tiles[j + 1][i].getValue() == tiles[j][i].getValue()) {
							tiles[j + 1][i].setValue(tiles[j + 1][i].getValue() + tiles[j][i].getValue());
							tiles[j][i].setValue(0);
							addScore(tiles[j + 1][i].getValue() + tiles[j][i].getValue());
							ScaleTransition transition = new ScaleTransition(Duration.seconds(.2),tiles[j+1][i].getLabel());
							transition.setFromY(1.2);
							transition.setToY(1);
							transition.setFromX(1.2);
							transition.setToX(1);
							transition.setToX(1);
							transition.play();
							fillRight();
						} else if (tiles[j + 1][i].getValue() == 0) {
							tiles[j + 1][i].setValue(tiles[j][i].getValue());
							tiles[j][i].setValue(0);
							fillRight();
						}
					}
				}
			}
		}
		hasChanged = hasChanged();
		if (hasChanged == true) {
			spawnTile();
		}
		checkLoss();
	}

	private void fillRight() {
		for (int h = 0; h < 5; h++) {
			for (int i = tiles[0].length - 1; i >= 0; i--) {
				for (int j = tiles.length - 1; j >= 0; j--) {
					if (tiles[j][i].getValue() != 0) {
						if (j + 1 < tiles.length) {
							if (tiles[j + 1][i].getValue() == 0) {
								ScaleTransition transition = new ScaleTransition(Duration.seconds(.2),tiles[j][i].getLabel());
								transition.setFromX(.95);
								transition.setToX(1);
								transition.setFromY(.95);
								transition.setToY(1);
								transition.play();
								tiles[j + 1][i].setValue(tiles[j][i].getValue());
								tiles[j][i].setValue(0);
							}
						}
					}
				}
			}
		}
	}

	private void fillLeft() {
		for (int h = 0; h < 5; h++) {
			for (int i = 0; i < tiles[0].length; i++) {
				for (int j = 0; j < tiles.length; j++) {
					if (tiles[j][i].getValue() != 0) {
						if (j - 1 >= 0) {
							if (tiles[j - 1][i].getValue() == 0) {
								ScaleTransition transition = new ScaleTransition(Duration.seconds(.2),tiles[j][i].getLabel());
								transition.setFromX(.95);
								transition.setToX(1);
								transition.setFromY(.95);
								transition.setToY(1);
								transition.play();
								tiles[j - 1][i].setValue(tiles[j][i].getValue());
								tiles[j][i].setValue(0);
							}
						}
					}
				}
			}
		}
	}

	private void fillUp() {
		for (int j = 0; j < 5; j++) {
			for (Tile[] tArr : tiles) {
				for (int i = 0; i < tArr.length; i++) {
					if (tArr[i].getValue() != 0) {
						if (i - 1 >= 0) {
							if (tArr[i - 1].getValue() == 0) {
								ScaleTransition transition = new ScaleTransition(Duration.seconds(.2),tArr[i].getLabel());
								transition.setFromX(.95);
								transition.setToX(1);
								transition.setFromY(.95);
								transition.setToY(1);
								transition.play();
								tArr[i - 1].setValue(tArr[i].getValue());
								tArr[i].setValue(0);
							}
						}
					}
				}
			}
		}
	}

	private void fillDown() {
		for (int j = 0; j < 5; j++) {
			for (Tile[] tArr : tiles) {
				for (int i = tArr.length - 1; i >= 0; i--) {
					if (tArr[i].getValue() != 0) {
						if (i + 1 <= tArr.length - 1) {
							if (tArr[i + 1].getValue() == 0) {
								ScaleTransition transition = new ScaleTransition(Duration.seconds(.2),tArr[i].getLabel());
								transition.setFromX(.95);
								transition.setToX(1);
								transition.setFromY(.95);
								transition.setToY(1);
								transition.play();
								tArr[i + 1].setValue(tArr[i].getValue());
								tArr[i].setValue(0);
							}
						}
					}
				}
			}
		}
	}

	private void addScore(int i) {
		setScore(getScore() + i);
	}

	private boolean hasChanged() {
		boolean returnBool = false;
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				if (tiles[i][j].getValue() != valueCheck[i][j]) {
					returnBool = true;
					break;
				}
			}
		}
		return returnBool;
	}

	private void randomizeStart() {
		for (int i = 0; i < 2; i++) {
			spawnTile();
		}

	}

	private void spawnTile() {
		boolean isTaken = false;
		boolean hasOpenSlot = false;
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				if (tiles[i][j].getValue() == 0) {
					MusicManager.playSpawn();
					hasOpenSlot = true;
					break;
				}
			}
		}
		if (hasOpenSlot) {
			do {
				int x = rng.nextInt(4);
				int y = rng.nextInt(4);
				int num;
				if (rng.nextBoolean()) {
					num = 2;
				} else {
					num = 4;
				}
				if (tiles[x][y].getValue() != 0) {
					isTaken = true;
				} else {
					ScaleTransition transition = new ScaleTransition(Duration.seconds(.2),tiles[x][y].getLabel());
					transition.setFromX(.7);
					transition.setFromY(.7);
					transition.setToX(1);
					transition.setToY(1);
					transition.play();
					tiles[x][y].setValue(num);
					isTaken = false;
				}
			} while (isTaken);
		}
	}

	private int[][] copiedValues() {
		int[][] iArr = new int[4][4];
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				iArr[i][j] = tiles[i][j].getValue();
			}
		}
		return iArr;
	}

	private void initGrid(GridPane gridPane) {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				tiles[i][j] = new Tile();
				gridPane.add(tiles[i][j].getLabel(), i, j, 1, 1);
			}
		}
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setMinSize(500, 500);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.getStyleClass().add("mainView");
		gridPane.setStyle("-fx-padding: 10 ;");
	}
}
