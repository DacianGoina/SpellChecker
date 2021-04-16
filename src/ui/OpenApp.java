package ui;

import javafx.stage.Stage;
/**
 * @author Dacian
 * <p> OpenApp
 * Clasa folosita pentru configura proprietatile ecranului care va fi afisat. 
 * Nu configureaza efectiv interfata grafica (butoane, text,etc) ci proprietatile ferestrei - titlu, dimensiune, etc
 * La final lanseaza interfata grafica efectiva
 */

public class OpenApp {
	private double windowHeight;
	private double windowWidth;

	public OpenApp(double windowWidth, double windowHeight) {
		super();
		this.windowHeight = windowHeight;
		this.windowWidth = windowWidth;
	}

	public double getWindowHeight() {
		return windowHeight;
	}
	public void setWindowHeight(double windowHeight) {
		this.windowHeight = windowHeight;
	}

	public double getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(double windowWidth) {
		this.windowWidth = windowWidth;
	}
	
	
	public void openWindow(Stage primaryStage) {
			try {
				//primaryStage.setResizable(false);
				primaryStage.setTitle("SpellChecker");
				primaryStage.setWidth(windowWidth);
				primaryStage.setHeight(windowHeight);
				primaryStage.setScene((new MainPage()).showMainPage(primaryStage,windowWidth, windowHeight));
				primaryStage.setMinWidth(650);
				primaryStage.setMinHeight(500);
				primaryStage.show();
				System.out.println(primaryStage.getWidth() + " " + primaryStage.getHeight());
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
	}
}
