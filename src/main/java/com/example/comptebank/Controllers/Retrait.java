package com.example.comptebank.Controllers;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class Retrait {
        public static void effectuerRetrait(TextField numCompteField, TextField montantField, Text nbOperation, Text nbCompte) {
            String numCompte = numCompteField.getText().trim();
            String montantStr = montantField.getText().trim();

            if (numCompte.isEmpty() || montantStr.isEmpty()) {
                Actions.afficherMessageErreur("Veuillez remplir tous les champs.");
                return;
            }

            try {
                int code = Integer.parseInt(numCompte);
                double montant = Double.parseDouble(montantStr);

                if (montant <= 0) {
                    Actions.afficherMessageErreur("Le montant doit être supérieur à zéro.");
                    return;
                }

                // Vérifier si le compte existe et obtenir le solde actuel
                double soldeActuel = Actions.recupererSolde(code);

                if (soldeActuel == -1) { // -1 indique que le compte n'existe pas
                    Actions.afficherMessageErreur("Compte non trouvé.");
                    return;
                }

                if (montant > soldeActuel) {
                    Actions.afficherMessageErreur("Fonds insuffisants pour effectuer le retrait.");
                    return;
                }

                // Mise à jour de la base de données
                String queryUpdate = "UPDATE compte SET solde = solde - ? WHERE code = ?";
                int rowsUpdated = Actions.executerMiseAJour(queryUpdate, montant, code);

                if (rowsUpdated > 0) {
                    // Enregistrer la transaction
                    Actions.enregistrerTransaction(code, montant, "Retrait");
                    Actions.afficherMessageSucces("Retrait effectué avec succès !");
                    Actions.mettreAJourLesValeurs(nbOperation,nbCompte);
                    // Réinitialiser les champs
                    resetFields(numCompteField, montantField);
                } else {
                    Actions.afficherMessageErreur("Erreur lors de l'exécution du retrait.");
                }
            } catch (NumberFormatException e) {
                Actions.afficherMessageErreur("Numéro de compte ou montant invalide.");
            }
        }

        public static void annulerRetrait(TextField numCompte, TextField montant) {
            // Réinitialiser les champs et masquer la boîte de dialogue si nécessaire
            resetFields(numCompte, montant);
        }

        private static void resetFields(TextField numCompteField, TextField montantField) {
            numCompteField.clear();
            montantField.clear();
        }
}
