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
		MenuItem fileItem1 = new MenuItem("Import .txt");
		MenuItem fileItem2 = new MenuItem("Import .docx");
		MenuItem fileItem3 = new MenuItem("Exporteaza ca .txt");
		MenuItem fileItem4 = new MenuItem("Exporteaza ca .docx");
		
		file.getItems().add(fileItem1);
		file.getItems().add(fileItem2);
		file.getItems().add(fileItem3);
		file.getItems().add(fileItem4);
		
		
		// Doar pentru a testa chestii momentan
		
		
		// Import .txt
		fileItem1.setOnAction(e->{
			MenuBarEvents.importTXTFile(MainPage.mainStage);
		});
		
		
		
		// Export ca .txt
		fileItem3.setOnAction(e->{
			MenuBarEvents.exportTXTFile(MainPage.mainStage);
		});
		
		/*fileItem1.setOnAction(e->{
			List<String> a = new LinkedList<>();
			a.add("cuvant 1");
			a.add("cuvant 2");
			a.add("cuvant 3");
			a.add("cuvnat 4");
			RightClickMenu.setCorrectionOptions(a);
			ObservableList<MenuItem> l = RightClickMenu.correctionMenu.getItems();
			for(MenuItem i : l)
			i.setOnAction(new EventHandler<ActionEvent>() {
			    public void handle(ActionEvent t) {
			        System.out.println("Ai ales: " + i.getText());
			    }
			});
			
			
		});*/
		
		fileItem2.setOnAction(e->{
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
