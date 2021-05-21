package ui;

import java.awt.event.MouseEvent;
import java.util.List;

import db.DB;
import db.WordObj;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;

// Clasa pentru meniu dreapta - apare cand facem click dreapta in CodeArea
// Momentan folosim clasa asta ca sa nu stricam cealalta clasa (RightClickMenu)
public class RightClickMenuAux {
	private ContextMenu contextMenu;// = new ContextMenu();
	private MenuItem ignore; // = new MenuItem("Ignora");
	private MenuItem addToDict; // = new MenuItem("Adauga in dictionar");
	private Menu correctionMenu; // = new Menu("Corectare");
	private MainPage mainPage;
	
	private DB auxDB = new DB();
	
	// Instantiaza un obiect de tip RightClickMenuAux care practic se va folosi ca un ContextMenu (obiect JavaFx)
	public RightClickMenuAux(MainPage p) {
		super();
		this.mainPage = p;
		this.contextMenu = new ContextMenu();
		this.ignore = new MenuItem("Ignora");
		this.addToDict = new MenuItem("Adauga in dictionar");
		this.correctionMenu = new Menu("Corectare");
		this.contextMenu.getItems().addAll(this.ignore, this.addToDict, this.correctionMenu);
	}
	
	public ContextMenu getContextMenu() {
		return this.contextMenu;
	}
		
	
	// Activare events pentru ignore si addToDict, pentru alea din correctionMenu trebuie separat
	// Oricum vor fi apasabile doar cand se face click pe un cuvant gresit
	public void enableClickEvents() {
		this.ignore.setOnAction(e->{
			int[] indici = mainPage.getIndici();
			String cuvant = mainPage.getCodeArea().getText().substring(indici[0],indici[1]);
			ignoreWord(cuvant);
			System.out.println("IGNORE: " + cuvant);
		});
		

		this.addToDict.setOnAction(e->{
			int[] indici = mainPage.getIndici();
			String cuvant = mainPage.getCodeArea().getText().substring(indici[0],indici[1]);
			addWordToDict(cuvant);
			System.out.println("ADD TO DICT: " + cuvant);
		});
		
	}
	
	// Primeste un cuvant (word) si il va ignora: il adauga in dictionar dar un in baza de date (deci doar local)
	// Vor fi ignorate toate cuvintele egale cu word (de ex ignoram "aabb" dar intr-un text avem mai multe cuvinte "aabb",
	// nu le dam ignore la fiecare, ci doar la unul )
	// Il pune doar TreeMap, adica este ceva local, cand se va redeschide aplicatia din nou cuvantul va aparea din nou gresit
	// astfel trebuie ignorat din nou (daca se doreste acest lucru)
	public void ignoreWord(String word) {
		mainPage.getDict().put(word, null); // adauga in dictionar, nu trebuie WordObj cu valori specifice
		List<List<Integer>> lIndiciCuvinte = mainPage.getLIndiciCuvinte();
		String text = mainPage.getCodeArea().getText();
		if(lIndiciCuvinte != null) { // inlocuire in text cu stilul respectiv: peste tot unde apare word pune stilul normal, deoarece acum word este corect
			for(int i=0;i<lIndiciCuvinte.get(0).size();i++) { 
				int begin = lIndiciCuvinte.get(0).get(i);
				int end = lIndiciCuvinte.get(1).get(i);
				String cuvant = text.substring(lIndiciCuvinte.get(0).get(i), lIndiciCuvinte.get(1).get(i));
				if(cuvant.equals(word)) // daca cuvantul delimitat (cu indici begin, end) este word
					mainPage.getCodeArea().setStyleClass(begin, end, mainPage.normalStyle);
			}
		}
		// resetare indicii 'clicked' deoarece acum nu mai suntem pe cuvant gresit
		mainPage.setIndici(-1, -1);
	}
	
	
	// Primeste un cuvant (word) si il adauga in dictionar (fisier .db)
	// Dupa asta, toate cuvintele egale cu el vor fi considerate corecte (de ex avem "aabb" in mai multe locuri si il adaugam in dictionar)
	// Toate aparitiile lui "aabb" vor fi corecte, sa nu facem adaugare la fiecare aparitie
	public void addWordToDict(String word) {
		WordObj a = new WordObj(word); // creare cuvant nou --- vezi constructor in db.WordObj ---
		auxDB.insertCuvantNou(a); // adauga in baza de date
		mainPage.getDict().put(word, a); // adauga in dictionar
		
		// Acum se continua ca la ignoreWord() - word este corect deci pune stilul de cuvant corect, reseteaza indicii
		List<List<Integer>> lIndiciCuvinte = mainPage.getLIndiciCuvinte();
		String text = mainPage.getCodeArea().getText();
		if(lIndiciCuvinte != null) { // inlocuire in text cu stilul respectiv: peste tot unde apare word pune stilul normal, deoarece acum word este corect
			for(int i=0;i<lIndiciCuvinte.get(0).size();i++) { 
				int begin = lIndiciCuvinte.get(0).get(i);
				int end = lIndiciCuvinte.get(1).get(i);
				String cuvant = text.substring(lIndiciCuvinte.get(0).get(i), lIndiciCuvinte.get(1).get(i));
				if(cuvant.equals(word)) // daca cuvantul delimitat (cu indici begin, end) este word
					mainPage.getCodeArea().setStyleClass(begin, end, mainPage.normalStyle);
			}
		}
		// resetare indicii 'clicked' deoarece acum nu mai suntem pe cuvant gresit
		mainPage.setIndici(-1, -1);
		
	}
	
	// Event pentru MenuItems care reprezinta optiunile de corectare
	public void clickEventsCorrectionMenu() {
		ObservableList<MenuItem> l = this.correctionMenu.getItems();
		if(l != null) {
			for(MenuItem i : l)
				i.setOnAction(e->{
					System.out.println("AI APASAT: " + i.getText());
					correctionWith(i.getText());
				});
		}
	}
	
	// Folosita pentru a corecta (partea de replace) cuvantul selectat in MainPage cu cuvantul dat (word)
	public void correctionWith(String word) {
		int[] indici = mainPage.getIndici();
		mainPage.getCodeArea().replaceText(indici[0], indici[1], word); // inlocuire efectiva
		mainPage.setIndici(-1, -1); // resetare indici deoarece acum noul cuvant este corect
	}
	
	
	// Pentru a seta optiunile de corectare - maxim 4-5 variante de corectare sa zicem
	public void setCorrectionOptions(List<String> l) {
		this.correctionMenu.getItems().clear();
		if(l != null) {
			for(String i:l) {
				MenuItem childMenuItem = new MenuItem(i);
				this.correctionMenu.getItems().add(childMenuItem);
			}
		}
	}
	// Cele 3 optiuni nu vor fi apasabile tot timpul
	
	public void enableIgnore() {
		this.ignore.setDisable(false);
	}
	
	public void disableIgnore() {
		this.ignore.setDisable(true);
	}
	
	public void enableAddToDict() {
		this.addToDict.setDisable(false);
	}
	
	public void disableAddToDict() {
		this.addToDict.setDisable(true);
	}
	
	public void enableCorrectionMenu() {
		this.correctionMenu.setDisable(false);
	}
	
	public void disableCorrectionMenu() {
		this.correctionMenu.setDisable(true);
	}
	
	public void enableAll() {
		this.ignore.setDisable(false);
		this.addToDict.setDisable(false);
		this.correctionMenu.setDisable(false);
	}
	
	public void disableAll() {
		this.ignore.setDisable(true);
		this.addToDict.setDisable(true);
		this.correctionMenu.setDisable(true);
	}
}
