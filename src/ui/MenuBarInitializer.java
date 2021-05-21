package ui;

import java.util.LinkedList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * 
 * @author Dacian
 * <p> MenuBarInitializer
 * Clasa care contruiește bara de meniu a aplicației. 
 * Bara de meniu conține mai multe submeniuri care oferă diverse acțiuni, de ex. importare text, exportare text etc
 *
 */
public class MenuBarInitializer {
	
	// Meniu pentru aplicatie - optiunile de sus
	
	public static MenuBar getMenuBar(final MainPage page) {
		MenuBar menuBar = new MenuBar();
			
		final MenuBarEvents events = new MenuBarEvents(page);
		
		// Meniul 1 - Fisier
		Menu file = new Menu("Fișier");
		
		// submeniuri pentru import si export
		Menu importMenu = new Menu("Import");
		Menu exportMenu = new Menu("Export");
		
		
		// elementele de la submeniuri
		MenuItem importTXT = new MenuItem("Import .txt");
		MenuItem importDOCX = new MenuItem("Import .docx");
		MenuItem importXLSX = new MenuItem("Import .xlsx"); // excel nou
		MenuItem exportTXT = new MenuItem("Exporteaza ca .txt");
		MenuItem exportDOCX = new MenuItem("Exporteaza ca .docx");
		MenuItem exportXLSX = new MenuItem("Exporteaza ca .xlsx"); 
		
		
		
		//adauga elemente in submeniuri
		importMenu.getItems().addAll(importTXT, importDOCX, importXLSX);
		exportMenu.getItems().addAll(exportTXT, exportDOCX, exportXLSX);
		
		
		//adauga submeniuri
		file.getItems().addAll(importMenu, exportMenu);
		
		// Doar pentru a testa chestii momentan
		
		
		// Import .txt
		importTXT.setOnAction(e->{
			events.importTXTFile(page.mainStage);
		});
		
		
		// Import .docx
		importDOCX.setOnAction(e->{
			events.importDOCXFile(page.mainStage);
		});
		
		// Import .xlsx
		importXLSX.setOnAction(e->{
			events.importXLSXFile(page.mainStage);
		});
		
		// Export ca .txt
		exportTXT.setOnAction(e->{
			events.exportTXTFile(page.mainStage);
		});
		
	
		//Export ca .docx
		exportDOCX.setOnAction(e->{
			events.exportDOCXFile(page.mainStage);
		});
		
		
		//Export ca excel
		exportXLSX.setOnAction(e->{
			events.exportXLSXFile(page.mainStage);
		});
		
		/*VA FI NEVOIE DE CODUL ACESTA DIN EVENT 
		importDOCX.setOnAction(e->{
			List<String> a = new LinkedList<>();
			a.add("cuvant 5");
			a.add("cuvant 6");
			a.add("cuvant 7");
			a.add("cuvnat 8");
			RightClickMenu.setCorrectionOptions(a);
			ObservableList<MenuItem> l = RightClickMenu.correctionMenu.getItems();
			for(MenuItem i : l)
				i.setOnAction(new EventHandler<ActionEvent>() {
				    public void handle(ActionEvent t) {
				        System.out.println("Ai ales: " + i.getText());
				    }
				});
			
		});
		*/
		
		
		menuBar.getMenus().add(file);
		
		
		// Meniul 2 - Optiuni
		Menu paragraph = new Menu("Paragraf");
		MenuItem copyParagraph = new MenuItem("Copiere");
		MenuItem pasteParagraph = new MenuItem("Lipire");
		MenuItem clearParagraph = new MenuItem("Golire paragraf");
		MenuItem clearAllParagraphs = new MenuItem("Goleste toate paragrafele");
		paragraph.getItems().addAll(copyParagraph, pasteParagraph, clearParagraph, clearAllParagraphs);
		
		
		//Copiere pe clipboard
		copyParagraph.setOnAction(e->{
			events.copyToClipboard();
		});
		
		pasteParagraph.setOnAction(e->{
			events.pasteFromClipboard();
		});
		
		// Golire paragraf
		clearParagraph.setOnAction(e->{
			events.clearParagraph();
		});
		
		// Goleste toate paragrafele
		clearAllParagraphs.setOnAction(e->{
			events.clearAllParagraphs();
		});
		
		
		menuBar.getMenus().add(paragraph);
		
		
		// Dictionar
		Menu dict = new Menu("Dicționar");
		MenuItem checkWord = new MenuItem("Verificare cuvânt"); // daca exista un anumit cuvant
		MenuItem ignore = new MenuItem("Ignorare cuvânt"); // ignore
		MenuItem unIgnore = new MenuItem("Eliminare ignorare"); // elimna ignorare
		MenuItem addToDict = new MenuItem("Adaugare cuvânt"); // adauga in dictionar
		MenuItem removeFromDict = new MenuItem("Eliminare cuvânt"); // scoate din dictionar - de ex am introdus un cuvant din greseala
		dict.getItems().addAll(checkWord, ignore, unIgnore, addToDict, removeFromDict);
		
		checkWord.setOnAction(e->{
			events.checkWord();
		});
		
		ignore.setOnAction(e->{
			events.ignore();
		});
		
		unIgnore.setOnAction(e->{
			events.unIgnore();
		});
		
		addToDict.setOnAction(e->{
			events.addToDict();
		});
		
		removeFromDict.setOnAction(e->{
			events.removeFromDict();
		});
		
		menuBar.getMenus().add(dict);
		
		// Setare ID-uri pentru meniu si componente (pentru a folosi CSS pe ele)
		for(Menu i : menuBar.getMenus()) {
			i.setId("menuHeader");
			for(MenuItem j : i.getItems())
				j.setId("menuItem");
		}
		
		
		menuBar.getStylesheets().add(MenuBarInitializer.class.getResource("style_MainPage.css").toExternalForm());
		return menuBar;
	}
	

}
