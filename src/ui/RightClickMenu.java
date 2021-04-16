package ui;


/**
 * 
 * @author Dacian
 * RightClickMenu
 * Clasa crează obiecte de tip ContextMenu, aceste meniuri (este afișat doar un meniu la un moment dat) se vor afișa la inspectarea unui cuvânt.
 * De ex. dacă un cuvânt este marcat ca fiind greșit iar utilizatorul selectează (click dreapta) cuvântul atunci va fi afișat un meniu
 * care va conține mai multe acțiuni care se vor putea efectua asupra acelui cuvânt, 
 * de ex. opțiunea de adăugare cuvânt în dicționar, sau o listă cu câteva cuvinte apropiate de cuvântul inițial - selectând un cuvânt din această listă putem înlocui (corecta) cuvântul pe care am făcut click
 */
import java.util.List;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class RightClickMenu {
	
	// folosesc obiecte statice pentru a nu crea obiecte noi la fiecare click dreapta
	
	// se va modifica vizibilitate ulterior - am folosit public doar pt teste
	private static ContextMenu contextMenu = new ContextMenu();
	public static MenuItem ignore = new MenuItem("Ignora");
	private static MenuItem addToDict = new MenuItem("Adauga in dictionar");
	public static Menu correctionMenu = new Menu("Corectare");
	
	
	// Creare meniu care apare cand se apasa click dreapta
	public static ContextMenu getRightClickMenu() {
	     contextMenu.getItems().addAll(ignore,addToDict, correctionMenu);
	     
		return contextMenu;
	}
	
	
	// Pentru a seta optiunile de corectare - maxim 4-5 variante de corectare sa zicem
	public static void setCorrectionOptions(List<String> l) {
		correctionMenu.getItems().clear();
		for(String i:l) {
			MenuItem childMenuItem = new MenuItem(i);
			correctionMenu.getItems().add(childMenuItem);
		}
		
	
			
	}
	
	
	// Cele 3 optiuni nu vor putea fi apasate tot timpul
	// De exemplu nu are rost sa folosim optiunea Ignorare pentru un cuvant corect - el oricum nu va fi marcat
	
	
	// ignore
	public static void enableIgnore() {
		ignore.setDisable(false);
	}
	
	public static void disableIgnore() {
		ignore.setDisable(true);
	}
	
	// addToDict
	public static void enableAddToDict() {
		addToDict.setDisable(false);
	}
	
	public static void disableAddToDict() {
		addToDict.setDisable(true);
	}
	
	// correctionMenu
	public static void clearCorrectionMenu(){
		correctionMenu.getItems().clear();
	}
	
	public static void enableCorrectionMenu() {
		correctionMenu.setDisable(false);
	}
	
	public static void disableCorrectionMenu() {
		correctionMenu.setDisable(true);
	}
	
	
	
	

}
