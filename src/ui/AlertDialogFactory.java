package ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.scene.control.Alert.AlertType;

// aceasta clasa va crea obiecte de tip Alert - acele ferestre in care trebuie sa confirmi ("Da" / "Nu") inainte de o actiune
// OBS: daca se foloseste optiunea acceptButton.setDefaultButton(false); atunci nu vei mai putea "apasa" butonul folosind ENTER
// totusi daca se pune optiunea respectiva pe true atunci se va schimba putin aspectul butonului, va avea si o nuanta de albastru peste

/**
 * 
 * @author Dacian
 * <p> AlertDialogFactory
 * Această clasă conține metode care generează obiect de tip Alert și InputTextDialog.
 * Practic, când utilizatorul interacționează cu aplicația aceasta afișează diverse mesaje (Alert) pentru a comunica cu utilizatorul.
 * De ex. dacă utilizatorul dorește să încarce text dintr-un fișier pentru care nu are opțiunea de citire permisă atunci 
 * aplicația îl va informa că nu poate citi acel fișier. 
 * Informarea utilizatorul se va realiza prin obiecte de tip Alert (din JavaFx), astfel se folosește această clasă pentru a crea
 * obiecte Alert cu proprietăți custom - de exemplu titlul mesajului, conținutul mesajului
 * 
 */



public class AlertDialogFactory {

	/**
	 * Metoda care creaza un alert de tip confirmation, acest tip de alert pune o
	 * intrebare si pune la dispozitie doua optiuni de raspuns
	 * 
	 * @param type               - tipul alertei - eroare, confirmare, etc
	 * @param acceptButtonValue  - valoarea butonului pentru accept, ex: "Da"
	 * @param declineButtonValue - valoarea butonului pentru refuz, ex: "Nu"
	 * @param alertTitle         - Titlul de la alert, ex: "Golire zona text"
	 * @param alertContent       - Continutul intrebarii, ex: "Esti sigur ca vrei sa
	 *                           stergi textul?"
	 * @param hideIcon           - daca vrei sa ascunzi icon-ul mesajului - de ex
	 *                           pentru informatii se afiseaza un 'i' care nu este
	 *                           neaparat necesar in schimb pt error se afiseaza
	 *                           ceva rosu care este bine ca iti "sare in ochi"
	 * @param hideHeader         - daca vrei sa ascunzi textul din header-ul alertei
	 *                           - de obice nu adauga informatii suplimentare dar
	 *                           daca ca icon-ul sa se vada mai bine
	 * @return OBS: vezi ca mereu va avea 2 butoane, deci daca vrei doar o
	 *         confirmare simpla (doar un "OK") foloseste alta metoda
	 */

	public static Alert createAlertConfirmation(AlertType type, String acceptButtonValue, String declineButtonValue,
			String alertTitle, String alertContent, boolean hideIcon, boolean hideHeader) {

		Alert alert = new Alert(type);
		alert.initModality(Modality.APPLICATION_MODAL);

		alert.setTitle(alertTitle);
		if (hideHeader)
			alert.setHeaderText(null);
		else
			alert.setHeaderText("CONFIRMARE"); // ASTA DACA VREM SA NU MAI AFISAM HEADER (CARE NU PREA PUNE INFORMATII
												// SUPLIMENTARE)
		if (hideIcon)
			alert.setGraphic(null); // ASTA ASCUNDE UN "i"
		alert.setContentText(alertContent);

		// Personalizare aspect
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setStyle("-fx-font-size: 15px;");

		// adaugare butoane
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

		// Configurare button pt accept - "Da"
		Button acceptButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
		acceptButton.setText(acceptButtonValue);
		acceptButton.setDefaultButton(false);
		acceptButton.setFocusTraversable(false);

		// Configurare button pt refuz - "Nu"
		Button declineButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
		declineButton.setText(declineButtonValue);
		declineButton.setDefaultButton(false);
		declineButton.setFocusTraversable(false);

		return alert;
	}

	/**
	 * 
	 * @param alertTitle   - titlul de la alerta
	 * @param alertContent - continutul alertei (efectiv despre ce informeaza)
	 * @return
	 */
	public static Alert createAlertInformation(String alertTitle, String alertContent) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initModality(Modality.APPLICATION_MODAL);

		alert.setTitle(alertTitle);
		alert.setHeaderText("Mesaj informativ");
		alert.setContentText(alertContent);

		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.YES);

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setStyle("-fx-font-size: 15px;");

		Button acceptButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
		acceptButton.setText("OK");
		acceptButton.setDefaultButton(false);
		acceptButton.setFocusTraversable(false);

		return alert;

	}

	public static TextInputDialog createInputDialog(String dialogTitle, String dialogHeader, String dialogContent) {
			
		TextInputDialog dialog = new TextInputDialog();
		dialog.initModality(Modality.APPLICATION_MODAL);

		dialog.setTitle(dialogTitle);
		dialog.setHeaderText(dialogHeader);
		dialog.setContentText(dialogContent);


		Button okButton = (Button)dialog.getDialogPane().lookupButton(ButtonType.OK);
		okButton.setText("Continua");
		//okButton.setDefaultButton(false);
		okButton.setFocusTraversable(false);
		
		Button cancelButton = (Button)dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
		cancelButton.setText("Iesire");
		cancelButton.setDefaultButton(false);
		cancelButton.setFocusTraversable(false);
		return dialog;
	}
}
