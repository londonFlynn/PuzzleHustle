package models;

import java.io.Serializable;
import java.util.HashMap;

import enums.PuzzleType;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private HashMap<PuzzleType, Float> highScore = new HashMap<>();
	private HashMap<PuzzleType, Integer> totalWins = new HashMap<>();
	private HashMap<PuzzleType, Integer> totalPlays = new HashMap<>();
	private String filePath = "user"+name+".txt";
	
	public User(String name) {
		setName(name);
		setFilePath("user"+getName()+".txt");
	}
	public void save() {
		lib.FileIO.save(this, filePath);
	}
	public void delete() {
		lib.FileIO.delete(filePath);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Float getHighScore(PuzzleType puzzleType) {
		return highScore.get(puzzleType);
	}
	public void setHighScore(PuzzleType puzzleType, Float score) {
		if (score > getHighScore(puzzleType)) {
		this.highScore.put(puzzleType, score);
		}
	}
	public Integer getTotalWins(PuzzleType puzzleType) {
		if (this.totalWins.get(puzzleType) == null) {
			setTotalWins(puzzleType, 0);
		}
		return this.totalWins.get(puzzleType);
	}
	public void setTotalWins(PuzzleType puzzleType, Integer wins) {
		totalWins.put(puzzleType, wins);
	}
	public Integer getTotalPlays(PuzzleType puzzleType) {
		if (this.totalPlays.get(puzzleType) == null) {
			setTotalPlays(puzzleType, 0);
		}
		return this.totalPlays.get(puzzleType);
	}
	public void setTotalPlays(PuzzleType puzzleType, Integer plays) {
		totalPlays.put(puzzleType, plays);
	}
	public void setFilePath(String path) {
		this.filePath = path;
	}
	public String getFilePath() {
		return filePath;
	}

}
