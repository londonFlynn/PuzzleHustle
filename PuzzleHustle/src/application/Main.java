package application;

import controllers.MusicManager;
import controllers.PuzzleHub;
import interfaces.SubscribesToExitable;
import interfaces.SubscribesToSceneChange;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Main extends Application {

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

		} catch (Exception e) {
			e.printStackTrace();
		}

		primaryStage.setMinWidth(primaryStage.getWidth());
		primaryStage.setMinHeight(primaryStage.getHeight());
		primaryStage.setMaxWidth(primaryStage.getWidth());
		primaryStage.setMaxHeight(primaryStage.getHeight());
		primaryStage.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				EventTarget target = mouseEvent.getTarget();
				System.out.println(target);
				if (target instanceof Node) {
					Node targetNode = (Node) target;

					while (targetNode != null && !(targetNode instanceof Button)) {
						targetNode = targetNode.getParent();
					}
					if (target instanceof Button || targetNode instanceof Button) {
						MusicManager.playSquish();
					}
				}
			}
		});
	}

	public static void main(String[] args) {
		MusicManager.startMusic();
		launch(args);
	}
}
