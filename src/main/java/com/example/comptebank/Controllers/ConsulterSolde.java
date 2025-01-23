//package com.example.comptebank.Controllers;
//
//import application.gestionDeCompte.FormAction;
//
//public class ConsulterSolde {
//	// Définir l'action de consultation de solde comme une instance de FormAction
//    public static FormAction consulterSolde = (numCompte, unused) -> {
//        if (numCompte == null || numCompte.isEmpty()) {
//            Actions.afficherMessageErreur("Veuillez entrer un numéro de compte.");
//            return;
//        }
//
//        try {
//            int code = Integer.parseInt(numCompte);  // Conversion du numéro de compte en entier
//            double solde = Actions.recupererSolde(code);      // Récupération du solde à partir de la base de données
//
//            if (solde == -1) {
//                Actions.afficherMessageErreur("Compte non trouvé.");
//            } else {
//                Actions.afficherMessageSucces("Solde du compte : " + solde + " USD");
//            }
//        } catch (NumberFormatException e) {
//            Actions.afficherMessageErreur("Numéro de compte invalide.");
//        }
//    };
//}
