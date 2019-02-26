package controllers;

import java.util.HashMap;

import enums.PuzzleType;
import models.Puzzle;
import models.User;

public class PuzzleHub {
	private HashMap<String, User> users = new HashMap<>();
	private User activeUser;
	private Puzzle activePuzzle;
	
	public User getUser(String userName) {
		return users.get(userName);
	}
	public void addUser(User user) {
		users.put(user.getName(), user);
	}
	public User getActiveUser() {
		return activeUser;
	}
	public void setActiveUser(User activeUser) {
		this.activeUser = activeUser;
	}
	public Puzzle getActivePuzzle() {
		return activePuzzle;
	}
	public void setActivePuzzle(Puzzle activePuzzle) {
		this.activePuzzle = activePuzzle;
	}
	public PuzzleHub() {
		
	}
	public void selectUserMenu() {
		
	}
	public void mainMenu() {
		
	}
	public void createNewUser() {
		
	}
	public void login() {
		
	}
	public void setupGame(PuzzleType puzzleType) {
		
	}
	public void loadPuzzle(PuzzleType puzzleType) {
		
	}
	public void createNewPuzzle(PuzzleType puzzleType) {
		
	}
	public void runGame() {
		
	}
	public void exit() {
		
	}

}
