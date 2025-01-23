package com.example.comptebank.Controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Actions {

//	public static FormAction consulterSolde;

    public static void afficherMessageErreur(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void afficherMessageSucces(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public static double recupererSolde(int code) {
        String query = "SELECT solde FROM compte WHERE code = ?";
        try (PreparedStatement stmt = MainController.connection.prepareStatement(query)) {
            stmt.setInt(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("solde");
            }
        } catch (SQLException e) {
            Actions.afficherMessageErreur("Erreur lors de la récupération du solde : " + e.getMessage());
        }
        return -1;
    }



    public static ObservableList<Operation> recupererOperations() {
        ObservableList<Operation> operations = FXCollections.observableArrayList();
        String query = "SELECT id,code_compte, montant, type_operation, proprietaire, date_operation FROM operation";
        try (Statement stmt = MainController.connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int code=rs.getInt("code_compte");
                double montant = rs.getDouble("montant");
                String typeOperation = rs.getString("type_operation");
                String proprietaire = rs.getString("proprietaire");
                Date dateOperation = rs.getDate("date_operation");

                // Ajouter l'opération à la liste
                operations.add(new Operation(id, code, montant, typeOperation, proprietaire, dateOperation));
            }
        } catch (SQLException e) {
            Actions.afficherMessageErreur("Erreur lors de la récupération des opérations : " + e.getMessage());
        }
        return operations;
    }



//    public static void enregistrerTransaction(int codeSource, int codeDest, double montant, String typeOperation) {
//        String query = "INSERT INTO operation (code_compte_source, code_compte_destinataire, montant, type_operation) VALUES (?, ?, ?, ?)";
//        try (PreparedStatement stmt = MainController.connection.prepareStatement(query)) {
//            stmt.setInt(1, codeSource);
//            stmt.setInt(2, codeDest);
//            stmt.setDouble(3, montant);
//            stmt.setString(4, typeOperation);
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            afficherMessageErreur("Erreur lors de l'enregistrement de la transaction : " + e.getMessage());
//        }
//    }


    // Méthodes utilitaires
    public static void enregistrerTransaction(int codeCompte, double montant, String typeOperation) {
        String queryGetProprietaire = "SELECT proprietaire FROM compte WHERE code = ?";
        String queryInsertOperation = "INSERT INTO operation (code_compte, proprietaire, montant, type_operation) VALUES (?, ?, ?, ?)";

        try (
                PreparedStatement stmtGetProprietaire = MainController.connection.prepareStatement(queryGetProprietaire);
                PreparedStatement stmtInsertOperation = MainController.connection.prepareStatement(queryInsertOperation)
        ) {
            // Récupérer le propriétaire du compte
            stmtGetProprietaire.setInt(1, codeCompte);
            ResultSet resultSet = stmtGetProprietaire.executeQuery();

            if (resultSet.next()) {
                String proprietaire = resultSet.getString("proprietaire");

                // Insérer l'opération dans la table
                stmtInsertOperation.setInt(1, codeCompte);
                stmtInsertOperation.setString(2, proprietaire);
                stmtInsertOperation.setDouble(3, montant);
                stmtInsertOperation.setString(4, typeOperation);
                stmtInsertOperation.executeUpdate();
            }
        } catch (SQLException e) {
            Actions.afficherMessageErreur("Erreur lors de l'enregistrement de la transaction : " + e.getMessage());
        }
    }

    public static int executerMiseAJour(String query, double montant, int code) {
        try (PreparedStatement stmt = MainController.connection.prepareStatement(query)) {
            stmt.setDouble(1, montant);
            stmt.setInt(2, code);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            afficherMessageErreur("Erreur lors de l'exécution de la requête : " + e.getMessage());
            return 0;
        }
    }

    // Méthode pour afficher la boîte de dialogue d'ajout de compte
    public static void afficherDialogAjoutCompte(Text nbOperation, Text nbCompte) {
        // Créer les champs de texte pour le code du compte et le nom du propriétaire
        TextField codeCompteField = new TextField();
        codeCompteField.setPromptText("Code du compte");

        TextField nomProprietaireField = new TextField();
        nomProprietaireField.setPromptText("Nom du propriétaire");

        // Créer les boutons "Valider" et "Annuler"
        Button btnValider = new Button("Valider");
        Button btnAnnuler = new Button("Annuler");

        // Créer un HBox pour les boutons
        HBox hboxButtons = new HBox(10, btnValider, btnAnnuler);
        hboxButtons.setAlignment(Pos.CENTER);

        // Créer un VBox pour contenir les champs de texte et les boutons
        VBox vbox = new VBox(10, codeCompteField, nomProprietaireField, hboxButtons);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Créer la boîte de dialogue
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Ajouter un nouveau compte");
        dialog.setScene(new Scene(vbox, 300, 200));

        // Action pour le bouton "Valider"
        btnValider.setOnAction(e -> {
            String codeCompte = codeCompteField.getText();
            String nomProprietaire = nomProprietaireField.getText();

            // Vérification des champs
            if (!codeCompte.isEmpty() && !nomProprietaire.isEmpty()) {
                // Insérer le compte dans la base de données
                boolean isInserted = insererCompteDansBase(codeCompte, nomProprietaire);

                if (isInserted) {
                    // Afficher un message de confirmation
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText(null);
                    alert.setContentText("Le compte a été créé avec succès !");
                    mettreAJourLesValeurs(nbOperation,nbCompte);
                    alert.showAndWait();
                    dialog.close();
                } else {
                    // Afficher un message d'erreur
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Une erreur est survenue lors de la création du compte.");
                    alert.showAndWait();
                }
            } else {
                // Afficher un message d'erreur si les champs sont vides
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
            }
        });

        // Action pour le bouton "Annuler"
        btnAnnuler.setOnAction(e -> dialog.close());
        dialog.show();
    }
    // Méthode pour vérifier si un compte existe déjà
    private static boolean compteExiste(String codeCompte) {
        String query = "SELECT COUNT(*) FROM compte WHERE code = ?";
        try (PreparedStatement stmt = MainController.connection.prepareStatement(query)) {
            stmt.setString(1, codeCompte);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Retourne true si le compte existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retourne false si une erreur survient ou si le compte n'existe pas
    }

    // Méthode pour insérer le compte dans la base de données avec vérification préalable
    private static boolean insererCompteDansBase(String codeCompte, String nomProprietaire) {
        if (compteExiste(codeCompte)) {
            // Afficher un message d'erreur si le compte existe déjà
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Un compte avec ce code existe déjà.");
            alert.showAndWait();
            return false; // Interrompt l'insertion
        }

        // Insérer le compte s'il n'existe pas
        String query = "INSERT INTO compte (code, proprietaire) VALUES (?, ?)";
        try (PreparedStatement stmt = MainController.connection.prepareStatement(query)) {
            stmt.setString(1, codeCompte);
            stmt.setString(2, nomProprietaire);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour supprimer un compte de la base de données
    private static boolean supprimerCompteDansBase(String codeCompte) {
        if (!compteExiste(codeCompte)) {
            // Afficher un message si le compte n'existe pas
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Aucun compte trouvé avec ce code.");
            alert.showAndWait();
            return false;
        }

        // Suppression du compte
        String query = "DELETE FROM compte WHERE code = ?";
        try (PreparedStatement stmt = MainController.connection.prepareStatement(query)) {
            stmt.setString(1, codeCompte);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Méthode pour afficher la boîte de dialogue de suppression de compte
    public static void afficherDialogSuppressionCompte(Text nbOperation,Text nbCompte) {
        TextField codeCompteField = new TextField();
        codeCompteField.setPromptText("Code du compte");

        Button btnValider = new Button("Valider");
        Button btnAnnuler = new Button("Annuler");

        HBox hboxButtons = new HBox(10, btnValider, btnAnnuler);
        hboxButtons.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(10, codeCompteField, hboxButtons);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Supprimer un compte");
        dialog.setScene(new Scene(vbox, 300, 150));

        btnValider.setOnAction(e -> {
            String codeCompte = codeCompteField.getText();

            if (!codeCompte.isEmpty()) {
                boolean isDeleted = supprimerCompteDansBase(codeCompte);

                if (isDeleted) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText(null);
                    alert.setContentText("Le compte a été supprimé avec succès !");
                    mettreAJourLesValeurs(nbOperation,nbCompte);
                    alert.showAndWait();
                    dialog.close();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez entrer le code du compte.");
                alert.showAndWait();
            }
        });

        btnAnnuler.setOnAction(e -> dialog.close());
        dialog.show();
    }

    // Méthode pour récupérer le nombre total d'opérations
    private static int getNombreOperations() {
        String query = "SELECT COUNT(*) FROM operation";
        try (Statement stmt = MainController.connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Méthode pour récupérer le nombre total de comptes
    private static int getNombreComptes() {
        String query = "SELECT COUNT(*) FROM compte";
        try (Statement stmt = MainController.connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Méthode pour mettre à jour les valeurs affichées
    public static void mettreAJourLesValeurs(Text nombreOperationsText, Text nombreComptesText) {
        nombreOperationsText.setText(String.valueOf(getNombreOperations()));
        nombreComptesText.setText(String.valueOf(getNombreComptes()));
    }

}
