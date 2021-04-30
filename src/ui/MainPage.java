package ui;


/**
 * 
 * @author Dacian
 *<p> MainPage
 * Clasă folosită pentru interfața grafică - conține (construiește) elementele grafice (meniu, butoane, zonă text) prin care utilizatorul va putea interacționa cu aplicația.
 */

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainPage {
	
	protected final Command cmd ;
	public MainPage(final Command cmd) {
		this.cmd = cmd;
	}
	
	
	// GUI objects
	private Button leftButton = new Button("Left Btn");
	private Button rightButton = new Button("Right Btn");

	private VBox leftGroup = new VBox();
	private Button pasteBtn = new Button();
	private Button copyBtn = new Button();
	
	HBox bottomGroup = new HBox();
	private TextField goToParaField = new TextField();
	private Button goToParaBtn = new Button("Go");
	private Button firstParaBtn = new Button("<<");
	private Button prevParaBtn = new Button("<");
	private Button nextParaBtn = new Button(">");
	private Button lastParaBtn = new Button(">>");
	private Text paraInfo = new Text();
	
	private TextArea inputZone = new TextArea();
	
	/**
	 * <p>Indexul se va folosi pentru a naviga prin paragrafe, va indica paragraful curent - la care ne aflam
	 * <p>Este important pentru a comunica cu lista de paragrafe
	 * <p>OBS: lungimea liste cu textul pentru paragrafe incepe de la 1, indexul folosit pentru a naviga prin lista incepe de la 0
	 */
	private int paraIndex; 
	
	
	/**
	 * <p>isDocxOpen - retine daca anterior (cel mai recent) am deschis un fisier DOCX
	 * pentru a putea exporta DOCX ce acceasi structura (imagini, tabele etc), nu doar paragrafe
	 * (altfel ar fi cam dezavantajos sa folosesti aplicatia asta pentru a corecta de ex o lucrare de licenta in care ai tabele si imagini,
	 * deoarece cand exporti DOCX iti exporta doar paragrafele, DAR folosind abordarea asta tinem minte fisierul si recream unul asemanator
	 * unde schimbam doar textul din paragrafe, nu ne atingem de tabele, imagini, ecuatii etc)
	 * 
	 * <p>auxDocxFilePath - retine calea (path) spre fisierul docx deschis recent
	 */
	private String auxDocxFilePath = new String();
	private boolean isDocxOpen; 

	/**
	 * <p>Lista cu paragrafe din aplicatie
	 * <p>Aceasta lista gestioneaza efectiv textul din aplicatie
	 * <p>Prin inputZone utlizatorul comunica secvential cu elementele (textele) din paraList
	 * <p>Cand se face trecerea la un alt paragraf se salveaza modificarile facute in paragraful curent
	 * <p>Pentru a determina unde trebuie salvate modificarile (la care pozitie in lista) se foloseste paraIndex
	 */
	private ArrayList<String> paraList = new ArrayList<String>();
	
	public Stage mainStage;
	
	/**
	 * <p>Restarteaza paragrafele din aplicatie - continutul textului:
	 * <p>Dezactiveaza butonele
	 * <p>Goleste lista de paragrafe si creaza doar un paragraf gol
	 * <p>paraIndex este resetat la 0
	 * <p>In inputZone este setat textul de la paragraful gol creat anterior
	 * <p>Este afisat mesaj corespunzator care indica numarul paragrafului la care ne aflam
	 * <p>OBS: numerotarea parafelor in afisare incepe de la 1 (paraIndex + 1)
	 */
	public void initializeParaList() {
		disableBottomButtons(); // daca avem doar un paragraf atunci nu este nevoie de butoane deoarece nu avem unde naviga
		this.paraList.clear();
		String p1 = "";
		this.paraList.add(p1);
		this.paraIndex = 0;
		setInputZoneText(getParaListElem(getParaIndex())); 
		setParaInfo();
	}
	
	/**
	 * <p>Seteaza proprietati pentru unele obiecte GUI: seteaza ID-uri (poate folosim la CSS putin pentru design), dezactiveaza focus (albastru nepotrivit)
	 */
	public void auxiliaryObjectsProperties() {
		inputZone.setId("inputZone");
		inputZone.setFocusTraversable(false);
		inputZone.setWrapText(true); // pentru a face afisare textului pe mai multe linii in care este linie continua si nu incape toate
		// ajuta mult mai ales la fisiere docx unde un paragraf este pus pe linie continua (fara newline in el)
		
		
		this.initializeParaList();
		
		
		this.leftButton.setVisible(false);
		this.rightButton.setVisible(false);
		
		
		// Butoanele pentru navigarea prin paragrafe - bottomGroup
		
		// Id-uri pt butoanele ( + text) de jos (pt a le customiza din css)
		this.firstParaBtn.setId("bb");
		this.lastParaBtn.setId("bb");
		this.prevParaBtn.setId("bb");
		this.nextParaBtn.setId("bb");
		this.goToParaField.setId("bb");
		this.goToParaBtn.setId("bb");
		this.paraInfo.setId("paraInfo");
		
		// Focus pentru butoane
		this.firstParaBtn.setFocusTraversable(false);
		this.lastParaBtn.setFocusTraversable(false);
		this.prevParaBtn.setFocusTraversable(false);
		this.nextParaBtn.setFocusTraversable(false);
		this.goToParaField.setFocusTraversable(false);
		this.goToParaBtn.setFocusTraversable(false);
		
		goToParaField.setPrefWidth(80);
		
		bottomGroup.setAlignment(Pos.CENTER);
		bottomGroup.setId("bg");
		bottomGroup.setSpacing(15);
		
		// Margini pentru butoanele de jos
		HBox.setMargin(firstParaBtn, new Insets(10, 0, 10, 0));
		HBox.setMargin(prevParaBtn, new Insets(10,0,10,0));
		HBox.setMargin(nextParaBtn, new Insets(10,0,10,0));
		HBox.setMargin(lastParaBtn, new Insets(10,0,10,0));
		HBox.setMargin(paraInfo, new Insets(10,0,10,0));
		HBox.setMargin(goToParaField, new Insets(10,0,10,0));
		HBox.setMargin(goToParaBtn, new Insets(10,0,10,0));
		
		// Butoanele din partea stanga - leftGroup
		pasteBtn.setId("lb");
		copyBtn.setId("lb");
		pasteBtn.setFocusTraversable(false);
		copyBtn.setFocusTraversable(false);
		leftGroup.setAlignment(Pos.TOP_CENTER);
		leftGroup.setId("lg");
		leftGroup.setSpacing(15);
		
		VBox.setMargin(copyBtn,  new Insets(10, 10, 10, 10));
		VBox.setMargin(pasteBtn,  new Insets(5, 10, 10, 10));
		
	}
	
	/**
	 * <p>Dezactiveaza butoanele din partea de jos - sa nu fie apasabile
	 * <p>De exemplu cand avem doar un paragraf nu avem unde naviga
	 */
	public void disableBottomButtons() {
		this.nextParaBtn.setDisable(true);
		this.prevParaBtn.setDisable(true);
		this.lastParaBtn.setDisable(true);
		this.firstParaBtn.setDisable(true);
		this.goToParaField.setDisable(true);
		this.goToParaBtn.setDisable(true);
	}
	
	/**
	 * <p>Se foloseste pentru a activa butoanele
	 * <p>Se apeleaza de ex. cand se importa DOCX sau EXCEL care contin mai mult de un paragraf
	 * <p>Se porneste de la paragraful 1, prin urmare nu are rost sa activam butoanele prev si first
	 */
	public void enableBottomButtons() {
		this.nextParaBtn.setDisable(false);
		this.lastParaBtn.setDisable(false);
		this.goToParaField.setDisable(false);
		this.goToParaBtn.setDisable(false);
		//this.prevParaBtn.setDisable(false);
		//this.firstParaBtn.setDisable(false);
	}
	
	// Setari pentru butoanele din leftGroup
	public void leftGroupProperties() {
		
	}
	
	public Scene showMainPage(Stage primaryStage, double windowWidth, double windowHeight) {
		mainStage = primaryStage; // am nevoie sa il pasez ca argument pentru FileChooser
		
		//StackPane root = new StackPane();
		BorderPane root = new BorderPane();
		Scene a = new Scene(root, windowWidth, windowHeight);
		root.setCenter(inputZone);
		root.setRight(rightButton);
		//root.setLeft(leftButton);
		root.setTop(MenuBarInitializer.getMenuBar(this));
		

		leftGroup.getChildren().addAll(copyBtn,pasteBtn);
		root.setLeft(leftGroup);
		
		bottomGroup.getChildren().addAll(firstParaBtn, prevParaBtn, nextParaBtn, lastParaBtn, goToParaField, goToParaBtn, paraInfo);
		root.setBottom(bottomGroup);
		this.auxiliaryObjectsProperties();
		this.disableBottomButtons();
			
		
		
		
		
		inputZone.setContextMenu(RightClickMenu.getRightClickMenu());
		
	
		inputZone.setOnMouseClicked(e->{
			System.out.println("Coordonate mouse: " + e.getSceneX() + " " + e.getSceneY());
		});
		
		
		
		//Mouse handler event pentru firstParaBtn - du-te la primul paragraf
		firstParaBtn.setOnMouseClicked(e->{
			firstParaBtnHandler();
		});
		
		//Mouse handler event pentru nextParaBtn - du-te la urmatorul paragraf
		nextParaBtn.setOnMouseClicked(e->{
			nextParaBtnHandler();
		});
		
		
		//Mouse handler event pentru prevParaBtn - du-te la paragraful anterior
		prevParaBtn.setOnMouseClicked(e->{
			prevParaBtnHandler();
		});
		
		//Mouse handler event pentru lastParaBtn - du-te la ultimul paragraf
		lastParaBtn.setOnMouseClicked(e->{
			lastParaBtnHandler();
		});

		// Mouse handler event pentru goToParaBtn - sari la un anumit paragraf
		goToParaBtn.setOnMouseClicked(e->{
			goToParaBtnHandler();
		});
		
		a.getStylesheets().add(getClass().getResource("style_MainPage.css").toExternalForm());
		return a;
		}

	/**
	 * <p>Butonul pentru a sari de la un paragraf la altul (non secvential adica), in cazul in care avem multe paragrafe
	 * am consuma foarte mult timp daca am parcurge secvential doar ca sa ajungem la ultimul paragraf
	 * <p>Se preia valoarea lui campul goToParaField
	 * <p>Se verifica daca valoarea preluata este nenula (!= null), daca este o valoarea numerica pozitiva si daca apartine intervalului corespunzator
	 * astfel incat este index valid pentru un paragraf
	 * <p>La trecerea spre un alt paragraf se salveaza textul din paragraful curent, se afiseaza in inputZone textul din noul paragraf
	 * si se inchid / deschid butoanele corespunzatoare (ex. daca merg la ultimul paragraf atunci nu ma mai pot deplasa in fata)
	 */
	public void goToParaBtnHandler() {
		String val = goToParaField.getText();
		if(val != null) 
			if(StringUtils.isNumeric(val) == true) {
				int n = Integer.valueOf(val);
				if(n >=1 && n <=paraList.size()) {
					setParaListElem(paraIndex, getInputZoneText());
					paraIndex = n-1;
					setInputZoneText(getParaListElem(paraIndex));
					setParaInfo();
					
					prevParaBtn.setDisable(false);
					firstParaBtn.setDisable(false);
					lastParaBtn.setDisable(false);
					nextParaBtn.setDisable(false);
					
					if(paraIndex == 0) {
						prevParaBtn.setDisable(true);
						firstParaBtn.setDisable(true);
					}
					if(paraIndex == paraList.size() - 1) {
						nextParaBtn.setDisable(true);
						lastParaBtn.setDisable(true);
					}
				}
			}
				
	}

	public void firstParaBtnHandler() {
		setParaListElem(paraIndex,getInputZoneText()); // updateaza text in paragraful curent
		paraIndex = 0;
		setParaInfo();
		setInputZoneText(getParaListElem(paraIndex));
		prevParaBtn.setDisable(true);
		firstParaBtn.setDisable(true);
		lastParaBtn.setDisable(false);
		nextParaBtn.setDisable(false);
	}
	
	public void nextParaBtnHandler() {
		setParaListElem(paraIndex,getInputZoneText());
		paraIndex++;
		if(paraIndex == paraList.size() - 1) {
			nextParaBtn.setDisable(true);
			lastParaBtn.setDisable(true);
		}
		setInputZoneText(getParaListElem(paraIndex));
		setParaInfo();
		firstParaBtn.setDisable(false);
		prevParaBtn.setDisable(false);
	}
	
	public void prevParaBtnHandler() {
		setParaListElem(paraIndex,getInputZoneText());
		paraIndex--;
		if(paraIndex == 0) {
			prevParaBtn.setDisable(true);
			firstParaBtn.setDisable(true);
		}
		setParaInfo();
		setInputZoneText(getParaListElem(paraIndex));
		nextParaBtn.setDisable(false);
		lastParaBtn.setDisable(false);
	}
	
	public void lastParaBtnHandler() {
		setParaListElem(paraIndex,getInputZoneText());
		paraIndex = paraList.size() - 1;
		setParaInfo();
		setInputZoneText(getParaListElem(paraIndex));
		nextParaBtn.setDisable(true);
		lastParaBtn.setDisable(true);
		prevParaBtn.setDisable(false);
		firstParaBtn.setDisable(false);
	}
	
	
	public String getInputZoneText() {
		return inputZone.getText();
	}
	
	public void setInputZoneText(String text) {
		inputZone.setText(text);
		
	}
	
	public void setParaInfo() {
		paraInfo.setText("Paragraful " + paraIndexInfoDisplay() + " / " + paraList.size());
	}

	public ArrayList<String> getParaList() {
		return paraList;
	}

	public void setParaList(ArrayList<String> paraList) {
		this.paraList = paraList;
	}
	
	// get valoarea unui anumit paragraf identificat prin index
	public String getParaListElem(int index) {
		return paraList.get(index);
	}

	// set valoarea unui anumit paragraf identificat prin index
	public void setParaListElem(int index, String text) {
		this.paraList.set(index, text);
	}
	
	
	public int getParaIndex() {
		return paraIndex;
	}

	public void setParaIndex(int paraIndex) {
		this.paraIndex = paraIndex;
	}

	
	public void setAuxDocxFilePath(String path) {
		this.auxDocxFilePath = path;
	}
	
	public String getAuxDocxFilePath() {
		return this.auxDocxFilePath;
	}
	
	public void setDocxOpen(boolean val) {
		this.isDocxOpen = val;
	}
	
	public boolean getDocxOpen() {
		return this.isDocxOpen;
	}
	
	/**
	 * <p>Se foloseste pentru ca afisarea numarului paragrafului sa arate bine si sa nu se shifteze
	 * <p>Daca de exemplu avem in total 15 paragrafe si suntem la primul, in mod normal se afiseaza "Paragraful 1/15"
	 * apasam next si tot mergem prin paragrafe, iar cand ajungem la paragraful 10 textul devine "Paragraful 10/15"
	 * in acest moment se mai adauga un caracter in String-ul care afiseaza si "misca layout-ul"
	 * adica butoanele sunt impinse putin mai incolo (nu cu multi pixeli, dar se poate considera totusi o jena)
	 * cu aceasta metoda construim un mod de afisare ca sa nu mai impinga layout-ul cand modificam textul
	 * cu aceasta metoda afiseaza numarului paragrafului va fi sub forma "Paragraful 01/15" in loc de "Paragraful 1/15"
	 * <p>Metoda calculeaza numarul de zerouri care trebuie puse inaintea numarului de dinainte de slash, si construieste textul respectiv
	 * ex. avem 1222 de paragrafe si suntem la paragraful 77, se va afisa "Paragraful 0077/1222", adica inaintea lui 77 se pune 2 zerouri
	 * @return
	 */
	public String paraIndexInfoDisplay() {
		String rez = "";
		int a = (int) Math.log10(paraList.size());
		int b = (int) Math.log10(paraIndex+1);
		System.out.println("PARA-INDEX-INFO: " + " a = " + a + " b = " + b );
		int t = a - b;
		for(int i=1;i<=t;i++)
			rez = rez + "0";
		rez = rez + (paraIndex + 1);
		return rez;
		
	}
	
}
