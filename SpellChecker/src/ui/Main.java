package ui;
	
import javafx.application.Application;
import javafx.stage.Stage;



/**
 * 
 * @author Dacian
 *
 */

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		OpenApp A = new OpenApp(800,600);
		A.openWindow(primaryStage);
		
		
		/*
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}*/
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
