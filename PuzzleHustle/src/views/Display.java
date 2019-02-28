package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Display {
	
	private VBox root = new VBox();
	
	private VBox name = new VBox();
	
	private HBox navigation = new HBox();
	
	private VBox leftSidebar = new VBox();
	
	private VBox rightSidebar = new VBox();
	
	private VBox mainView = new VBox();
	
	private HBox mainCollection = new HBox();
	
	private int height = 810;
	
	private int width = 1440;
	
	private Scene scene = new Scene(root,width,height);

	
	public Display() {
		name.setAlignment(Pos.CENTER);
		
		root.setPadding(new Insets(20, 0, 20, 0));
		root.setMinSize(width, height/6);
		
		name.setPadding(new Insets(50, 0, 50, 0));
		name.setMinSize(width, height/6);
		
		navigation.setPadding(new Insets(20, 20, 20, 20));
		navigation.setMinSize(width, height/12 +2);
		navigation.setAlignment(Pos.CENTER);
		
		leftSidebar.setPadding(new Insets(30, 30, 30, 30));
		leftSidebar.setMinSize(width/6, height * 4/6 + 20);
		
		rightSidebar.setPadding(new Insets(30, 30, 30, 30));
		rightSidebar.setMinSize(width/6, height * 4/6 + 20);
		
		mainView.setMinSize(width * 4/6, height * 4/6 + 20);
		mainView.setMaxSize(width * 4/6, height * 4/6 + 20);
		mainCollection.getChildren().addAll(leftSidebar,mainView,rightSidebar);
		root.getChildren().addAll(name,navigation,mainCollection);
	}

	public VBox getRoot() {
		return root;
	}

	public void setRoot(VBox root) {
		this.root = root;
	}

	public VBox getName() {
		return name;
	}

	public void setName(VBox name) {
		this.name = name;
	}

	public HBox getNavigation() {
		return navigation;
	}

	public void setNavigation(HBox navigation) {
		this.navigation = navigation;
	}

	public VBox getLeftSidebar() {
		return leftSidebar;
	}

	public void setLeftSidebar(VBox leftSidebar) {
		this.leftSidebar = leftSidebar;
	}

	public VBox getRightSidebar() {
		return rightSidebar;
	}

	public void setRightSidebar(VBox rightSidebar) {
		this.rightSidebar = rightSidebar;
	}

	public VBox getMainView() {
		return mainView;
	}

	public void setMainView(VBox mainView) {
		this.mainView = mainView;
	}

	public HBox getMainCollection() {
		return mainCollection;
	}

	public void setMainCollection(HBox mainCollection) {
		this.mainCollection = mainCollection;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}
	

}
