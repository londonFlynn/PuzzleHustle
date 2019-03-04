package models;

import java.io.Serializable;
import java.util.ArrayList;

import enums.PuzzleType;
import interfaces.IExitable;
import interfaces.NewPuzzlePublisher;
import interfaces.NewPuzzleSubscriber;
import interfaces.SubscribesToExitable;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import views.Display;

public abstract class Puzzle implements NewPuzzlePublisher, Serializable, IExitable {

	protected VBox puzzlePane = new VBox();
	protected Scene scene;
	private static final long serialVersionUID = 1L;
	private float score = 0;
	private boolean solved = false;
	private String filePath;
	public final PuzzleType PUZZLE_TYPE;
	private User user;
	protected Display display = new Display();
	private boolean instructionsMode = true;
	private Label highScoreLabel;
	private Label totalWinsLabel;
        private Label totalPlaysLabel;
	protected Label scoreLabel = new Label("Score: 0");
	protected long elapsedTimeTotal = 0;
	protected long startTime;
	protected AnimationTimer updater = new AnimationTimer() {
		@Override
		public void handle(long now) {
			long elapsedMillis = (System.currentTimeMillis() - startTime) + elapsedTimeTotal;
			Label time = (Label) display.getRightSidebar().getChildren().get(0);
			time.setText("Time: " + getTimerPresent(elapsedMillis));
			Label score = (Label) display.getRightSidebar().getChildren().get(1);
			score.setText("Score: " + getScore());
		}
	};
	
	
	private ArrayList<SubscribesToExitable> exitSubscribers = new ArrayList<>();
	
	private ArrayList<NewPuzzleSubscriber> puzzleSubscribers = new ArrayList<>();
	
	public Puzzle(String filePath, PuzzleType PUZZLE_TYPE, User user) {
		setFilePath(filePath);
		this.PUZZLE_TYPE = PUZZLE_TYPE;
		setUser(user);
		display.getLeftSidebar().getChildren().addAll(/*saveButton(),deleteButton(),*/instructionsButton());
		display.getName().getChildren().add(getPuzzleName());
		display.getNavigation().getChildren().add(exitButton());
		display.getNavigation().getChildren().add(newPuzzleButton());
		Label timeLabel = new Label("Time: 00:00.0");
		timeLabel.getStyleClass().add("gameStats");
		display.getRightSidebar().getChildren().add(timeLabel);
		display.getRightSidebar().getChildren().add(scoreLabel);
		Label userLabel = new Label("User: " + getUser().getName());
		userLabel.getStyleClass().add("gameStats");
		display.getRightSidebar().getChildren().add(userLabel);
		highScoreLabel = new Label("High Score: "+ user.getHighScore(PUZZLE_TYPE));
		highScoreLabel.getStyleClass().add("gameStats");
		display.getRightSidebar().getChildren().add(highScoreLabel);
		totalPlaysLabel = new Label("Total Plays: "+ user.getTotalPlays(PUZZLE_TYPE));
		totalPlaysLabel.getStyleClass().add("gameStats");
		display.getRightSidebar().getChildren().add(totalPlaysLabel);
		totalWinsLabel = new Label("Total Wins: "+ user.getTotalWins(PUZZLE_TYPE));
		totalWinsLabel.getStyleClass().add("gameStats");
		display.getRightSidebar().getChildren().add(totalWinsLabel);
		scoreLabel.getStyleClass().add("gameStats");
		timeLabel.setAlignment(Pos.CENTER);
		userLabel.setAlignment(Pos.CENTER);
		highScoreLabel.setAlignment(Pos.CENTER);
		totalPlaysLabel.setAlignment(Pos.CENTER);
		totalWinsLabel.setAlignment(Pos.CENTER);
		scoreLabel.setAlignment(Pos.CENTER);
	}
//	private Button saveButton() {
//		Button save = new Button("Save");
//		save.setMinSize(100, 40);
//		save.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				save();
//			}
//		});
//		return save;
//	}
//	
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
		Label nameLabel = new Label(name);
		nameLabel.getStyleClass().add("nameLabel");
		nameLabel.setAlignment(Pos.CENTER);
		return nameLabel;
		
	}
	
//	public void save() {
//		System.out.println(lib.FileIO.save(this, getFilePath()));
//	}
//	private Button deleteButton() {
//		Button delete = new Button("Delete");
//		delete.setMinSize(100, 40);
//		delete.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				delete();
//			}
//		});
//		return delete;
//	}
//	public void delete() {
//		lib.FileIO.delete(getFilePath());
//		exit();
//	}
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

	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
		if (score > user.getHighScore(PUZZLE_TYPE)) {
			user.setHighScore(PUZZLE_TYPE, score);
			highScoreLabel.textProperty().set("High Score: "+ user.getHighScore(PUZZLE_TYPE));
		}
		scoreLabel.textProperty().set("Score: " + getScore());
	}
	public boolean isSolved() {
		return solved;
	}
	public void setSolved(boolean solved) {
		if (solved = true) {
			user.setTotalWins(PUZZLE_TYPE, user.getTotalWins(PUZZLE_TYPE)+1);
			totalWinsLabel.textProperty().set("Total Wins: "+ user.getTotalWins(PUZZLE_TYPE));
		}
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
	
		protected void startTimer() {
		startTime = System.currentTimeMillis();
		updater.start();
	}
	
	protected void pauseTimer() {
		elapsedTimeTotal += System.currentTimeMillis() - startTime;
		updater.stop();
	}
	
	protected String getTimerPresent(long elapsedMillis){
		StringBuilder sb = new StringBuilder();
		
		int minutes = (int) (elapsedMillis / 60000);
		int seconds = (int) ((elapsedMillis / 1000) - (minutes * 60));
		int millSeconds = (int) (elapsedMillis - ((seconds * 1000) + minutes * 60000));
		
		if (minutes < 10) {
			if (minutes == 0) {
				sb.append("00:");
			} else {
				sb.append("0")
				.append(minutes)
				.append(":");
			}
		} else {
			sb.append(minutes)
			.append(":");
		}
		
		if (seconds < 10) {
			if (seconds == 0) {
				sb.append("00:");
			} else {
				sb.append("0")
				.append(seconds)
				.append(".");
			}
		} else {
			sb.append(seconds)
			.append(".");
		}
		
		if (millSeconds < 100) {
			sb.append("0");
		} else {
			sb.append(millSeconds/100);
		}
		return sb.toString();
		}
	
	

}
