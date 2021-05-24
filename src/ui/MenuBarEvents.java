package ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import db.DB;
import db.WordObj;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;


/**
 * 
 * @author Dacian
 * <p>MenuBarEvents
 * Clasă care implementează funcționalități pentru bara de meniu: 
 * mai exact funcții care sunt apelate în momentul în care utilizatorul interacționează cu submeniurile
 * Practic, apăsarea unei opțiuni din meniu (a unui submeniu) declanșează un mouse event iar efectul acestuia este executarea metodelor din această clasă.
 * De ex. dacă utilizatorul selectează opțiunea "Import .txt" atunci se va apela metoda importTXTFile 
 * care îi permite utilizatorul să aducă conținutul unui fișier .txt în aplicație pentru a face prelucrări (corectare) pe acel text
 */
public class MenuBarEvents {
	
	/**
	 * 
	 * <p>OBSERVARII LA DOCUMENTATIE DIN ACEASTA CLASA
	 * <p>E scrisa cu scopul de a ajuta pe cei care scriu documentatia proiectului 
	 * <p>Nu e scrisa cu diacritice
	 * <p>Este explicata in mare fiecare metoda
	 * <p>In metodele "inrudite" (ex: readTXTFile, readDOCXFile, readXLSXFile) structura metodelor este asemanatoare, validarile sunt asemanatoare
	 *  de ex."inainte de a face export se actualizeaza textul (din inputZone in paraList)" din aceasta cauza acest lucru nu este
	 *  explicat la toate cele 3 metode (readTXTFile, readDOCXFile, readXLSXFile), doar la una(cateva) dintre ele
	 *  alt ex: "la importDOCX se verifica daca numele ales pentru fisier este valid"
	 */
	
	protected MainPage page;
	private DB DBObj = new DB();
	public MenuBarEvents(final MainPage page) {
	    this.page = page; // accesul la obiectul de baza
	}
	
	/**
	 * <p>Get textul din inputZone din page(MainPage)
	 * <p>Posibil sa se foloseasca mai incolo daca folosim o interfata
	 * MOMENTAN accesul la textul din inputZone se face prin page.getInputZoneText()
	 * @return
	 */
	public String GetText() {
	    return page.getInputZoneText();
	}
	
	/**
	 * <p>Set textul din inputZone in page(MainPage)
	 * <p>Posibil sa se foloseasca mai incolo daca vom folosi interfata
	 * <p>MOMENTAN setarea textul in inputZone se face prin page.setInputZoneText(String a)
	 * @param text
	 */
	public void SetText(String text) {
	    page.setInputZoneText(text);
	}
	
	
	/**
	 *<p>Dimensiunea maxima pe care o poate avea un fisier pentru a putea fi importat
	 *<p>Unitate de masura : bytes, se poate verifica cu file.length() unde file este un obiect de tip File (gasesti prin metodele cu import)
	 */
	public final static int FILE_MAX_SIZE = 2147483647; // 2GB

