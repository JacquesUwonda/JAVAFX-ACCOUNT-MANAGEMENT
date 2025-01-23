package com.example.comptebank.Controllers;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class Solde {

    private static final double TAUX_INTERET = 0.16; // Taux d'intérêt de 5%

    public static void afficherSolde(TextField numCompteField, Label soldeAvantText, Label soldeApresText) {
        String numCompteStr = numCompteField.getText().trim();

        if (numCompteStr.isEmpty()) {
            Actions.afficherMessageErreur("Veuillez entrer un numéro de compte.");
            return;
        }

        try {
            int numCompte = Integer.parseInt(numCompteStr);

            // Requête pour récupérer le solde avant
            double soldeAvant = Actions.recupererSolde(numCompte);

            if (soldeAvant >= 0) {
                // Calcul du solde après mise à jour
                double soldeApres = soldeAvant + (soldeAvant * TAUX_INTERET);

                // Mise à jour des champs de texte
                soldeAvantText.setText(String.format("%.2f", soldeAvant));
                soldeApresText.setText(String.format("%.2f", soldeApres));

                // Requête pour mettre à jour le solde dans la base de données
                String query = "UPDATE compte SET solde = ? WHERE code = ?";
                int rowsUpdated = Actions.executerMiseAJour(query, soldeApres, numCompte);
            } else {
                Actions.afficherMessageErreur("Compte non trouvé.");
            }
        } catch (NumberFormatException e) {
            Actions.afficherMessageErreur("Numéro de compte invalide.");
        }
    }
}
