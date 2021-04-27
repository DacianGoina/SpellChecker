package ui;
	
import javafx.application.Application;
import javafx.stage.Stage;



/**
 * 
 * @author Dacian
 * <p> Main
 * Clasa principala a interfetei grafice. Este generata automat la crearea proiectului, practic este root-ul interfetei grafice
 * Foloseste un obiect OpenApp pentru a configura proprietatile ecranului care va afisa interfata grafica efectiva
 */

public class Main extends Application {
	
	protected final Command cmd = new Command();
	
	@Override
	public void start(Stage primaryStage) {
		
		OpenApp A = new OpenApp(cmd, 800, 600);
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
