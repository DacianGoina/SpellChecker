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
 *
 */
public class MenuBarFactory {
	
	// Meniu pentru aplicatie - optiunile de sus
	public static MenuBar getMenuBar() {
		MenuBar menuBar = new MenuBar();
		
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
			MenuBarEvents.importTXTFile(MainPage.mainStage);
		});
		
		
		// Import .docx
		importDOCX.setOnAction(e->{
			MenuBarEvents.importDOCXFile(MainPage.mainStage);
		});
		
		// Import .xlsx
		importXLSX.setOnAction(e->{
			MenuBarEvents.importXLSXFile(MainPage.mainStage);
		});
		
		// Export ca .txt
		exportTXT.setOnAction(e->{
			MenuBarEvents.exportTXTFile(MainPage.mainStage);
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
		Menu options = new Menu("Opțiuni");
		
		MenuItem optionsItem1 = new MenuItem("Golire zona text");
		options.getItems().add(optionsItem1);
		
		// Cand dai click pe "Golire zona text"
		optionsItem1.setOnAction(e->{
			MenuBarEvents.clearInputZone();
		});
		
		
		menuBar.getMenus().add(options);
		
		return menuBar;
	}
	

}
