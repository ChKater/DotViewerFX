package de.chkater.dotviewerfx.application;
	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import de.chkater.dotviewerfx.view.DotViewerFX;


public class Main extends Application {
	
	private static String graph = 	"digraph G {"
												+ "low -> high [label=hold];"
												+ "high -> off [label=press];"
												+ "low -> off [label=press];"
												+ "off -> low [label=press];"
											+ "}";
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 300, 300);
			DotViewerFX dv = new DotViewerFX();
			dv.setImage(graph);
			root.getChildren().add(dv);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
