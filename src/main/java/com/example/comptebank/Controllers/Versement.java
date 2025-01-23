package com.example.comptebank.Controllers;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Versement {
	public static void effectuerVersement(TextField numCompteField, TextField montantField, Text nbOperation, Text nbCompte) {
		String numCompte = numCompteField.getText().trim();
		String montantStr = montantField.getText().trim();

		if (numCompte.isEmpty() || montantStr.isEmpty()) {
			Actions.afficherMessageErreur("Veuillez remplir tous les champs.");
			return;
		}

		try {
			int code = Integer.parseInt(numCompte);
			double montant = Double.parseDouble(montantStr);

			// Mise à jour de la base de données
			String query = "UPDATE compte SET solde = solde + ? WHERE code = ?";
			int rowsUpdated = Actions.executerMiseAJour(query, montant, code);

			if (rowsUpdated > 0) {
				// Enregistrer la transaction
				//Actions.enregistrerTransaction(code, code, montant, "Versement");
				Actions.enregistrerTransaction(code,montant,"Versement");
				Actions.afficherMessageSucces("Versement effectué avec succès !");
				Actions.mettreAJourLesValeurs(nbOperation,nbCompte);
				// Réinitialiser les champs
				resetFields(numCompteField,montantField);
			} else {
				Actions.afficherMessageErreur("Compte non trouvé.");
			}
		} catch (NumberFormatException e) {
			Actions.afficherMessageErreur("Numéro de compte ou montant invalide.");
		}
	}

	public static void annulerVersement(TextField numCompte,TextField montant ) {
		// Réinitialiser les champs et masquer la boîte de dialogue si nécessaire
		resetFields(numCompte,montant);
	}

	private static void resetFields(TextField numCompteField, TextField montantField) {
		numCompteField.clear();
		montantField.clear();
	}
}
