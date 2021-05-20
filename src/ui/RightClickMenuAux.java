package ui;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

// Clasa pentru meniu dreapta - apare cand facem click dreapta in CodeArea
// Momentan folosim clasa asta ca sa nu stricam cealalta clasa (RightClickMenu)
public class RightClickMenuAux {
	private ContextMenu contextMenu;// = new ContextMenu();
	private MenuItem ignore; // = new MenuItem("Ignora");
	private MenuItem addToDict; // = new MenuItem("Adauga in dictionar");
	private Menu correctionMenu; // = new Menu("Corectare");
	
	// Instantiaza un obiect de tip RightClickMenuAux care practic se va folosi ca un ContextMenu (obiect JavaFx)
	public RightClickMenuAux() {
		super();
		this.contextMenu = new ContextMenu();
		this.ignore = new MenuItem("Ignora");
		this.addToDict = new MenuItem("Adauga in dictionar");
		this.correctionMenu = new Menu("Corectare");
		this.contextMenu.getItems().addAll(this.ignore, this.addToDict, this.correctionMenu);
	}
	
	public ContextMenu getContextMenu() {
		return this.contextMenu;
	}
	
	// Pentru ignore si addToDict, pentru alea din correctionMenu trebuie separat
	public void enableClickEvents() {
		this.ignore.setOnAction(e->{
			System.out.println("IGNORA");
			
		});
		
		this.addToDict.setOnAction(e->{
			System.out.println("ADD TO DICT");
		});
		
	}
	
	public void clickEventsCorrectionMenu() {
		ObservableList<MenuItem> l = this.correctionMenu.getItems();
		if(l != null) {
			for(MenuItem i : l)
				i.setOnAction(e->{
					System.out.println("AI APASAT: " + i.getText());
				});
		}
	}
	
	// Pentru a seta optiunile de corectare - maxim 4-5 variante de corectare sa zicem
	public void setCorrectionOptions(List<String> l) {
		correctionMenu.getItems().clear();
		if(l != null) {
			for(String i:l) {
				MenuItem childMenuItem = new MenuItem(i);
				correctionMenu.getItems().add(childMenuItem);
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
