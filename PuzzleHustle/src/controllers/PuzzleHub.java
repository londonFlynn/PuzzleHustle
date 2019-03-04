package controllers;

import java.io.File;
import java.util.HashMap;

import enums.PuzzleType;
import games.Hangman;
import games.MasterMind;
import games.Sudoku;
import games.TwentyFourtyEight;
import games.MineSweeper;
import interfaces.IExitable;
import interfaces.NewPuzzleSubscriber;
import interfaces.SceneChanges;
import interfaces.SubscribesToExitable;
import interfaces.SubscribesToSceneChange;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import models.Puzzle;
import models.User;
import views.Display;
import views.UserView;

public class PuzzleHub implements NewPuzzleSubscriber, SubscribesToExitable, IExitable, SceneChanges {
	private HashMap<String, User> users = new HashMap<>();
	private User activeUser;
	private Puzzle activePuzzle;
	private Display display = new Display();
	private VBox mainMenu;
	private VBox selectUserMenu;
//	private VBox selectPuzzleMenu;
	private VBox selectPuzzleType;
	private PuzzleType puzzleChoice;
	private SubscribesToExitable exitSubscriber;
	private SubscribesToSceneChange sceneSubscriber;
	private Scene scene = display.getScene();
	private VBox loginMenu;
	private VBox newUserMenu;
	private Label activeUserLabel;
	private Label validNameLabel = new Label();
	private TextField nameField = new TextField();

	public PuzzleHub() {
		Label hubName = new Label("Puzzle Hustle");
		hubName.getStyleClass().add("nameLabel");
		hubName.setAlignment(Pos.CENTER);
		display.getName().getChildren().add(hubName);
		display.getNavigation().getChildren().addAll(mainMenuButton(), exitButton());
		showMainMenu();
		display.getMainView().setAlignment(Pos.TOP_CENTER);
		setupUserFolder();
		setupUsers();
	}

	private void setupUsers() {
		File folder = new File("users");
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				try {
					User user = (User) lib.FileIO.load(listOfFiles[i].getPath());
					addUser(user);
				} catch (ClassNotFoundException e) {
					System.err.println("Failed to load user");
				}
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
			}
		}
	}

	private void setupUserFolder() {
		new File("users").mkdirs();
	}

	private void addUser(User user) {
		users.put(user.getName(), user);
	}

	private void setActivePuzzle(Puzzle activePuzzle) {
		this.activePuzzle = activePuzzle;
		this.activePuzzle.subcribe(this);
		this.activePuzzle.exitSubcribe(this);
	}

	public Scene getScene() {
		return scene;
	}

	private void showSelectUserMenu() {
		if (selectUserMenu == null) {
			selectUserMenu = new VBox();
			selectUserMenu.setAlignment(Pos.CENTER);
			selectUserMenu.getChildren().addAll(this.newUserButton(), this.loginButton());
		}
		display.getMainView().getChildren().clear();
		display.getMainView().getChildren().add(selectUserMenu);
	}

	private void showMainMenu() {
		mainMenu = new VBox();
		mainMenu.setAlignment(Pos.CENTER);
		if (activeUser != null) {
			mainMenu.getChildren().addAll(playButton(), userStatsButton());
			this.playButton();
		}
		mainMenu.getChildren().addAll(this.selectUserMenuButton());

		display.getMainView().getChildren().clear();
		display.getMainView().getChildren().add(mainMenu);
	}

	private void showSelectPuzzleMenu() {
		if (selectPuzzleType == null) {
			selectPuzzleType = new VBox();
			selectPuzzleType.setAlignment(Pos.CENTER);
			selectPuzzleType.getChildren().addAll(sudokuButton(), mastermindButton(), minesweeperButton(),
					Twenty48Button(), hangmanButton());
		}
		display.getMainView().getChildren().clear();
		display.getMainView().getChildren().add(selectPuzzleType);
	}

	private void createNewUser() {
		newUserMenu = new VBox();
		newUserMenu.setAlignment(Pos.CENTER);
		Label enterNamePrompt = new Label("Please enter your user name");
		enterNamePrompt.getStyleClass().add("gameStats");
		nameField.textProperty().set("");
		validNameLabel.textProperty().set("");
		newUserMenu.getChildren().addAll(enterNamePrompt,nameField, submitNameButton(), validNameLabel);
		display.getMainView().getChildren().clear();
		display.getMainView().getChildren().add(newUserMenu);
	}

	private void login() {
		loginMenu = new VBox();
		loginMenu.setAlignment(Pos.CENTER);
		loginMenu.getChildren().addAll(getUserButtons());
		display.getMainView().getChildren().clear();
		display.getMainView().getChildren().add(loginMenu);
	}

	private ListView<Button> getUserButtons() {
		ListView<Button> list = new ListView<Button>();
		if (users.size() != 0) {
			User[] allUsers = new User[users.size()];
			ObservableList<Button> items = FXCollections
					.observableArrayList(selectUserButton(users.values().toArray(allUsers)[0]));
			for (int i = 1; i < allUsers.length; i++) {
				items.add((Button) selectUserButton(allUsers[i]));
			}
			list.setItems(items);
			list.setPrefWidth(100);
			list.setMinHeight(display.getMainCollection().getHeight()-30);
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
		return list;
	}

	private void setActiveUser(User user) {
		activeUser = user;
		display.getRightSidebar().getChildren().clear();
		if (activeUser != null) {
			display.getRightSidebar().setAlignment(Pos.TOP_CENTER);
			activeUserLabel = new Label("Logged in as: " + user.getName());
			activeUserLabel.getStyleClass().add("gameStats");
			display.getRightSidebar().getChildren().add(activeUserLabel);
		}
	}

	private Button selectUserButton(User user) {
		Button button = new Button(user.getName());
		button.setMinSize(100, 40);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				setActiveUser(user);
				showMainMenu();
			}
		});
		return button;
	}

