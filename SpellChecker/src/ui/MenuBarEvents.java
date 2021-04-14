package ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;


/**
 * 
 * @author Dacian
 *
 */
public class MenuBarEvents {
	
	
	
	//protected final MainPage page;
	protected MainPage page;
	public MenuBarEvents(final MainPage page) {
	    this.page = page; // accesul la obiectul de baza
	}
	
	
	public String GetText() {
	    return page.getInputZoneText();
	}
	
	public void SetText(String text) {
	    page.setInputZoneText(text);
	}
	
	

	// dimensiunea maxima a unui fisier a carui text poate fi adus in aplicatie
	public final static int FILE_MAX_SIZE = 65535;

	// Golire zona text
	public final static void clearInputZone() {

		String inputZoneText = MainPage.getInputZoneText();

		if (inputZoneText.trim().replaceAll("\\s", "").length() == 0) // in cazul in care sunt doar spatii albe nu mai
																		// intreb daca vrei sa stergi
			MainPage.setInputZoneText("");

		else {

			Alert alert = AlertDialogFactory.createAlertConfirmation(AlertType.CONFIRMATION, "Da", "Nu",
					"Golire zona text", "Este sigur ca vrei sa stergi textul?", false, false);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.YES) { // actiunea care are loc cand apesi butonul "Da" din mesaj(alert)
				System.out.println("OK");
				MainPage.setInputZoneText("");
			}

		}

	}

	// Importare continut din document .txt
	public final static void importTXTFile(Stage mainStage) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selectare fisier");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text file (.txt)", "*.txt*"));

		String inputZoneText = MainPage.getInputZoneText();
		if (inputZoneText.trim().replaceAll("\\s", "").length() == 0) { // in cazul in care sunt doar spatii atunci se
																		// poate inlocui textul direct

			File file = fileChooser.showOpenDialog(mainStage);
			MenuBarEvents.readTXTFile(file);
		}

		else { // in cazul in care exista deja text in TextArea vei fi avertizat ca acesta va
				// fi inlocuit

			Alert alert = AlertDialogFactory.createAlertConfirmation(AlertType.WARNING, "Da", "Nu",
					"Importare fisier .txt", "Este sigur ca vrei sa importi un alt text?\nTexul actual va fi inlocuit!",
					false, false);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.YES) {
				System.out.println("OK");
				File file = fileChooser.showOpenDialog(mainStage);
				MenuBarEvents.readTXTFile(file);
			}
		}
	}

	// Verifica daca fisierul dat prin file respecta anumite conditii si ii citeste
	// continutul, apoi il pune in zona de text
	// Se foloseste metoda separata de importTXT pentru a face refolosi codul
	// in importTXT aceasta metoda se apeleaza de doua ori
	public static void readTXTFile(File file) {
		if (file != null) {

			System.out.println("Ai selectat: " + file.getName());
			String fileName = file.getName();
			if (!fileName.endsWith(".txt")) { // nu este .txt
				Alert a = AlertDialogFactory.createAlertInformation("Eroare", "Fisierul selectat nu are extensia .txt");
				a.show();
			}

			else if (!file.canRead()) { // nu este citibil
				Alert a = AlertDialogFactory.createAlertInformation("Eroare",
						"Fisierul ales nu are permisiune pentru citire");
				a.show();
			}

			else if (file.length() > FILE_MAX_SIZE) { // prea mare
				Alert a = AlertDialogFactory.createAlertInformation("Eroare ",
						"Fisierul ales are dimensiune prea mare");
				a.show();
			}

			else { // fisierul are tot ce trebuie pentru a fi folosit

				try (BufferedReader br = new BufferedReader(new FileReader(file))) {
					String st;
					StringBuilder sb = new StringBuilder();
					while ((st = br.readLine()) != null) {
						sb.append(st).append("\n");

					}
					
					
					MainPage.setInputZoneText(sb.toString()); // setare text
					
					
				} catch (IOException e) {
					Alert a = AlertDialogFactory.createAlertInformation("Eroare", "Eroare la citirea din fisier");
					a.show();
					e.printStackTrace();
				}

			}

		}

		else {
			// Nu a fost selectat un fisier (file == null), de ex ai inchis fereastra
			Alert a = AlertDialogFactory.createAlertInformation("Selectia invalida", "Nu ai selectat vreun fisier");
			a.show();
		}
	}

	public static void exportTXTFile(Stage mainStage) {
		String fileContent = MainPage.getInputZoneText();

		if (fileContent.trim().replace("\\s", "").length() == 0) {
			// sunt doar spatii libere in TextArea si nu merita sa salvezi
			Alert a = AlertDialogFactory.createAlertInformation("Eroare", "Nu exista text care poate fi salvat");
			a.show();
		}

		else {

			TextInputDialog inputfileName = AlertDialogFactory.createInputDialog("Nume fisier", "Preluare input",
					"Nume fisier nou: ");
			Optional<String> result = inputfileName.showAndWait();

			if (result.isPresent()) {

				System.out.println("NUME INTRODUS: " + result.get());

				if (validateFileName(result.get()) == true) {

					String fileName = result.get().trim().replaceAll("\\s{2,}", " ");
					if (fileName.endsWith(".txt") == false) // daca nu a pus el .txt la final de fisier ii pun eu
						fileName = fileName + ".txt";

					DirectoryChooser directoryChooser = new DirectoryChooser();

					File selectedDirectory = directoryChooser.showDialog(mainStage);
					if (selectedDirectory != null) {

						File[] filesList = selectedDirectory.listFiles(); // toate fisierele din director, verificam sa
																			// nu existe deja un fisier cu numele ales
																			// de noi
						for (File i : filesList) {
							if (i.getName().equals(fileName)) {
								Alert a = AlertDialogFactory.createAlertInformation("Eroare",
										"Exista deja un fisier cu acest nume in directorul selectat");
								a.show();
								return;
							}
						}

						System.out.println("Director ales: " + selectedDirectory);
						String path = selectedDirectory.getAbsolutePath() + "\\" + fileName;
						File newFile = new File(path);

						try (BufferedWriter buffer = new BufferedWriter(new FileWriter(newFile))) {
							buffer.write(fileContent);
							buffer.close();
							System.out.println("Scriere cu succes!");
							Alert a = AlertDialogFactory.createAlertInformation("Informare", "Fisierul a fost creat cu succes!");
							a.show();
						} catch (IOException e) {
							// in caz ca ceva nu functioneaza bine cu scriere atunci sterge fisierul nou
							// creat
							newFile.delete();
							Alert a = AlertDialogFactory.createAlertInformation("Eroare", "Create fisierului a esuat");
							a.show();
							e.printStackTrace();
						}

					} else {
						// nu s-a ales un director (de ex a inchis fereastra), dar am putea renunta la
						// acest ELSE - pur si simplu
						// nu se intampla nimic si gata, nu trebuie informat neaparat pt ca nu a ales
						// director
						Alert a = AlertDialogFactory.createAlertInformation("Eroare", "Nu ai ales un director");
						a.show();
					}

				} else { // daca numele ales pentru fisier nu respecta conditiile
					Alert a = AlertDialogFactory.createAlertInformation("Eroare", "Numele introdus este invalid");
					a.show();
				}
			}

		}
	}

	public static boolean validateFileName(String fileName) {
		if (fileName.trim().replaceAll("\\s{2,}", " ").length() > 1) // se va completa si cu alte conditii, de ex numele
																		// fisierului nu poate include unele caractere
			return true;
		return false;
	}
	
	
	public final static void importDOCXFile(Stage mainStage) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selectare fisier");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Word file (.docx)", "*.docx*"));

		String inputZoneText = MainPage.getInputZoneText();
		if (inputZoneText.trim().replaceAll("\\s", "").length() == 0) { // in cazul in care sunt doar spatii atunci se
																		// poate inlocui textul direct

			File file = fileChooser.showOpenDialog(mainStage);
			MenuBarEvents.readDOCXFile(file);
		}

		else { // in cazul in care exista deja text in TextArea vei fi avertizat ca acesta va
				// fi inlocuit

			Alert alert = AlertDialogFactory.createAlertConfirmation(AlertType.WARNING, "Da", "Nu",
					"Importare fisier .docx", "Este sigur ca vrei sa importi un alt text?\nTexul actual va fi inlocuit!",
					false, false);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.YES) {
				System.out.println("OK");
				File file = fileChooser.showOpenDialog(mainStage);
				MenuBarEvents.readDOCXFile(file);
			}
		}
	}
	
	public static void readDOCXFile(File file) {
		if (file != null) {

			System.out.println("Ai selectat: " + file.getName());
			String fileName = file.getName();
			if (!fileName.endsWith(".docx")) { 
				Alert a = AlertDialogFactory.createAlertInformation("Eroare", "Fisierul selectat nu are extensia .docx");
				a.show();
			}

			else if (!file.canRead()) { // nu este citibil
				Alert a = AlertDialogFactory.createAlertInformation("Eroare",
						"Fisierul ales nu are permisiune pentru citire");
				a.show();
			}

			else if (file.length() > FILE_MAX_SIZE) { // prea mare
				Alert a = AlertDialogFactory.createAlertInformation("Eroare ",
						"Fisierul ales are dimensiune prea mare");
				a.show();
			}

			else { // fisierul are tot ce trebuie pentru a fi folosit

				try (FileInputStream fis = new FileInputStream(file.getAbsolutePath())) {
					
					
					XWPFDocument document = new XWPFDocument(fis);
					List<XWPFParagraph> paragraphs = document.getParagraphs();
					
					StringBuilder sb = new StringBuilder();
					for (XWPFParagraph para : paragraphs) {		
	                    sb.append(para.getText());
	                    sb.append("\n\n");
	                }
					
					MainPage.setInputZoneText(sb.toString()); // setare text
					document.close();
					
					
				} catch (IOException e) {
					Alert a = AlertDialogFactory.createAlertInformation("Eroare", "Eroare la citirea din fisier");
					a.show();
					e.printStackTrace();
				}

			}

		}

		else {
			// Nu a fost selectat un fisier (file == null), de ex ai inchis fereastra
			Alert a = AlertDialogFactory.createAlertInformation("Selectia invalida", "Nu ai selectat vreun fisier");
			a.show();
		}
	}
	
	
	public final static void importXLSXFile(Stage mainStage) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selectare fisier");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel file (.xlsx)", "*.xlsx*"));

		String inputZoneText = MainPage.getInputZoneText();
		if (inputZoneText.trim().replaceAll("\\s", "").length() == 0) { // in cazul in care sunt doar spatii atunci se
																		// poate inlocui textul direct

			File file = fileChooser.showOpenDialog(mainStage);
			MenuBarEvents.readXLSXFile(file);
		}

		else { // in cazul in care exista deja text in TextArea vei fi avertizat ca acesta va
				// fi inlocuit

			Alert alert = AlertDialogFactory.createAlertConfirmation(AlertType.WARNING, "Da", "Nu",
					"Importare fisier .xlsx", "Este sigur ca vrei sa importi un alt text?\nTexul actual va fi inlocuit!",
					false, false);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.YES) {
				System.out.println("OK");
				File file = fileChooser.showOpenDialog(mainStage);
				MenuBarEvents.readXLSXFile(file);
			}
		}
	}
	
	public static void readXLSXFile(File file) {
		if (file != null) {

			System.out.println("Ai selectat: " + file.getName());
			String fileName = file.getName();
			if (!fileName.endsWith(".xlsx")) { // 
				Alert a = AlertDialogFactory.createAlertInformation("Eroare", "Fisierul selectat nu are extensia .xlsx");
				a.show();
			}

			else if (!file.canRead()) { // nu este citibil
				Alert a = AlertDialogFactory.createAlertInformation("Eroare",
						"Fisierul ales nu are permisiune pentru citire");
				a.show();
			}

			else if (file.length() > FILE_MAX_SIZE) { // prea mare
				Alert a = AlertDialogFactory.createAlertInformation("Eroare ",
						"Fisierul ales are dimensiune prea mare");
				a.show();
			}

			else { // fisierul are tot ce trebuie pentru a fi folosit

				try (FileInputStream fis = new FileInputStream(file.getAbsolutePath())) {
				
					
					StringBuilder sb = new StringBuilder();
					
					 //Create Workbook instance holding reference to .xlsx file
		            XSSFWorkbook workbook = new XSSFWorkbook(fis);
		 
		            //Get first/desired sheet from the workbook
		            XSSFSheet sheet = workbook.getSheetAt(0);
		 
		            //Iterate through each rows one by one
		            Iterator<Row> rowIterator = sheet.iterator();
		            while (rowIterator.hasNext()) 
		            {
		                Row row = rowIterator.next();
		                //For each row, iterate through all the columns
		                Iterator<Cell> cellIterator = row.cellIterator();
		                 
		                while (cellIterator.hasNext()) 
		                {
		                    Cell cell = cellIterator.next();
		                   
		                    if(cell.getCellType() == CellType.STRING) {
		                    	System.out.println("TEXT " + cell.getStringCellValue());
		                    	sb.append(cell.getStringCellValue());
		                    	sb.append("\n\n");
		                    } 
		                    else { // celula nu are tipul corespunzator
		                    	workbook.close();
		                    	System.out.println("CELULA NU ARE TIPUL CORESPUNZATOR");
		                    	throw new IOException();
		                    	
		                    }
		                    
		                }
		               
		            }
		            MainPage.setInputZoneText(sb.toString());
		            workbook.close();
		            
		        } catch (IOException e) {
		        
					Alert a = AlertDialogFactory.createAlertInformation("Eroare", "Eroare la citirea din fisier");
					a.show();
					e.printStackTrace();
				}

			}

		}

		else {
			// Nu a fost selectat un fisier (file == null), de ex ai inchis fereastra
			Alert a = AlertDialogFactory.createAlertInformation("Selectia invalida", "Nu ai selectat vreun fisier");
			a.show();
		}
	}

}