	/**
	 * <p> Sterge textul din paragraful curent - recunoscut prin paraIndex din variabila page (obiect MainPage)
	 * <p> Daca paragraful contine doar spatii albe atunci textul este sters direct
	 * <p> Daca paragraful contine text efectiv atunci utilizatorul trebuie sa confirme ca doreste stergerea textului
	 */
	public final void clearParagraph() {
		
		String paraText = page.getInputZoneText(); // get textul din paragraful curent
		
		if (paraText.trim().replaceAll("\\s+", "").length() == 0) { // in cazul in care sunt doar spatii albe sterge direct																	
			page.setParaListElem(page.getParaIndex(), "");; // goleste paragraful
			page.setInputZoneText(page.getParaListElem(page.getParaIndex())); // seteaza textul in inputZone
		}

		else { // in cazul in care este text, intreaba utilizatorul sa confirme stergerea

			Alert alert = AlertDialogFactory.createAlertConfirmation(AlertType.CONFIRMATION, "Da", "Nu",
					"Golire zona text", "Este sigur ca vrei sa stergi textul?", false, false);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.YES) { // actiunea care are loc cand apesi butonul "Da" din mesaj(alert)
				System.out.println("OK");
				page.setParaListElem(page.getParaIndex(), "");
				page.setInputZoneText(page.getParaListElem(page.getParaIndex())); 
			}

		}

	}
	
	
	/**
	 * <p>Sterge textul din toate paragrafele
	 * <p>Prin stergerea tuturor paragrafelor se revine la un singur paragraf
	 * <p>Daca exista doar un paragraf atunci se foloseste metoda anterioara
	 * <p>Daca paragrafele contin doar spatii albe atunci se golesc direct
	 * <p>Daca cel putin un paragraf contine text efectiv atunci utilizatorul trebuie sa confirme ca vrea golirea paragrafelor
	 */
	public final void clearAllParagraphs() {
		if(page.getParaList().size() == 1) { // daca avem doar un paragraf se foloseste metoda definita anterior
			System.out.println("clearAllParagraphs - AVEM DOAR UN PARAGRAF");
			clearParagraph();
			page.setDocxOpen(false);
		}
		else {
			System.out.println("clearAllParagraphs - AVEM MAI MULTE PARAGRAFE");
			page.setParaListElem(page.getParaIndex(), page.getInputZoneText()); // updateaza textul in paragraful curent
			
			// daca toate paragrafele au doar spatii albe atunci se sterg direct
			for(String i : page.getParaList()) {
				String paraText = i;
				if (paraText.trim().replaceAll("\\s+", "").length() != 0) { // daca cel putin un paragraf contine text, utilizatorul trebuie sa confirme stergerea
					
					Alert alert = AlertDialogFactory.createAlertConfirmation(AlertType.CONFIRMATION, "Da", "Nu",
							"Golire paragrafe", "Este sigur ca vrei sa stergi tot textul?", false, false);

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.YES) { 
						System.out.println("OK");
						page.initializeParaList();
						page.setDocxOpen(false);
					}
					
					return; // oprire metoda - intreb utilizatorul doar o data - nu pentru fiecare paragraf care contine text
				}
				
			}
			//daca s-a ajuns aici atunci nu s-a intrat in primul if din for, adica toate paragrafele au doar spatii albe
			page.initializeParaList();
			page.setDocxOpen(false);
		}
	}

	/**
	 * <p>Importeaza text dintr-un fisier TXT
	 * <p>Textul va fi adus sub forma unui singur paragraf
	 * <p>Intai updateaza textul in paragraful curent (din inputZone in paragraful curent)
	 * @param mainStage
	 */
	public final void importTXTFile(Stage mainStage) {
		page.setParaListElem(page.getParaIndex(), page.getInputZoneText()); // salveaza modificarile facute pe text in paragraful curent
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selectare fisier");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text file (.txt)", "*.txt*"));
		
		String text = getAllText();
		if (text.trim().replaceAll("\\s+", "").length() == 0) { // in cazul in care sunt doar spatii atunci se poate inlocui textul direct
			File file = fileChooser.showOpenDialog(mainStage);
			readTXTFile(file);
		}

		else { // in cazul in care exista deja text in paragrafe - in aplicatie vei fi avertizat ca va fi inlocuit

			Alert alert = AlertDialogFactory.createAlertConfirmation(AlertType.WARNING, "Da", "Nu",
					"Importare fisier .txt", "Este sigur ca vrei sa importi un alt text?\nTexul actual va fi inlocuit!",
					false, false);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.YES) {
				System.out.println("OK");
				File file = fileChooser.showOpenDialog(mainStage);
				readTXTFile(file);
			}
		}
	}

	
	/**
	 *<p>Primeste ca parametru un obiect File care reprezinta fisierul care trebuie citit 
	 *<p>Se foloseste pentru a citi text dintr-un fisier TXT
	 *<p>Se apeleze in metoda importTXT
	 *<p>Se foloseste metoda separata de importTXT pentru a face refolosi codul - in importTXT aceasta metoda se apeleaza de doua ori
	 *<p>Verifica daca fisierul selectat are extensie .txt
	 *<p>Verifica daca fisierul selectat are permisiune pentru a fi citit de utilizatorul curent
	 *<p>Verifica daca fisierul selectat nu depaseste dimensiunea maxima permisa
	 *<p>Daca documentul curent contine deja mai multe paragrafe (de ex. am deschis anterior un DOCX) acestea vor fi sterse si vom avea doar unul
	 * @param file
	 */
	public  void readTXTFile(File file) {
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
					
					
					// Afisare text modificat in lista de paragrafe - un txt este doar un paragraf
					page.initializeParaList(); // distruge paragrafele anterioare deoarece TXT este adus ca unul singur
					page.setParaListElem(page.getParaIndex(), sb.toString()); // modificare in array
					page.setInputZoneText(page.getParaListElem(page.getParaIndex())); // afisare in inputZone
					page.setDocxOpen(false);
					
					
					
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

	
	/**
	 * <p> Se foloseste cand este nevoie sa stim textul din toate paragrafele
	 * <p> Concateneaza toate paragrafele (toate elementele din paraList) intr-un singur obiect String
	 * <p>Ca delimitator intre paragrafe se foloseste \n\n
	 * <p>String-ul rezultat este folosit pentru a fi scris cand se face exportTXT
	 * @return
	 */
	public String getAllText() {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<page.getParaList().size()-1;i++) {
			sb.append(page.getParaListElem(i));
			sb.append("\n\n");
		}
		sb.append(page.getParaListElem(page.getParaList().size() - 1));
		String fileContent = sb.toString();
		
		return fileContent;
	}
	
	
	/**
	 * <p>Exporteaza textul din aplicatie sub forma unui fisier TXT
	 * <p>Fisierul DOCX in cauza poate sa contina si altceva inafara de paragrafe (ex. tabele, imagini, ecuatii etc)
	 * <p>Paragraf nu inseamna doar ceva care incepe cu \t, orice text normal este vazut ca paragraf (ex: paragraf clasic, titlu etc) de catre APACHE POI
	 * <p>Din aceasta cauza fisierele mai pot contine si unele paragrafe vide (goale) care nu sunt aduse ca paragrafe in aplicatie (in paraList)
	 *  dar se tine seama de ele atunci cand se face exportDOCX cu getDocxOpen() == true, in fisierul rezultat paragrafele vide vor fi tot vide
	 *  si vor fi la locul lor (acolo unde au fost in fisierul origininal)
	 * <p>Intai updateaza textul in paragraful curent (din inputZone in paragraful curent)
	 * <p>Citeste (user input) numele pe care il va avea noul fisier - daca utilizatorul nu pune .txt la final se pune automat
	 * <p>Citeste directorul unde va fi salvat noul fisier
	 * <p>Verifica ca in directorul ales sa nu existe un alt fisier cu aceeasi nume si aceeasi extensie
	 * <p>Valideaza numele noului fisier(lungime, caractere speciale) folosind validateFileName()
	 * @param mainStage
	 */
	public void exportTXTFile(Stage mainStage) {
		page.setParaListElem(page.getParaIndex(),page.getInputZoneText()); // salveaza modificarile facute pe text in paragraful curent
		String fileContent = getAllText();

		if (fileContent.trim().replace("\\s+", "").length() == 0) { // sunt doar spatii libere in fileContent si nu merita sa salvezi
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

	/**
	 * TO-DO de implementat complet
	 * <p>Valideaza numele fisierul (pentru export) sa respecte anumite conditii - lungime, sa nu contine caractere ilegale - de ex. "*" in Windows
	 * @param fileName
	 * @return
	 */
	public static boolean validateFileName(String fileName) {
		if (fileName.trim().replaceAll("\\s{2,}", " ").length() > 1) // se va completa si cu alte conditii, de ex numele
																		// fisierului nu poate include unele caractere
			return true;
		return false;
	}
	
	/**
	 * <p>Citeste un fisier DOCX si aduce continutul paragrafelor din el in  aplicatie
	 * <p>Se foloseste de metoda readDOCXFile()
	 * @param mainStage
	 */
	public final void importDOCXFile(Stage mainStage) {
		page.setParaListElem(page.getParaIndex(), page.getInputZoneText());
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selectare fisier");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Word file (.docx)", "*.docx*"));

		String text = getAllText();
		if (text.trim().replaceAll("\\s+", "").length() == 0) { // in cazul in care sunt doar spatii atunci se
																		// poate inlocui textul direct

			File file = fileChooser.showOpenDialog(mainStage);
			readDOCXFile(file);
		}

		else { // daca exista deja text aplicatie va trebuie sa confirmi daca vrei sa continui importul

			Alert alert = AlertDialogFactory.createAlertConfirmation(AlertType.WARNING, "Da", "Nu",
					"Importare fisier .docx", "Este sigur ca vrei sa importi un alt text?\nTexul actual va fi inlocuit!",
					false, false);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.YES) {
				System.out.println("OK");
				File file = fileChooser.showOpenDialog(mainStage);
				readDOCXFile(file);
			}
		}
	}
	
	/**
	 *<p>Primeste @param file care reprezinta calea(path) spre un fisier DOCX
	 *<p>Citeste efectiv fisierul si aduce continutul paragrafelor in aplicatie
	 *<p>Este apelata in importDOCXFile
	 * @param file
	 */
	public void readDOCXFile(File file) {
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
					
					page.setAuxDocxFilePath(file.getAbsolutePath()); // salveaza fisierul
					page.setDocxOpen(true); // retine ca am deschis un fisier DOCX
					
					page.initializeParaList();
					
					// pune textul din paragrafe in Array pe pozitii corespunzatoare
					int i = 0;
					int counter = 0;
					while(i<paragraphs.size()) {
						String a = new String(paragraphs.get(i).getText());
						if(a.trim().replaceAll("\\s+", "").length() != 0) { // daca sunt paragrafe goale nu le adaug
							if(counter==0)
								page.setParaListElem(0, a);
							else
								page.getParaList().add(a);
							System.out.println("PARAGRAFUL " + counter + ": " + a);
							counter++;
							
							}
						i++;
						}
						
					/*
					for(int i=0;i<paragraphs.size();i++)
						if(i==0) // deja exista un element String in Array
							page.setParaListElem(0, paragraphs.get(0).getText());
						else {
							String a = new String(paragraphs.get(i).getText());
							if(a.trim().replaceAll("\\s", "").length() != 0) // daca sunt paragrafe goale nu le adaug
								page.getParaList().add(a);
						}
					*/
					if(page.getParaList().size() > 1) // daca avem mai mult de 1 paragraf activeaza butoanele
						page.enableBottomButtons();
					
					page.setInputZoneText(page.getParaListElem(page.getParaIndex())); // setare text (paraList poz 0 in inputZone)
					page.setParaInfo();
					document.close();
					
					System.out.println("Am incarcat " + page.getParaList().size() + " paragrafe din fisier docx");
					
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
	
	/**
	 *<p>Exporta textul din aplicatie sub forma unui fisier DOCX
	 *<p>Fiecare paragraf din aplicatie va fi un paragraf in fisier
	 *<p>Se foloseste o copie a fisierul original pentru a crea un fisier nou cu acceasi structura ca cel originial (tabele, imagini etc)
	 *<p> (se aplica doar daca anterior (cel mai recent) am importat un fisier DOCX - adica ii cunosc structura)
	 *<p>In fisierul exportat vor fi modificate doar paragrafele
	 *<p>In cazul in care anterior(cel mai recent) nu am importat docx (getDocxOpen(false)) atunci se va crea un fisier docx care contine doar paragrafe (fara tabele, imagini etc)
	 *<p>Se afiseaza mesaj corespunzator daca nu s-a ales un director (in caz ca exista posibilitatea asta) sau daca numele ales pentru fisier este invalid
	 * @param mainStage
	 */
	public void exportDOCXFile(Stage mainStage) {
		page.setParaListElem(page.getParaIndex(),page.getInputZoneText()); // salveaza modificarile facute pe text in paragraful curent
		String fileContent = getAllText();

		if (fileContent.trim().replace("\\s+", "").length() == 0) { // sunt doar spatii libere in fileContent si nu merita sa salvezi
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
					if (fileName.endsWith(".docx") == false) // daca nu a pus el .docx la final de nume fisier ii pun eu
						fileName = fileName + ".docx";

					DirectoryChooser directoryChooser = new DirectoryChooser();

					File selectedDirectory = directoryChooser.showDialog(mainStage);
					if (selectedDirectory != null) {

						File[] filesList = selectedDirectory.listFiles(); // toate fisierele din director, verificam sa nu existe deja un fisier cu numele ales
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
						
						if(page.getDocxOpen() == true) { // anterior am deschis un DOCX deci modificam paragrafele si folosim aceeasi structura
							
							File newFile = new File(path);		
					        try(OutputStream fileOut = new FileOutputStream(newFile)) {  
					            
					        	XWPFDocument document = new XWPFDocument(); 
								XWPFDocument auxDocument = new XWPFDocument();  
					        	XWPFDocument iniDocument = new XWPFDocument(new FileInputStream(page.getAuxDocxFilePath()));
								
								document = iniDocument.getXWPFDocument();
								
								int paraCounter = 0; // numara paragrafele efective din aplicatie
								int paraDocxCounter = 0; // numara paragrafele efective din fisierul docx, pot fi si paragrafe goale
								while(paraCounter < page.getParaList().size()) {
									String a = document.getParagraphs().get(paraDocxCounter).getText();
									if(a.trim().replace("\\s+", "").length() != 0) { // daca paragraful nu e doar spatii goale
										String paraValue = page.getParaListElem(paraCounter);
										XWPFParagraph newPara = auxDocument.createParagraph();
										// vezi daca poti rezolva cu stilul - bold, aliniere etc BRUTE FORCE OBIECT PENTRU CU TOATE PROPRIETATILE
										XWPFRun run = newPara.createRun();
										// INCA NU FUNCTIONEAZA FOARTE BINE COPIEREA DE STILURI - de ex spatiul intre paragrafe;
										//copyParaRunStyle(document.getParagraphs().get(paraDocxCounter).getRuns().get(0),run);
										//copyParaStyle(document.getParagraphs().get(paraDocxCounter),newPara); MAI ALES ASTA - FOARTE RAU
										run.setText(paraValue);
										document.setParagraph(newPara, paraDocxCounter);
										paraCounter++;
									}
									paraDocxCounter++;
								}
					        	
					            	
					            document.write(fileOut);
					            document.close();
					            iniDocument.close();
					            auxDocument.close();
					            System.out.println("Scriere cu succes!");
								Alert a = AlertDialogFactory.createAlertInformation("Informare", "Fisierul a fost creat cu succes!");
								a.show();
								
					        }catch (IOException e) {
					        	newFile.delete();
								Alert a = AlertDialogFactory.createAlertInformation("Eroare", "Create fisierului a esuat");
								a.show();
								e.printStackTrace();
							}
					        
						}
						
						else { // anterior nu am importat DOCX, deci cream un nou DOCX in care salvam doar paragrafele
							File newFile = new File(path);	
							 try(OutputStream fileOut = new FileOutputStream(newFile)) {  
						        	XWPFDocument document = new XWPFDocument(); 
						        	for(String i:page.getParaList()) {
						        		XWPFParagraph newPara = document.createParagraph();
						        		newPara.createRun().setText(i);
						        	}
						        	   	
						            document.write(fileOut);
						            document.close();
						            System.out.println("Scriere cu succes!");
									Alert a = AlertDialogFactory.createAlertInformation("Informare", "Fisierul a fost creat cu succes!");
									a.show();
									
						        }catch (IOException e) {
						        	newFile.delete();
									Alert a = AlertDialogFactory.createAlertInformation("Eroare", "Create fisierului a esuat");
									a.show();
									e.printStackTrace();
								}
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
	
	/**
	 *<p>Face import la un fisier excel
	 *<p>Se foloseste de metoda readXLSLFile
	 *<p>Inainte de import se salveaza (actualizeaza) textul din inputZone(actualizare in paraList)
	 *<p>Se verifica daca aplicatia contine text - in caz afirmativ utilizatorul trebuie sa confirme ca vrea sa importeze (deoarece textul actual se pierde)
	 * @param mainStage
	 */
	public final void importXLSXFile(Stage mainStage) {
		page.setParaListElem(page.getParaIndex(), page.getInputZoneText());
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selectare fisier");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel file (.xlsx)", "*.xlsx*"));

		String text = getAllText();
		if (text.trim().replaceAll("\\s+", "").length() == 0) { // in cazul in care sunt doar spatii atunci se importa direct
			File file = fileChooser.showOpenDialog(mainStage);
			readXLSXFile(file);
		}

		else { // in cazul in care exista deja text in aplicatie va trebui sa confirmi ca vrei importul

			Alert alert = AlertDialogFactory.createAlertConfirmation(AlertType.WARNING, "Da", "Nu",
					"Importare fisier .xlsx", "Este sigur ca vrei sa importi un alt text?\nTexul actual va fi inlocuit!",
					false, false);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.YES) {
				System.out.println("OK");
				File file = fileChooser.showOpenDialog(mainStage);
				readXLSXFile(file);
			}
		}
	}
	
	/**
	 *<p>Citeste text dintr-un fisier excel
	 *<p>Este o metoda auxiliara folosita de importXLSXFile pentru a face economie de cod
	 *<p>In importXLSXFile aceasta metoda se apeleaza de doua ori
	 *<p>Se citeste textul din prima foaie de lucru a fisierului excel
	 *<p>Fisierul citit AR TREBUI sa respecta urmatoarea structura: - sa contine doar celule de tip text
	 *- sa fie continut (text) doar in celulele cu indexul de forma (i,0) unde i porneste de la 0
	 *(adica doar prima celula din fiecare linie)
	 *(desi citirea se face atat pe linii cat si pe coloane, ar fi recomandat ca fisierul de intrare sa respecta formatul descris mai sus) 
	 *<p>Se verifica daca a fost selectat un fisier cu extensie corespunzatoare (.xlsx)
	 *<p>Fiecare paragraf din aplicatie va corespunde unei linii (in ordine corecta)
	 *<p>Daca se reuseste citirea fisierului excel (dimensiunea e ok, extensia e ok etc) atunci openDocxOpen() se pune pe false deoarece cel mai recent fisier importat va fi excel, nu docx
	 * @param file
	 */
	public void readXLSXFile(File file) {
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
									
					 //Create Workbook instance holding reference to .xlsx file
		            XSSFWorkbook workbook = new XSSFWorkbook(fis);
		 
		            //Get first/desired sheet from the workbook
		            XSSFSheet sheet = workbook.getSheetAt(0);
		 
		            page.initializeParaList();
		                 
		            //se foloseste deoarece in paraList() avem deja un element text
		            // prima linie din fisier vine pe pozitia 0 (elementul deja creat) iar restul liniilor vin in continuare
		            boolean fillFirstPara = false; 
		            
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
		                    	if(fillFirstPara == false) {
		                    		page.setParaListElem(0, cell.getStringCellValue());
		                    		fillFirstPara = true;
		                    	}
		                    	else
		                    		page.getParaList().add(cell.getStringCellValue());
		                    } 
		                    else { // celula nu are tipul corespunzator
		                    	workbook.close();
		                    	System.out.println("CELULA NU ARE TIPUL CORESPUNZATOR");
		                    	Alert a = AlertDialogFactory.createAlertInformation("Eroare", "Eroare la citirea textului din celule!");
		    					a.show();
		                    	throw new IOException();
		                    	
		                    }
		                    
		                }
		               
		            }
		            
		            page.setDocxOpen(false); // nu importam fisier DOCX, retine asta
		            if(page.getParaList().size() > 1) // daca avem mai mult de 1 linie activeaza butoanele
						page.enableBottomButtons();
					
					page.setInputZoneText(page.getParaListElem(page.getParaIndex())); // setare text (paraList poz 0 in inputZone)
					page.setParaInfo();
					
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

	/**
	 *<p>Exporteaza textul din aplicatie ca fisier excel (.xlsx)
	 *<p>Creaza un fisier Excel care contine doar o foaie de lucru, in aceasta foaie pune textul din aplicatie
	 *<p>Cate un paragraf pe o linie - linia 1, coloana 0
	 * @param mainStage
	 */
	public void exportXLSXFile(Stage mainStage) {
		page.setParaListElem(page.getParaIndex(),page.getInputZoneText()); // salveaza modificarile facute pe text in paragraful curent
		String fileContent = getAllText();

		if (fileContent.trim().replace("\\s+", "").length() == 0) { // sunt doar spatii libere in fileContent si nu merita sa salvezi
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
					if (fileName.endsWith(".xlsx") == false) // daca nu a pus el .xlsx la final de nume fisier ii pun eu
						fileName = fileName + ".xlsx";

					DirectoryChooser directoryChooser = new DirectoryChooser();

					File selectedDirectory = directoryChooser.showDialog(mainStage);
					if (selectedDirectory != null) {

						File[] filesList = selectedDirectory.listFiles(); // toate fisierele din director, verificam sa nu existe deja un fisier cu numele ales
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
				        try (FileOutputStream fileOut = new FileOutputStream(newFile)) {
				        	XSSFWorkbook workbook = new XSSFWorkbook();
					        XSSFSheet sheet = workbook.createSheet("Sheet1");
					        for(int i = 0;i < page.getParaList().size();i++) {
					        	 Row row = sheet.createRow(i);
					        	 Cell cell = row.createCell(0);
					        	 cell.setAsActiveCell();
					        	 //cell.setCellType(CellType.STRING); decrapted
					        	 cell.setCellValue(page.getParaListElem(i));
					        	 
					        }
					        
				            workbook.write(fileOut);
				            workbook.cloneSheet(0);
				            workbook.close();
				            System.out.println("Scriere cu succes!");
							Alert a = AlertDialogFactory.createAlertInformation("Informare", "Fisierul a fost creat cu succes!");
							a.show();
				        } catch (IOException e) {
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
	
	/**
	 * <p>Copiere din inputZone pe clipboard
	 * <p>Momentan copiaza ca PlainText
	 */
	public void copyToClipboard() {
		final Clipboard clipboard = Clipboard.getSystemClipboard();
	    final ClipboardContent content = new ClipboardContent();
	    content.putString(page.getInputZoneText());
	    
	    clipboard.setContent(content);
	}
	
	/**
	 * <p>Aducere text din clipboard in inputZone si actualizare in paraList
	 * <p>In cazul in care exista deja text in paragraful curent utilizatorul va trebuie sa confirme ca vrea sa pasteze noul text
	 */
	public void pasteFromClipboard() {
		String content = Clipboard.getSystemClipboard().getString();
		if(page.getInputZoneText().trim().replaceAll("\\s+", "").length() == 0) { // confirmare utilizator daca exista deja text in paragraful respectiv
			page.setParaListElem(page.getParaIndex(), content);
			page.setInputZoneText(content);
		}
		else {
			Alert alert = AlertDialogFactory.createAlertConfirmation(AlertType.CONFIRMATION, "Da", "Nu",
					"Lipire text", "Este sigur ca vrei sa lipesti noul textul?", false, false);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.YES) { // actiunea care are loc cand apesi butonul "Da" din mesaj(alert)
				System.out.println("OK");
				page.setParaListElem(page.getParaIndex(), content);
				page.setInputZoneText(content);
			}
		}
	}

	// Corecteaza tot textul din paragraful curent, pastreaza separatorii
	public void correctParagraph() {
		String text = page.getInputZoneText();
		String newText = page.splitAlgs.textCorrection(text, page.getDict());
		page.setInputZoneText(newText);
	}
	
	// Corecteaza textul din toate paragrafele, pastreaza separatorii
	public void correctAllParagraphs() {
		
		if(page.getParaList().size() == 1) {
			correctParagraph();
			return;
		}
		
		int n = page.getParaList().size();
		int currentParaIndex = page.getParaIndex();
		for(int i = 0;i<n;i++) {
			if(page.getParaIndex() == i)
				page.setParaListElem(i, page.getInputZoneText());
			String text = page.getParaListElem(i);
			String newText = page.splitAlgs.textCorrection(text, page.getDict());
			page.setParaListElem(i, newText);
		}
		page.setInputZoneText(page.getParaListElem(currentParaIndex));
	}
	
	/**
	 * <p> Verifica daca un cuvant dat de utilizator (input) este in dictionar
	 * Se va verifica daca input-ul utilizatorului este unul valid (sa nu contina spatii sau alti separatori)
	 */
	public void checkWord() {
		TextInputDialog inputfileName = AlertDialogFactory.createInputDialog("Verificare cuvânt", "Preluare input",
				"Cuvânt: ");
		Optional<String> result = inputfileName.showAndWait();
		String rez;
		try { // asta deoarece poate arunca exceptie in cazul in care se da X la dialog sau se apasa Iesire
			rez = result.get();
		}catch(NoSuchElementException e) {
			return;
		}
		
		if(rez.length() > 0 && page.splitAlgs.isNormalWord(rez) == true) { // input-ul este bun
			Alert a = null;
			if(page.getDict().containsKey(rez) && page.getDict().get(rez) == null) 
				a = AlertDialogFactory.createSimpleAlertInformation("Verificare cuvânt", "Cuvântul introdus se află în dicționar dar este doar ignorat");
			else if(page.getDict().containsKey(rez) && page.getDict().get(rez) != null)
				a = AlertDialogFactory.createSimpleAlertInformation("Verificare cuvânt", "Cuvântul introdus se află în dicționar");
			else
				a = AlertDialogFactory.createSimpleAlertInformation("Verificare cuvânt", "Cuvântul introdus nu se află în dicționar");
			a.show();
		}
		else { // input-ul nu este bun
			Alert a = AlertDialogFactory.createSimpleAlertInformation("Eroare", "Nu ai introdus un cuvânt!");
			a.show();
		}
	
	}
	
	/**
	 * <p>Ignora un cuvant dat de utlilzator - il scoate din TreeMap dict dar doar local
	 * Cand deschizi aplicatia il vei avea din nou
	 * Practic, se poate ignora direct de aia, nu trebuie neaparat sa gasesti cuvantul intr-un anumit text si sa il ignori abia atunci
	 */
	public void ignore() {
		TextInputDialog inputfileName = AlertDialogFactory.createInputDialog("Ignorare cuvânt", "Preluare input",
				"Cuvânt: ");
		Optional<String> result = inputfileName.showAndWait();
		String rez;
		try { // asta deoarece poate arunca exceptie in cazul in care se da X la dialog sau se apasa Iesire
			rez = result.get();
		}catch(NoSuchElementException e) {
			return;
		}
		
		if(rez.length() > 0 && page.splitAlgs.isNormalWord(rez) == true) { // input-ul este bun
			Alert a = null;
			if(page.getDict().containsKey(rez) && page.getDict().get(rez) == null) //contine cuvantul, deci oricum nu il marcheaza gresit
				a = AlertDialogFactory.createSimpleAlertInformation("Ignorare cuvânt", "Cuvântul introdus este deja ignorat");
			else if(page.getDict().containsKey(rez) && page.getDict().get(rez) != null)
				a = AlertDialogFactory.createSimpleAlertInformation("Ignorare cuvânt", "Cuvântul introdus este deja in dictionar");
			else {
				page.getDict().put(rez, null);
				page.spellText(page.getInputZoneText()); // verifica din nou textul curent, cuvantul introdus va fi ignorat acum (deci va avea alt stil)
				a = AlertDialogFactory.createSimpleAlertInformation("Ignorare cuvânt", "Cuvântul introdus va fi ignorat pe viitor");
			}
			a.show();
		}
		else { // input-ul nu este bun
			Alert a = AlertDialogFactory.createSimpleAlertInformation("Eroare", "Nu ai introdus un cuvânt!");
			a.show();
		}
		
	}
	
	// Anulare ignorare
	public void unIgnore() {
		TextInputDialog inputfileName = AlertDialogFactory.createInputDialog("Eliminare ignorare", "Preluare input",
				"Cuvânt: ");
		Optional<String> result = inputfileName.showAndWait();
		String rez;
		try { // asta deoarece poate arunca exceptie in cazul in care se da X la dialog sau se apasa Iesire
			rez = result.get();
		}catch(NoSuchElementException e) {
			return;
		}
		
		if(rez.length() > 0 && page.splitAlgs.isNormalWord(rez) == true) { // input-ul este bun
			Alert a = null;
			if(page.getDict().containsKey(rez) && page.getDict().get(rez) == null) { // contine cuvantul, deci il putem scoate din dict, pe viitor nu o sa-l mai ignore
				page.getDict().remove(rez);
				page.spellText(page.getInputZoneText());
				a = AlertDialogFactory.createSimpleAlertInformation("Eliminare ignorare", "Cuvântul introdus nu va mai fi ignorat");
			}
			else if(page.getDict().containsKey(rez) && page.getDict().get(rez) != null)
				a = AlertDialogFactory.createSimpleAlertInformation("Eliminare ignorare", "Cuvântul introdus se află în dicționar, nu îl poți modifica");
			else 
				a = AlertDialogFactory.createSimpleAlertInformation("Eliminare ignorare", "Cuvântul introdus nu este încă ignorat");
			
			a.show();
		}
		else { // input-ul nu este bun
			Alert a = AlertDialogFactory.createSimpleAlertInformation("Eroare", "Nu ai introdus un cuvânt!");
			a.show();
		}
	}
	
	// Adauga un anumit cuvant in dictionar - oricand, nu trebuie sa astepti pana gasesti cuvantul in CodeArea
	public void addToDict() {
		TextInputDialog inputfileName = AlertDialogFactory.createInputDialog("Adaugare in dictionar", "Preluare input",
				"Cuvânt: ");
		Optional<String> result = inputfileName.showAndWait();
		String rez;
		try { // asta deoarece poate arunca exceptie in cazul in care se da X la dialog sau se apasa Iesire
			rez = result.get();
		}catch(NoSuchElementException e) {
			return;
		}
		
		if(rez.length() > 0 && page.splitAlgs.isNormalWord(rez) == true) { // input-ul este bun
			Alert a = null;
			if(page.getDict().containsKey(rez) && page.getDict().get(rez) != null) // este deja ADAUGAT in dictionar
				a = AlertDialogFactory.createSimpleAlertInformation("Adăugare în dicționar", "Cuvântul introdus este deja în dicționar");
			else { // daca nu este in dictionar, sau daca este doar ignorat
				WordObj cuvNou = new WordObj(rez);
				page.getDict().put(rez, cuvNou);
				DBObj.adaugare(rez);
				page.spellText(page.getInputZoneText()); // verifica din nou textul curent
				a = AlertDialogFactory.createSimpleAlertInformation("Adăugare în dicționar", "Cuvântul a fost adăugat în dicționar");
			}
			a.show();
		}
		else { // input-ul nu este bun
			Alert a = AlertDialogFactory.createSimpleAlertInformation("Eroare", "Nu ai introdus un cuvânt!");
			a.show();
		}
	}
	
	
	//Scoate un cuvant din dictionar - de ex am adaugat din greseala
	public void removeFromDict() {
		TextInputDialog inputfileName = AlertDialogFactory.createInputDialog("Eliminare cuvânt", "Preluare input",
				"Cuvânt: ");
		Optional<String> result = inputfileName.showAndWait();
		String rez;
		try { // asta deoarece poate arunca exceptie in cazul in care se da X la dialog sau se apasa Iesire
			rez = result.get();
		}catch(NoSuchElementException e) {
			return;
		}
		
		if(rez.length() > 0 && page.splitAlgs.isNormalWord(rez) == true) { // input-ul este bun
			Alert a = null;
			if(page.getDict().containsKey(rez) == false)
				a = AlertDialogFactory.createSimpleAlertInformation("Eliminare cuvânt", "Cuvântul introdus nu este în dicționar");
			
			else if(page.getDict().containsKey(rez) && page.getDict().get(rez) == null) // este in dictionar, dar este doar ignorat
				a = AlertDialogFactory.createSimpleAlertInformation("Eliminare cuvânt", "Cuvântul introdus este doar ignorat, nu se afla in dictionar");
			
			else { // este in dict (adaugat) deci se scoate si se actualizeaza starea textului
				DBObj.stergereCuvant(page.getDict().get(rez).getId());
				page.getDict().remove(rez);
				page.spellText(page.getInputZoneText()); // verifica din nou textul curent
				a = AlertDialogFactory.createSimpleAlertInformation("Eliminare cuvânt", "Cuvântul a fost eliminat din dicționar");
			}
			
			a.show();
		}
		else { // input-ul nu este bun
			Alert a = AlertDialogFactory.createSimpleAlertInformation("Eroare", "Nu ai introdus un cuvânt!");
			a.show();
		}
	}
	
	
	/**
	 * <p>Pentru a copia stilul (bold, aliniere, italic etc) de la un Run din paragraful original in Run de la un paragraf nou creat
	 * <p>Stiu nu se poate copie dintr-o bucata (getStyle()), prin urmare trebuie luata pe rand fiecare proprietatie :( BRUTE FORCE
	 * @param original
	 * @param nou
	 */
	public void copyParaRunStyle(XWPFRun original, XWPFRun nou) {
		nou.setBold(original.isBold());
		nou.setCapitalized(original.isCapitalized());
		nou.setCharacterSpacing(original.getCharacterSpacing());
		if(original.getColor() != null)
			nou.setColor(original.getColor());
		nou.setDoubleStrikethrough(original.isDoubleStrikeThrough());
		nou.setEmbossed(original.isEmbossed());
		if(original.getEmphasisMark() != null)
			nou.setEmphasisMark(original.getEmphasisMark().toString());
		if(original.getFontFamily() != null)
			nou.setFontFamily(original.getFontFamily());
		nou.setFontSize(original.getFontSize());
		nou.setImprinted(original.isImprinted());
		nou.setItalic(original.isItalic());
		nou.setKerning(original.getKerning());
		if(original.getLang() != null)
			nou.setLang(original.getLang());
		nou.setShadow(original.isShadowed());
		nou.setSmallCaps(original.isSmallCaps());
		//nou.setStrike(original.isStrike()); //deprecated
		nou.setStrikeThrough(original.isStrikeThrough());
		//if(original.getSubscript() != null)
			//nou.setSubscript(original.getSubscript()); // deprecated
		if(original.getTextHightlightColor() != null)
			nou.setTextHighlightColor(original.getTextHightlightColor().toString());
		nou.setTextPosition(original.getTextPosition());
		nou.setTextScale(original.getTextScale());
		if(original.getUnderline() != null)
			nou.setUnderline(original.getUnderline());
		if(original.getUnderlineColor() != null)
			nou.setUnderlineColor(original.getUnderlineColor());
		if(original.getUnderlineThemeColor() != null)
			nou.setUnderlineThemeColor(original.getUnderlineThemeColor().toString());
		nou.setVanish(original.isVanish());
		if(original.getVerticalAlignment() != null)
			nou.setVerticalAlignment(original.getVerticalAlignment().toString());
	}
	
	/**
	 * <p>Asemanator cu copyParaRunStyle, dar aici se copieaza proprietatile care tin de paragraf si nu de Run
	 * @param original
	 * @param nou
	 */
	public void copyParaStyle(XWPFParagraph original, XWPFParagraph nou) {
		if(original.getAlignment() != null)
			nou.setAlignment(original.getAlignment());
		if(original.getBorderBetween() != null)
			nou.setBorderBetween(original.getBorderBetween());
		if(original.getBorderBottom() != null)
			nou.setBorderBottom(original.getBorderBottom());
		if(original.getBorderLeft() != null)
			nou.setBorderLeft(original.getBorderLeft());
		if(original.getBorderRight() != null)
			nou.setBorderRight(original.getBorderRight());
		if(original.getBorderTop() != null)
			nou.setBorderTop(original.getBorderTop());
		nou.setFirstLineIndent(original.getFirstLineIndent());
		nou.setFontAlignment(original.getFontAlignment());
		nou.setIndentationFirstLine(original.getIndentationFirstLine());
		nou.setIndentationHanging(original.getIndentationHanging());
		nou.setIndentationLeft(original.getIndentationLeft());
		nou.setIndentFromRight(original.getIndentationRight());
		nou.setIndentFromLeft(original.getIndentFromLeft());
		nou.setIndentFromRight(original.getIndentFromRight());
		//nou.setNumID(original.getNumID());
		nou.setPageBreak(original.isPageBreak());
		nou.setSpacingAfter(original.getSpacingAfter());
		nou.setSpacingAfterLines(original.getSpacingAfterLines());
		nou.setSpacingBefore(original.getSpacingBefore());
		nou.setSpacingBeforeLines(original.getSpacingBeforeLines());
		nou.setSpacingBetween(original.getSpacingBetween());
		if(original.getSpacingLineRule() != null) {
			nou.setSpacingBetween(original.getSpacingBetween(), original.getSpacingLineRule());
			nou.setSpacingLineRule(original.getSpacingLineRule());
		}
		if(original.getVerticalAlignment() != null)
			nou.setVerticalAlignment(original.getVerticalAlignment());
		//nou.setWordWrap(original.isWordWrap()); deprecated
		//nou.setWordWrapped(original.isWordWrapped());
		if(original.getStyle() != null)
			nou.setStyle(original.getStyle());
	}
}
