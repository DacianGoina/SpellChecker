package application;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 * 
 * @author Dacian
 *
 */

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainPage {
	// GUI objects
	private Button leftButton = new Button("Left Btn");
	private Button rightButton = new Button("Right Btn");
	private Label bottomText = new Label("Bottom text");

	
	private TextArea inputZone = new TextArea();
	
	
	public void auxiliaryObjectsProperties(Button leftBtn, Button rightBtn,Label bottomText) {
		leftBtn.setVisible(false);
		rightBtn.setVisible(false);
		bottomText.setVisible(false);
		
		
	}
	
	
	public Scene showMainPage(Stage primaryStage, double windowWidth, double windowHeight) {
			
		
		//StackPane root = new StackPane();
		BorderPane root = new BorderPane();
		Scene a = new Scene(root, windowWidth, windowHeight);
		root.setCenter(inputZone);
		root.setRight(rightButton);
		root.setLeft(leftButton);
		root.setBottom(bottomText);
		root.setTop(MenuBarFactory.getMenuBar());
		
		this.auxiliaryObjectsProperties(rightButton, leftButton, bottomText);
		
			
		inputZone.setContextMenu(RightClickMenu.getRightClickMenu());
		
		
		
		
		
			
		
		
		inputZone.setId("inputZone");
		inputZone.setFocusTraversable(false);
		
		 
		
		
		
		
	
		inputZone.setOnMouseClicked(e->{
			System.out.println("Coordonate mouse: " + e.getSceneX() + " " + e.getSceneY());
		});
		
		
		
		
		

		a.getStylesheets().add(getClass().getResource("style_MainPage.css").toExternalForm());
		//root.getChildren().add(inputZone);
		//root.getChildren().add(menuBar);
		return a;
		}
}
