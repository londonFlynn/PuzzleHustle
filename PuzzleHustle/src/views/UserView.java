package views;

import java.text.DecimalFormat;

import enums.PuzzleType;
import interfaces.IExitable;
import interfaces.SubscribesToExitable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.User;

public class UserView implements IExitable {
	private Display display = new Display();
	private User user;
	private SubscribesToExitable subscriber;
	
	public UserView(User user, SubscribesToExitable exitSubscriber) {
		Label viewName = new Label(user.getName());
		viewName.getStyleClass().add("nameLabel");
		viewName.setAlignment(Pos.CENTER);
		display.getName().getChildren().add(viewName);
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
		GridPane fullView = new GridPane();
		VBox[] VBoxes = new VBox[6];
		
		Label userName = new Label("All Games");
		int generalTotalPlays = 0;
		for (PuzzleType puzzleType : PuzzleType.values()) {
			generalTotalPlays += user.getTotalPlays(puzzleType);
		}
		int generalTotalWins = 0;
		for (PuzzleType puzzleType : PuzzleType.values()) {
			generalTotalWins += user.getTotalWins(puzzleType);
		} 
		float generalWinPercent = generalTotalPlays != 0 ? ((float)generalTotalWins / (float)generalTotalPlays) * 100 : 0 ;
		DecimalFormat df = new DecimalFormat("###.##");
		Label generalTotalWinsLabel = new Label("Wins: " + generalTotalWins + " | Win Percent: " + df.format(generalWinPercent) + "%");
		Label generalTotalPlaysLabel = new Label("Total Plays: " + generalTotalPlays);
		VBox generalVBox = new VBox();
		generalVBox.getChildren().addAll(userName, generalTotalPlaysLabel, generalTotalWinsLabel);
		VBoxes[0] = generalVBox;
		
		for (int i = 0; i < PuzzleType.values().length; i++) {
			int totalPlays = user.getTotalPlays(PuzzleType.values()[i]);
			int totalWins = user.getTotalWins(PuzzleType.values()[i]);
			float winPercent = totalPlays != 0 ? ((float)totalWins / (float)totalPlays) * 100 : 0;
			String gameName = "ERROR";
			switch (PuzzleType.values()[i]) {
				case SUDOKU: 
					gameName = "Sudoku";
					break;
				case HANGMAN: 
					gameName = "Hangman";
					break;
				case MASTERMIND: 
					gameName = "Mastermind";
					break;
				case TWO048: 
					gameName = "2048";
					break;
				case MINESWEEPER: 
					gameName = "MineSweeper";
					break;
				default:
					System.out.println("This should not appear.");
			}
			Label nameLabel = new Label(gameName);
			Label totalPlaysLabel = new Label("Total Plays: " + totalPlays + " | High Score: " + user.getHighScore(PuzzleType.values()[i]));
			Label winsLabel = new Label("Wins: " + totalWins + " | Win Percent: " + df.format(winPercent) + "%");
			VBox temp = new VBox();
			temp.getChildren().addAll(nameLabel, totalPlaysLabel, winsLabel);
			VBoxes[i+1] = temp;
		}
		
		for (int i = 0; i < VBoxes.length; i++) {
			Label label1 = (Label) VBoxes[i].getChildren().get(0);
			label1.getStyleClass().add("highScoreLabelTop");
			label1.setAlignment(Pos.CENTER);
			Label label2 = (Label) VBoxes[i].getChildren().get(1);
			label2.getStyleClass().add("highScoreLabelMid");
			label2.setAlignment(Pos.CENTER);
			Label label3 = (Label) VBoxes[i].getChildren().get(2);
			label3.getStyleClass().add("highScoreLabelBottom");
			label3.setAlignment(Pos.CENTER);
			
			VBoxes[i].setAlignment(Pos.BASELINE_CENTER);
			VBoxes[i].setMaxWidth(250);
			VBoxes[i].setMinWidth(250);
		}
		
		fullView.add(VBoxes[0], 1, 0);
		fullView.add(VBoxes[1], 2, 1);
		fullView.add(VBoxes[2], 2, 0);
		fullView.add(VBoxes[3], 1, 1);
		fullView.add(VBoxes[4], 0, 1);
		fullView.add(VBoxes[5], 0, 0);
		
		fullView.setMaxWidth(750);
		fullView.setMinWidth(750);
		fullView.setMaxHeight(300);
		fullView.setMinHeight(300);
		fullView.setHgap(5f);
		fullView.setVgap(5f);
		display.getMainView().setAlignment(Pos.CENTER);
		
		Label gun = new Label("Game Stats for " + user.getName());
		gun.getStyleClass().add("nameLabel");
		gun.setAlignment(Pos.CENTER);
		gun.setStyle("-fx-min-width: 50px; -fx-padding: 2px 5px 2px 5px; -fx-min-height: 10px; -fx-font-size: 32px;");
		display.getMainView().getChildren().addAll(gun, fullView);
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