//	private void showPuzzleCreationMenu() {
//		if (selectPuzzleMenu == null) {
//			selectPuzzleMenu = new VBox();
//			selectPuzzleMenu.setAlignment(Pos.CENTER);
//			selectPuzzleMenu.getChildren().addAll(/*this.loadPuzzleButton(),*/this.newPuzzleButton());
//		}
//		display.getMainView().getChildren().clear();
//		display.getMainView().getChildren().add(selectPuzzleMenu);
//	}
//	public void loadPuzzle(PuzzleType puzzleType) {
//			try {
//				activePuzzle = (Puzzle) FileIO.load("SUDOKUnull.txt");
//				runGame();
//			} catch (ClassNotFoundException | NullPointerException e) {
//				display.getMainView().getChildren().clear();
//				Label couldNotLoad = new Label("You have never played that game before. We cannot load the game.");
//				couldNotLoad.getStyleClass().add("gameStats");
//				display.getMainView().getChildren().addAll(couldNotLoad, mainMenuButton());
//			}
//	}

	private String getFilePath() {
		return puzzleChoice.toString() + activeUser.getName() + ".txt";
	}

	@Override
	public void createNewPuzzle(PuzzleType puzzleChoice) {
		String filePath = getFilePath();
		switch (puzzleChoice) {
		case SUDOKU:
			setActivePuzzle(new Sudoku(filePath, activeUser));
			break;
		case TWO048:
			setActivePuzzle(new TwentyFourtyEight(filePath, PuzzleType.TWO048, activeUser));
			break;
		case HANGMAN:
			setActivePuzzle(new Hangman(filePath, activeUser));
			break;
		case MASTERMIND:
			setActivePuzzle(new MasterMind(filePath, activeUser));
			break;
		case MINESWEEPER:
			setActivePuzzle(new MineSweeper(filePath, activeUser));
			break;
		}
		runGame();
	}

	private void setScene(Scene scene) {
		this.scene = scene;
		sceneChange();
	}

	private void runGame() {
		activeUser.setTotalPlays(puzzleChoice, activeUser.getTotalPlays(puzzleChoice) + 1);
		setScene(activePuzzle.getScene());
	}

	public void exit() {
		exitSubscriber.menuExited();
	}

	private void showUserStats() {
		new SubscribesToExitable() {
			UserView userView = new UserView(activeUser, this);

			@Override
			public void menuExited() {
				showMainMenu();
				PuzzleHub.this.setScene(display.getScene());
			}

			public void setScene() {
				PuzzleHub.this.setScene(userView.getScene());
			}
		}.setScene();
	}

	private Button exitButton() {
		Button menu = new Button("Quit PuzzleHustle");
		menu.setMinSize(100, 40);
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				exit();
			}
		});
		return menu;
	}

	private Button mainMenuButton() {
		Button menu = new Button("Main Menu");
		menu.setMinSize(100, 40);
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showMainMenu();
			}
		});
		return menu;
	}

	private Button selectUserMenuButton() {
		Button menu = new Button("Login");
		menu.setMinSize(100, 40);
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showSelectUserMenu();
			}
		});
		return menu;
	}

	private Button loginButton() {
		Button menu = new Button("Login as existing user");
		menu.setMinSize(100, 40);
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				login();
			}
		});
		return menu;
	}

	private Button newUserButton() {
		Button menu = new Button("New User");
		menu.setMinSize(100, 40);
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				createNewUser();
			}
		});
		return menu;
	}

	private Button userStatsButton() {
		Button menu = new Button("Show my game stats");
		menu.setMinSize(100, 40);
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showUserStats();
			}
		});
		return menu;
	}

	private void setValidNameLabel(String s) {
		validNameLabel.getStyleClass().add("instructions");
		validNameLabel.textProperty().set(s);
	}

	private boolean validateNameInput(String name) {
		boolean valid = (name.trim().equals("")) ? false : true;
		boolean showCharInvalid = false;
		setValidNameLabel("Name cannot be empty");
		for (User user : users.values()) {
			if (name.equals(user.getName())) {
				valid = false;
				setValidNameLabel("There is already a user with that name.");
			}
		}
		for (char c : name.toCharArray()) {
			if (c < '0') {
				showCharInvalid = true;
				valid = false;
			} else if (c > '9' && c < 'A') {
				showCharInvalid = true;
				valid = false;
			} else if (c > 'Z' && c < 'a') {
				showCharInvalid = true;
				valid = false;
			} else if (c > 'z') {
				showCharInvalid = true;
				valid = false;
			}
			if (showCharInvalid) {
				setValidNameLabel("Your user name can only include numbers \nand letters.");
			}
		}
		return valid;
	}

	private void submitName() {
		if (validateNameInput(nameField.getText())) {
			User user = new User(nameField.getText());
			user.save();
			addUser(user);
			setActiveUser(user);
			showMainMenu();
		} else {
			nameField.setText("");
		}
	}

	private Button submitNameButton() {
		Button menu = new Button("Submit");
		menu.setMinSize(100, 40);
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				submitName();
			}
		});
		return menu;
	}

	private Button playButton() {
		Button menu = new Button("Play Puzzle");
		menu.setMinSize(100, 40);
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showSelectPuzzleMenu();
			}
		});
		return menu;
	}

