package models;

import java.io.Serializable;

import enums.PuzzleType;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public abstract class Puzzle implements Serializable {

	protected BorderPane puzzlePane = new BorderPane();
	protected Scene scene;
	private static final long serialVersionUID = 1L;
	private double timeSpent = 0;
	private float score = 0;
	private boolean solved = false;
	private String filePath;
	public final PuzzleType PUZZLE_TYPE;
	private User user;
	public Puzzle(String filePath, PuzzleType PUZZLE_TYPE, User user) {
		setFilePath(filePath);
		this.PUZZLE_TYPE = PUZZLE_TYPE;
		setUser(user);
	}
	public void Save() {
		
	}
	public void delete() {
		
	}
	public Scene getScene() {
		
		return null;
	}
	
	public void exit() {
		
	}
	public abstract void showInstructions();
	
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
	
	

}
