package com.example.comptebank.Controllers;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class Virement {
    public static void effectuerVirement(TextField numCompteSourceField, TextField numCompteDestField, TextField montantField, Text nbOperation, Text nbCompte) {
        String numCompteSource = numCompteSourceField.getText().trim();
        String numCompteDestinataire = numCompteDestField.getText().trim();
        String montantStr = montantField.getText().trim();

        if (numCompteSource.isEmpty() || numCompteDestinataire.isEmpty() || montantStr.isEmpty()) {
            Actions.afficherMessageErreur("Veuillez remplir tous les champs.");
            return;
        }

        try {
            int codeSource = Integer.parseInt(numCompteSource);
            int codeDest = Integer.parseInt(numCompteDestinataire);
            double montant = Double.parseDouble(montantStr);

            double soldeSource = Actions.recupererSolde(codeSource);
            if (soldeSource == -1) {
                Actions.afficherMessageErreur("Compte source non trouvé.");
                return;
            }

            if (soldeSource >= montant) {
                // Débiter le compte source
                String queryDebiter = "UPDATE compte SET solde = solde - ? WHERE code = ?";
                int rowsUpdatedSource = Actions.executerMiseAJour(queryDebiter, montant, codeSource);

                if (rowsUpdatedSource > 0) {
                    // Créditer le compte destinataire
                    String queryCrediter = "UPDATE compte SET solde = solde + ? WHERE code = ?";
                    int rowsUpdatedDest = Actions.executerMiseAJour(queryCrediter, montant, codeDest);

                    if (rowsUpdatedDest > 0) {
                        // Enregistrer la transaction
                        Actions.enregistrerTransaction(codeSource, montant, "Retrait");
                        Actions.enregistrerTransaction(codeDest,montant,"Versement");
                        Actions.afficherMessageSucces("Virement effectué avec succès !");
                        Actions.mettreAJourLesValeurs(nbOperation,nbCompte);
                        resetFields(numCompteSourceField, numCompteDestField, montantField);
                    } else {
                        Actions.afficherMessageErreur("Compte destinataire non trouvé.");
                    }
                }
            } else {
                Actions.afficherMessageErreur("Solde insuffisant pour le virement !");
            }
        } catch (NumberFormatException e) {
            Actions.afficherMessageErreur("Numéros de comptes ou montant invalides.");
        }
    }

    public static void annulerVirement(TextField numCompteSourceField, TextField numCompteDestField, TextField montantField) {
        resetFields(numCompteSourceField, numCompteDestField, montantField);
    }

    private static void resetFields(TextField numCompteSourceField, TextField numCompteDestField, TextField montantField) {
        numCompteSourceField.clear();
        numCompteDestField.clear();
        montantField.clear();
    }
}
