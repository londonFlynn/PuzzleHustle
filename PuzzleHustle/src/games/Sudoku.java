package games;

import java.io.Serializable;

import enums.PuzzleType;
import interfaces.ISolveable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;
import models.Puzzle;
import models.User;

public class Sudoku extends Puzzle implements ISolveable, Serializable {

	private VBox boardView = new VBox();
	private VBox sudoku = new VBox();
	private SudokuLogic logic;
	private static final long serialVersionUID = 1L;
	private int[][][][] board;
	private int[][][][] solution;
	private int boardSize = 3;
	private boolean canWin = true;
	private final Label INSTRUCTIONS = new Label("How to Play Sudoku!"
			+ "\nEach puzzle consists of a 9x9 grid containing \ngiven clues in various places. "
			+ "\nThe object is to fill all empty squares so that the \nnumbers 1 to 9 appear exactly once in each row, \ncolumn and 3x3 box.");

	public Sudoku(String filePath, User user) {
		super(filePath, PuzzleType.SUDOKU, user);
		boardView.setAlignment(Pos.CENTER);
		
		boardView.getStyleClass().add("sudokuBoard");
		
		INSTRUCTIONS.setAlignment(Pos.CENTER);
		
		INSTRUCTIONS.getStyleClass().add("instructions");
		
		logic = new SudokuLogic(boardSize);
		board = logic.generatePuzzle();
		solution = logic.getSolution();
	}

	private void enterNumber(int newNumber, int rowIn, int columnIn, int row, int column) {
		board[rowIn][columnIn][row][column] = newNumber;
		if (newNumber == solution[rowIn][columnIn][row][column]) {
			setScore(getScore()+10);
		} else if (newNumber != 0){
			setScore(getScore()-8);
		}
		if (SudokuLogic.isSolved(board)) {
			setupBoard(true);
			if (canWin) {
			setSolved(true);
			}
		}
	}
	
	//TODO hint
	
	//TODO check if valid
	
	//TODO notes
	
	//TODO score

	private VBox setupBoard(boolean solvedVersion) {
		int cellLength = logic.CELL_LENGTH;
		boardView.getChildren().clear();
		boardView.setAlignment(Pos.CENTER);
		boardView.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		boardView.setMinSize(50 * cellLength * cellLength + 2, 50 * cellLength * cellLength + 2);
		boardView.setMaxSize(50 * cellLength * cellLength + 2, 50 * cellLength * cellLength + 2);
		for (int i = 0; i < cellLength; i++) {
			HBox row = new HBox();
			row.setAlignment(Pos.CENTER);
			for (int j = 0; j < cellLength; j++) {
				VBox masterCell = new VBox();
				masterCell.setAlignment(Pos.CENTER);
				masterCell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
						CornerRadii.EMPTY, BorderWidths.DEFAULT)));
				masterCell.setPadding(new Insets(10, 10, 10, 10));
				masterCell.setMinSize(50 * cellLength, 50 * cellLength);
				masterCell.setMaxSize(50 * cellLength, 50 * cellLength);
				for (int k = 0; k < cellLength; k++) {
					HBox cellRow = new HBox();
					cellRow.setAlignment(Pos.CENTER);
					for (int l = 0; l < cellLength; l++) {
						cellRow.getChildren().add(setupCell(solvedVersion, i, j, k, l));
					}
					masterCell.getChildren().add(cellRow);
				}
				row.getChildren().add(masterCell);
			}
			boardView.getChildren().add(row);
		}
		return boardView;
	}

	private VBox setupCell(boolean solvedVersion, int rowIn, int columnIn, int row, int column) {
		VBox cell = new VBox();
		cell.setMinSize(50, 50);
		cell.setMaxSize(50, 50);
		cell.setPadding(new Insets(2, 2, 2, 2));
		cell.setAlignment(Pos.CENTER);
		cell.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		cell.getChildren()
				.add((logic.getCellValue(rowIn, columnIn, row, column) != 0 || solvedVersion)
						? setupStaticNumber(rowIn, columnIn, row, column)
						: setupEditableNumberField(rowIn, columnIn, row, column));
		return cell;
	}

	private Label setupStaticNumber(int rowIn, int columnIn, int row, int column) {
		Label staticNumber = new Label(Integer.toString(solution[rowIn][columnIn][row][column]));
		staticNumber.setAlignment(Pos.CENTER);
		return staticNumber;
	}

	private LimitedTextField setupEditableNumberField(int rowIn, int columnIn, int row, int column) {
		LimitedTextField editableNumber = new LimitedTextField();
		editableNumber.setMaxLength(1);
		editableNumber.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
		editableNumber.alignmentProperty().set(Pos.CENTER);
		editableNumber.setMinSize(48, 48);
		editableNumber.setMaxSize(48, 48);
		editableNumber.textProperty().addListener((observable, oldValue, newValue) -> {
			int value;
			try {
				value = Integer.parseInt(newValue);
				if (value == 0) {
					editableNumber.textProperty().set("");
				}
			} catch (NumberFormatException nfe) {
				editableNumber.textProperty().set("");
				value = 0;
			}
			enterNumber(value, rowIn, columnIn, row, column);
		});
		return editableNumber;
	}

	@Override
	public void showInstructions() {
		puzzlePane.setAlignment(Pos.TOP_CENTER);
		puzzlePane.getChildren().clear();
		puzzlePane.getChildren().addAll(INSTRUCTIONS);
	}

	@Override
	public void hideInstructions() {
		puzzlePane.setAlignment(Pos.CENTER);
		puzzlePane.getChildren().clear();
		puzzlePane.getChildren().add(sudoku);
	}

	
	private void clearBoard() {
		setupBoard(false);
	}

	private Button resetButton() {
		Button reset = new Button("Reset Board");
		reset.setMinSize(100, 40);
		reset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				clearBoard();
			}
		});
		return reset;
	}

	private Button solutionButton() {
		Button solutionButton = new Button("Show Solution");
		solutionButton.setMinSize(100, 40);
		solutionButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showSolution();
				canWin = false;
			}
		});
		return solutionButton;
	}

	@Override
	protected void setupPuzzlePane() {
		sudoku.getChildren().addAll(setupBoard(false));
		sudoku.setAlignment(Pos.CENTER);
		
		puzzlePane.setAlignment(Pos.CENTER);
		
		
		puzzlePane.getChildren().add(sudoku);
		
		display.getLeftSidebar().getChildren().addAll(solutionButton(), resetButton());
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
	public void showSolution() {
		setupBoard(true);
	}
}
