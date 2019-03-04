package application;
	
import controllers.PuzzleHub;
import interfaces.SubscribesToExitable;
import interfaces.SubscribesToSceneChange;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application{
	
	private PuzzleHub hub = new PuzzleHub();
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setScene(hub.getScene());
			primaryStage.show();
			new SubscribesToExitable() {
				@Override
				public void menuExited() {
					primaryStage.close();
				}
				public void subscribe() {
					hub.exitSubcribe(this);
				}
			}.subscribe();
			new SubscribesToSceneChange() {
				@Override
				public void sceneChanged(Scene scene) {
					primaryStage.setScene(scene);
				}
				public void subscribe() {
					hub.subscribeToSceneChange(this);
				}
			}.subscribe();
		} catch(Exception e) {
			e.printStackTrace();
		}
		primaryStage.setMinWidth(primaryStage.getWidth());
		primaryStage.setMinHeight(primaryStage.getHeight());
		primaryStage.setMaxWidth(primaryStage.getWidth());
		primaryStage.setMaxHeight(primaryStage.getHeight());
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
