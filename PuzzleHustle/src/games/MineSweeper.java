package games;

import java.util.HashMap;
import java.util.Random;
import games.SurroundingFlagsLogic;
import interfaces.ISolveable;
import games.MinesSurroundingLogic;
import enums.Flag;
import enums.PuzzleType;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import models.Cell;
import models.Puzzle;
import models.User;

@SuppressWarnings("serial")
public class MineSweeper extends Puzzle implements ISolveable {
	
	public MineSweeper(String filePath, User user) {
		super(filePath, PuzzleType.MINESWEEPER, user);
	}

	public Cell[][] board;
	private boolean initialized;
	private int totalMines;
	private int flaggedCells;
	private int revealedCells;
	private Button[][] buttonBoard;
	private String minesRemainingText = "";
	private GridPane mineField; 
	private VBox root;
	private final Label INSTRUCTIONS = new Label("How to play Minesweeper:\n\nLeft Click: Reveal a Cell \n[Does not work if a "
			+ "cell has been marked with F]\nRight Click: Mark a cell. \n[Blank > Flag (F) > Unsure (?)]"
			+ "\nMiddle Click: Reveal all cells that are touching \nIF the number of Flags (F) surrounding is equal \nto the "
			+ "number on the cell.");
	private HashMap<Integer[], Cell> revealing = new HashMap<>();
	private Label minesRemainingLabel = new Label();

	public void run() {
		try {
			VBox superRoot = new VBox();
			
			setRoot(createRoot());
			setMineField(createMineFieldPane());

			getMinesRemainingLabel().getStyleClass().add("mineSweeperLabel");
			getRoot().getChildren().addAll(getMinesRemainingLabel(), getMineField());

			superRoot.getChildren().add(getRoot());
			superRoot.setAlignment(Pos.CENTER);
			display.getMainView().setAlignment(Pos.CENTER);
			display.getMainView().getChildren().add(superRoot);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int[] getDifficulty() {
		// [0] is the Width of the board, [1] is the Height, and [2] is the number of mines.
		int[] result = new int[3];
		
		ChoiceDialog<String> difficultySelector = new ChoiceDialog<String>("Easy", "Easy", "Medium", "Hard");
		difficultySelector.setTitle("Choose Difficulty");
		difficultySelector.setWidth(250);
		difficultySelector.setHeight(250);
		ButtonBar style = (ButtonBar) difficultySelector.getDialogPane().getChildren().get(2);
		style.getButtons().remove(1);
		difficultySelector.showAndWait();
		String dSResult = difficultySelector.getResult() != null ? difficultySelector.getResult() : difficultySelector.getDefaultChoice();
		
		if (dSResult.equals("Easy")) {
			result[0] = 10;
			result[1] = 10;
			result[2] = 10;
		} else if (dSResult.equals("Medium")) {
			result[0] = 10;
			result[1] = 10;
			result[2] = 20;
		} else {
			result[0] = 10;
			result[1] = 10;
			result[2] = 40;
		}
		return result;
	}
	
	private void createFirstMoveBoard(int boardHeight, int boardWidth) {
		Cell[][] temp = new Cell[boardHeight][boardWidth];
		for (Cell[] cell : temp) {
			for (int i = 0; i < cell.length; i++) {
				cell[i] = new Cell();
			}
		}

		setBoard(temp);
	}

	private void createBoard(int x, int y) {
		Random rng = new Random();
		for (int i = 0; i < getTotalMines(); i++) {
			boolean isInvalid = true;
			do {
				int randX = rng.nextInt(getBoard().length);
				int randY = rng.nextInt(getBoard()[randX].length);

				isInvalid = getBoard()[randX][randY].isMine();
				if (!isInvalid && (randX != x && randY != y)) {
					getBoard()[randX][randY].setMine(true);
				} else if (!isInvalid && !(randX != x && randY != y)) {
					isInvalid = true;
				}
			} while (isInvalid);
		}

		for (int i = 0; i < getBoard().length; i++) {
			for (int j = 0; j < getBoard()[i].length; j++) {
				getBoard()[i][j].setMinesTouching(MinesSurroundingLogic.run(i, j, getBoard()));
			}
		}

		createButtonActions();
		for (int i = 0; i < getBoard().length; i++) {
			for (int j = 0; j < getBoard()[i].length; j++) {
				getButtonBoard()[i][j].setText("");
			}
		}
		
		revealCell(x, y);
	}

	private void createButtonBoard() {
		int x = getBoard().length;
		int y = getBoard()[0].length;

		Button[][] temp = new Button[x][y];
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[i].length; j++) {
				temp[i][j] = new Button();
				temp[i][j].getStyleClass().add("cell");
			}
		}

		setButtonBoard(temp);
	}

