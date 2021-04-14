package ui;


/**
 * 
 * @author Dacian
 *
 */


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainPage {
	// GUI objects
	private Button leftButton = new Button("Left Btn");
	private Button rightButton = new Button("Right Btn");

	
	HBox bottomGroup = new HBox();
	private Button firstParaBtn = new Button("<<");
	private Button prevParaBtn = new Button("<");
	private Button nextParaBtn = new Button(">");
	private Button lastParaBtn = new Button(">>");
	private Text paraInfo = new Text("Paragraful 0 / 0");
	
	private int paraIndex;
	
	
	private static TextArea inputZone = new TextArea();
	
	public static Stage mainStage;
	
	public static String getInputZoneText() {
		return inputZone.getText();
	}
	
	public static void setInputZoneText(String text) {
		inputZone.setText(text);
	}
	
	public void auxiliaryObjectsProperties() {
		
		inputZone.setWrapText(true); // pentru a face afisare textului pe mai multe linii in care este linie continua si nu incape toate
		// ajuta mult mai ales la fisiere docx unde un paragraf este pus pe linie continua (fara newline in el)
		
		
		this.leftButton.setVisible(false);
		this.rightButton.setVisible(false);
		
		
		// Id-uri pt butoanele ( + text) de jos (pt a le customiza din css)
		this.firstParaBtn.setId("bb");
		this.lastParaBtn.setId("bb");
		this.prevParaBtn.setId("bb");
		this.nextParaBtn.setId("bb");
		this.paraInfo.setId("paraInfo");
		
		// Focus pentru butoane
		this.firstParaBtn.setFocusTraversable(false);
		this.lastParaBtn.setFocusTraversable(false);
		this.prevParaBtn.setFocusTraversable(false);
		this.nextParaBtn.setFocusTraversable(false);
		
		
		
		bottomGroup.setAlignment(Pos.CENTER);
		bottomGroup.setId("bg");
		bottomGroup.setSpacing(15);
		
		// Margini pentru butoanele de jos
		HBox.setMargin(firstParaBtn, new Insets(10, 0, 10, 0));
		HBox.setMargin(prevParaBtn, new Insets(10,0,10,0));
		HBox.setMargin(nextParaBtn, new Insets(10,0,10,0));
		HBox.setMargin(lastParaBtn, new Insets(10,0,10,0));
		HBox.setMargin(paraInfo, new Insets(10,0,10,0));
		
	}
	
	// cand vom avea doar un paragraf butoanele nu vor fi active
	public void disableBottomButtons() {
		this.nextParaBtn.setDisable(true);
		this.prevParaBtn.setDisable(true);
		this.lastParaBtn.setDisable(true);
		this.firstParaBtn.setDisable(true);
	}
	
	public void enableBottomButtons() {
		this.nextParaBtn.setDisable(false);
		this.prevParaBtn.setDisable(false);
		this.lastParaBtn.setDisable(false);
		this.firstParaBtn.setDisable(false);
	}
	
	
	public Scene showMainPage(Stage primaryStage, double windowWidth, double windowHeight) {
			
		
		mainStage = primaryStage; // am nevoie sa il pasez ca argument pentru FileChooser
		
		//StackPane root = new StackPane();
		BorderPane root = new BorderPane();
		Scene a = new Scene(root, windowWidth, windowHeight);
		root.setCenter(inputZone);
		root.setRight(rightButton);
		root.setLeft(leftButton);
		root.setTop(MenuBarFactory.getMenuBar());
		
		
		bottomGroup.getChildren().addAll(firstParaBtn, prevParaBtn, nextParaBtn, lastParaBtn, paraInfo);
		root.setBottom(bottomGroup);
		this.auxiliaryObjectsProperties();
		this.disableBottomButtons();
			
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
	
	
		/*
		// initializare text : "Paragraful 1 / noOfParagraphs"
		// de asemenea paraIndex se initializeaza cu 1 - ulterior se va modifica la navigarea prin paragrafe
		public void paraInfoInitialize(int noOfParagraphs) {
			this.paraIndex = 1;
			this.paraInfo.setText("Paragraful 1 / " + noOfParagraphs);
		}
		
		public void paraInfoUpdate(int noOfParagraphs) {
			this.paraInfo.setText("Paragraful " + this.paraIndex + " / " + noOfParagraphs);
		}
		
		
		// cand navigam prin paragrafe sa modificam si indexul
		public void incrementParaIndex() {
			this.paraIndex++;
			
		}
		
		public void decrementParaIndex() {
			this.paraIndex--;
		}
		
		public void resetParaIndex() {
			this.paraIndex = 1;
			this.firstParaBtn.setDisable(true); // daca sunt la primul paragraf nu are rost sa merg la primul
			this.prevParaBtn.setDisable(true); // nu pot merge inapoi
		}
		
		public void lastParaIndex(int index) {
			this.paraIndex = index;
			this.nextParaBtn.setDisable(true);
			this.lastParaBtn.setDisable(true);
		}
		
		
		public void paraInfoTXTFile() {
			this.paraInfo.setText("Paragraful 1 / 1");
		}
		
		*/
}