//	private Button newPuzzleButton() {
//		Button menu = new Button("New Puzzle");
//		menu.setMinSize(100, 40);
//		menu.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				createNewPuzzle(puzzleChoice);
//			}
//		});
//		return menu;
//	}
//	private Button loadPuzzleButton() {
//		Button menu = new Button("Load Puzzle");
//		menu.setMinSize(100, 40);
//		menu.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				loadPuzzle(puzzleChoice);
//			}
//		});
//		return menu;
//	}
	private Button sudokuButton() {
		Button menu = new Button("Sudoku");
		menu.setMinSize(100, 40);
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				setPuzzleChoice(PuzzleType.SUDOKU);
				createNewPuzzle(puzzleChoice);
			}
		});
		return menu;
	}

	private Button mastermindButton() {
		Button menu = new Button("Mastermind");
		menu.setMinSize(100, 40);
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				setPuzzleChoice(PuzzleType.MASTERMIND);
				createNewPuzzle(puzzleChoice);
			}
		});
		return menu;
	}

	private Button minesweeperButton() {
		Button menu = new Button("Minesweeper");
		menu.setMinSize(100, 40);
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				setPuzzleChoice(PuzzleType.MINESWEEPER);
				createNewPuzzle(puzzleChoice);
			}
		});
		return menu;
	}

	private Button hangmanButton() {
		Button menu = new Button("Hangman");
		menu.setMinSize(100, 40);
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				setPuzzleChoice(PuzzleType.HANGMAN);
				createNewPuzzle(puzzleChoice);
			}
		});
		return menu;
	}

	private Button Twenty48Button() {
		Button menu = new Button("2048");
		menu.setMinSize(100, 40);
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				setPuzzleChoice(PuzzleType.TWO048);
				createNewPuzzle(puzzleChoice);
			}
		});
		return menu;
	}

	private void setPuzzleChoice(PuzzleType puzzleType) {
		puzzleChoice = puzzleType;
	}

	@Override
	public void menuExited() {
		activeUser.save();
		setScene(display.getScene());
		showMainMenu();
	}

	@Override
	public void exitSubcribe(SubscribesToExitable subscriber) {
		exitSubscriber = subscriber;

	}

	@Override
	public void exitUnSubcribe(SubscribesToExitable subscriber) {
	}

	@Override
	public void sceneChange() {
		sceneSubscriber.sceneChanged(getScene());
	}

	@Override
	public void subscribeToSceneChange(SubscribesToSceneChange subscriber) {
		this.sceneSubscriber = subscriber;

	}

}