	private void createButtonActions() {
		if (isInitialized()) {
			super.startTimer();
			for (int i = 0; i < getButtonBoard().length; i++) {
				for (int j = 0; j < getButtonBoard()[i].length; j++) {
					int pseudoI = i;
					int pseudoJ = j;
					getButtonBoard()[i][j].setOnMouseClicked(initializedAction(pseudoI, pseudoJ));
				}
			}
		} else {
			for (int i = 0; i < getButtonBoard().length && !isInitialized(); i++) {
				for (int j = 0; j < getButtonBoard()[i].length && !isInitialized(); j++) {
					int pseudoI = i;
					int pseudoJ = j;
					getButtonBoard()[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							if (event.getButton() == MouseButton.PRIMARY) {
								setInitialized(true);
								createBoard(pseudoI, pseudoJ);
							}							
						}
					});
				};
			}
		}
	}

	private void revealCell(int x, int y) {
		Cell workingCell = getBoard()[x][y];
		Button workingButton = getButtonBoard()[x][y];
		
		if (workingCell.getFlag() != Flag.FLAG && !workingCell.isRevealed()) {
			if (workingCell.isMine()) {
				workingCell.setRevealed(true);
				workingButton.setText("M");
			} else {
				workingButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("revealed"), true);
				String value = workingCell.getMinesTouching() > 0 ? workingCell.getMinesTouching() + "" : "";
				workingButton.setText("" + value);
				if (!workingCell.isRevealed()) {
					setRevealedCells(getRevealedCells() + 1);
					super.setScore(super.getScore() + 5);
				}
				workingCell.setRevealed(true);
			} 
		}
	}

	private void flagCell(int x, int y) {
		Cell workingCell = getBoard()[x][y];
		Button workingButton = getButtonBoard()[x][y];
		
		if (!workingCell.isRevealed()) {
			switch (workingCell.getFlag()) {
			case BLANK:
				workingCell.setFlag(Flag.FLAG);
				workingButton.setText("F");
				minesRemainingText = "Remaining Mines: " + (totalMines - ++flaggedCells);
				minesRemainingLabel.setText(minesRemainingText);
				break;
			case FLAG:
				workingCell.setFlag(Flag.QUESTION);
				workingButton.setText("Q");
				minesRemainingText = "Remaining Mines: " + (totalMines - --flaggedCells);
				minesRemainingLabel.setText(minesRemainingText);
				break;
			default:
				System.out.println("This shouldn't have appeared.");
			case QUESTION:
				workingCell.setFlag(Flag.BLANK);
				workingButton.setText("");
				break;
			}
		}
	}
	
	private void revealSurrounding(int x, int y) {
		Cell workingCell = getBoard()[x][y];
		boolean revealCells = false;
		
		if (workingCell.isRevealed()) {
			revealCells = SurroundingFlagsLogic.run(x, y, getBoard());
		}
		if (revealCells) {
			if (y - 1 >= 0) {
				revealCell(x, y-1);
				if (x - 1 >= 0) {
					revealCell(x-1, y-1);
				}
				if (x + 1 < getBoard()[0].length) {
					revealCell(x+1, y-1);
				}
			}
			if (x - 1 >= 0) {
				revealCell(x-1, y);
			}
			if (x + 1 < getBoard()[0].length) {
				revealCell(x+1, y);
			}
			if (y + 1 < getBoard().length) {
				revealCell(x, y+1);
				if (x - 1 >= 0) {
					revealCell(x-1, y+1);
				}
				if (x + 1 < getBoard()[0].length) {
					revealCell(x+1, y+1);
				}
			}
		}
		if (!getBoard()[x][y].isRevealed()) {
			super.setScore((float) (super.getScore() + 12.5));
		}
	}
	
	private GridPane createMineFieldPane() {
		GridPane mineField = new GridPane();
		setFlaggedCells(0);
		setRevealedCells(0);
		setTotalMines(0);
		setInitialized(false);
		
		mineField.setHgap(3);
		mineField.setVgap(3);
		
		int[] difficulty = getDifficulty();
		setTotalMines(difficulty[2]);
		createFirstMoveBoard(difficulty[0], difficulty[1]);
		createButtonBoard();
		createButtonActions();

		for (int i = 0; i < getButtonBoard().length; i++) {
			for (int j = 0; j < getButtonBoard()[i].length; j++) {
				mineField.add(getButtonBoard()[i][j], j, i);
			}
		}

		int width = (getButtonBoard()[0].length * 40) + ((getButtonBoard()[0].length - 1) * 3) + 10;
		int height = (getButtonBoard().length * 40) + ((getButtonBoard().length - 1) * 3) + 10;
		String style = "-fx-min-width: " + width + "; -fx-max-width: " + width + "; -fx-min-height: " + height
				+ "; -fx-max-height: " + height;
		mineField.setStyle(style);
		mineField.getStyleClass().add("mineField");
		
		setMinesRemainingText("Mines Remaining: " + getTotalMines());
		getMinesRemainingLabel().setText(getMinesRemainingText());
		getMinesRemainingLabel().setAlignment(Pos.CENTER);
		return mineField;
	}
	
	private VBox createRoot() {
		VBox root = new VBox();
		root.setAlignment(Pos.CENTER);
		root.setMinSize(600, 600);
		root.setMaxSize(600, 600);
		return root;
	}
	
	private void revealZero(int x, int y) {
		Integer[] temp = {x, y};
		Cell workingCell = getBoard()[x][y];
		revealing.put(temp, workingCell);
		revealCell(x, y);
		revealSurrounding(x, y);
	}
	
	private EventHandler<MouseEvent> initializedAction(int x, int y) {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					if (getBoard()[x][y].isMine()) {
						lose();
					} else if (getBoard()[x][y].getMinesTouching() > 0){
						revealCell(x, y);
						if (getRevealedCells() + getTotalMines() == getBoard().length * getBoard()[0].length) {
							pauseTimer();
							Alert winner = new Alert(AlertType.CONFIRMATION, "You Win!", ButtonType.OK);
							winner.showAndWait();
							setSolved(true);
							newPuzzle();
						}
					} else {
						revealZero(x, y);
					}
				} else if (event.getButton() == MouseButton.SECONDARY) {
					flagCell(x, y);
				} else {
					revealSurrounding(x, y);
				}
			}
		};
	}
	
	private void lose() {
		super.pauseTimer();
		for (int i = 0; i < getBoard().length; i++) {
			for (int j = 0; j< getBoard()[0].length; j++) {
				if (getBoard()[i][j].isMine()) {
					revealCell(i, j);
				}
			}
		}
		Alert gameOver = new Alert(AlertType.INFORMATION, "You've lost!", ButtonType.OK);
		gameOver.showAndWait();
		newPuzzle();
	}

	private void newGame() {
		// I am making this variable just to make the new game "refresh" look a bit cleaner.
		GridPane cosmetic = getMineField();
		setMineField(createMineFieldPane());
		getRoot().getChildren().remove(cosmetic);
		super.elapsedTimeTotal = 0;
		super.startTime = 0;
		super.setScore(0);
		Label temp = (Label) display.getRightSidebar().getChildren().get(0);
		temp.setText("Time: " + super.getTimerPresent(0));
//		getRoot().getChildren().add(getMineField());
		newPuzzle();
	}
	
	public Cell[][] getBoard() {
		return board;
	}

	public void setBoard(Cell[][] board) {
		this.board = board;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public int getTotalMines() {
		return totalMines;
	}

	public void setTotalMines(int totalMines) {
		this.totalMines = totalMines;
	}

	public int getFlaggedCells() {
		return flaggedCells;
	}

	public void setFlaggedCells(int flaggedCells) {
		this.flaggedCells = flaggedCells;
	}

	public Button[][] getButtonBoard() {
		return buttonBoard;
	}

	public void setButtonBoard(Button[][] buttonBoard) {
		this.buttonBoard = buttonBoard;
	}

	public int getRevealedCells() {
		return revealedCells;
	}

	public void setRevealedCells(int revealedCells) {
		this.revealedCells = revealedCells;
	}



	public String getMinesRemainingText() {
		return minesRemainingText;
	}



	public void setMinesRemainingText(String minesRemainingText) {
		this.minesRemainingText = minesRemainingText;
	}



	public Label getMinesRemainingLabel() {
		return minesRemainingLabel;
	}



	public void setMinesRemainingLabel(Label minesRemainingLabel) {
		this.minesRemainingLabel = minesRemainingLabel;
	}



	public GridPane getMineField() {
		return mineField;
	}



	public void setMineField(GridPane mineField) {
		this.mineField = mineField;
	}

	public VBox getRoot() {
		return root;
	}

	public void setRoot(VBox root) {
		this.root = root;
	}

	@Override
	public void showSolution() {
		for (int i = 0; i < getBoard().length; i++) {
			for (int j = 0; j< getBoard()[0].length; j++) {
				if (getBoard()[i][j].isMine()) {
					revealCell(i, j);
				}
			}
		}
		
		getMineField().setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				newGame();
			}
		});
	}

	@Override
	public void showInstructions() {
		super.pauseTimer();
		INSTRUCTIONS.getStyleClass().add("instructions");
		puzzlePane.setAlignment(Pos.TOP_CENTER);
		puzzlePane.getChildren().clear();
		puzzlePane.getChildren().addAll(INSTRUCTIONS);
	}

	@Override
	public void hideInstructions() {
		super.startTimer();
		puzzlePane.setAlignment(Pos.CENTER);
		puzzlePane.getChildren().clear();
		puzzlePane.getChildren().add(root);
		
	}

	@Override
	protected void setupPuzzlePane() {
		run();
	}
	
	@Override
	public Scene getScene() {
		if (scene == null) {
			setupPuzzlePane();
			super.getScene();
		}
		return scene;
	}

	
}
