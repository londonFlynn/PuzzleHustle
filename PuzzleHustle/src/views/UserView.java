package views;

import java.util.ArrayList;

import enums.PuzzleType;
import interfaces.IExitable;
import interfaces.SubscribesToExitable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import models.User;

public class UserView implements IExitable {
	private Display display = new Display();
	private User user;
	private SubscribesToExitable subscriber;
	
	public UserView(User user, SubscribesToExitable exitSubscriber) {
		this.user = user;
		exitSubcribe(exitSubscriber);
		display.getNavigation().getChildren().add(exitButton());
	}
	public Scene getScene() {
		setupUserScene();
		return display.getScene();
	}
	private void setupUserScene() {
		setupHighScores();
		setupTotalPlays();
		setupTotalWins();
	}
	private void setupHighScores() {
		ArrayList<Label> highScores = new ArrayList<>();
		for (PuzzleType puzzleType : PuzzleType.values()) {
			highScores.add(new Label(getPuzzleName(puzzleType) + " high score: " + user.getHighScore(puzzleType)));
		}
		display.getRightSidebar().getChildren().addAll(highScores);
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
	
	private String getPuzzleName(PuzzleType puzzleType) {
		String name;
		switch (puzzleType) {
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
		return name;
		
	}
	private void setupTotalPlays() {
		
	}
	private void setupTotalWins() {
		
	}
	@Override
	public void exitSubcribe(SubscribesToExitable subscriber) {
		this.subscriber = subscriber;
	}
	@Override
	public void exitUnSubcribe(SubscribesToExitable subscriber) {
		this.subscriber = null;
	}
	@Override
	public void exit() {
		subscriber.menuExited();
	}
	

}
