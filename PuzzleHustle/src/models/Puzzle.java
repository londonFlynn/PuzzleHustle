package models;

import java.io.Serializable;
import java.util.ArrayList;

import enums.PuzzleType;
import interfaces.IExitable;
import interfaces.NewPuzzlePublisher;
import interfaces.NewPuzzleSubscriber;
import interfaces.SubscribesToExitable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import views.Display;

public abstract class Puzzle implements NewPuzzlePublisher, Serializable, IExitable {

	protected VBox puzzlePane = new VBox();
	protected Scene scene;
	private static final long serialVersionUID = 1L;
	private double timeSpent = 0;
	private float score = 0;
	private boolean solved = false;
	private String filePath;
	public final PuzzleType PUZZLE_TYPE;
	private User user;
	protected Display display = new Display();
	private boolean instructionsMode = true;
	private Label scoreLabel = new Label("Score: " +Float.toString(getScore()));
	
	private ArrayList<SubscribesToExitable> exitSubscribers = new ArrayList<>();
	
	private ArrayList<NewPuzzleSubscriber> puzzleSubscribers = new ArrayList<>();
	
	public Puzzle(String filePath, PuzzleType PUZZLE_TYPE, User user) {
		setFilePath(filePath);
		this.PUZZLE_TYPE = PUZZLE_TYPE;
		setUser(user);
		display.getLeftSidebar().getChildren().addAll(saveButton(),deleteButton(),instructionsButton());
		display.getName().getChildren().add(getPuzzleName());
		display.getNavigation().getChildren().add(exitButton());
		display.getNavigation().getChildren().add(newPuzzleButton());
		display.getRightSidebar().getChildren().add(new Label("Time: " +Double.toString(getTimeSpent())));
		display.getRightSidebar().getChildren().add(scoreLabel);
		display.getRightSidebar().getChildren().add(new Label("User: " + getUser().getName()));
		display.getRightSidebar().getChildren().add(new Label("High Score: "+ user.getHighScore(PUZZLE_TYPE)));
		display.getRightSidebar().getChildren().add(new Label("Total Plays: "+ user.getTotalPlays(PUZZLE_TYPE)));
		display.getRightSidebar().getChildren().add(new Label("Total Wins: "+ user.getTotalWins(PUZZLE_TYPE)));
	}
	private Button saveButton() {
		Button save = new Button("Save");
		save.setMinSize(100, 40);
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				save();
			}
		});
		return save;
	}
	
	private Label getPuzzleName() {
		String name;
		switch (PUZZLE_TYPE) {
		case SUDOKU:
			name = "Sudoku";
			break;
		case TWO048:
			name = "2048";
			break;
		case HANGMAN:
			name = "Hangman";
			break;
		case MASTERMIND:
			name = "Mastermind";
			break;
		case MINESWEEPER:
			name = "Minesweeper";
			break;
		default:
			name = "Name error";
		}
		return new Label(name);
		
	}
	
	public void save() {
		lib.FileIO.save(this, getFilePath());
	}
	private Button deleteButton() {
		Button delete = new Button("Delete");
		delete.setMinSize(100, 40);
		delete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				delete();
			}
		});
		return delete;
	}
	public void delete() {
		lib.FileIO.delete(getFilePath());
		exit();
	}
	public Scene getScene() {
		this.scene = display.getScene();
		this.puzzlePane = display.getMainView();
		return scene;
	}
	private Button exitButton() {
		Button exit = new Button("Exit to main menu");
		exit.setMinSize(100, 40);
		exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				exit();
			}
		});
		return exit;
	}
	
	@Override
	public void exit() {
		for (SubscribesToExitable subscriber : exitSubscribers) {
			subscriber.menuExited();
		}
	}
	
	private Button newPuzzleButton() {
		Button newPuzzle = new Button("New Puzzle");
		newPuzzle.setMinSize(100, 40);
		newPuzzle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				newPuzzle();
			}
		});
		return newPuzzle;
	}
	@Override
	public void newPuzzle() {
		for (NewPuzzleSubscriber subscriber : puzzleSubscribers) {
			subscriber.createNewPuzzle(this.PUZZLE_TYPE);
		}
	}
	private Button instructionsButton() {
		Button instruction = new Button("Instructions");
		instruction.setMinSize(100, 40);
		instruction.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (instructionsMode) {
					showInstructions();
				} else {
					hideInstructions();
				}
				instructionsMode = !instructionsMode;
			}
		});
		return instruction;
	}
	public abstract void showInstructions();
	
	public abstract void hideInstructions();
	
	public double getTimeSpent() {
		return timeSpent;
	}
	public void setTimeSpent(double timeSpent) {
		this.timeSpent = timeSpent;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
		scoreLabel.textProperty().set("Score: " + getScore());
	}
	public boolean isSolved() {
		return solved;
	}
	public void setSolved(boolean solved) {
		this.solved = solved;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public PuzzleType getPUZZLE_TYPE() {
		return PUZZLE_TYPE;
	}
	protected abstract void setupPuzzlePane();
	@Override
	public void exitSubcribe(SubscribesToExitable subscriber) {
		exitSubscribers.add(subscriber);
	}
	@Override
	public void exitUnSubcribe(SubscribesToExitable subscriber) {
		exitSubscribers.remove(subscriber);
		
	}
	@Override
	public void subcribe(NewPuzzleSubscriber subscriber) {
		puzzleSubscribers.add(subscriber);
	}
	@Override
	public void unSubcribe(NewPuzzleSubscriber subscriber) {
		puzzleSubscribers.remove(subscriber);
		
	}
	
	

}
