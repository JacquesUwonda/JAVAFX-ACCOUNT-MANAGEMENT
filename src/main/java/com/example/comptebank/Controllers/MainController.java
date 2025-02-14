package com.example.comptebank.Controllers;

import com.example.comptebank.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import static com.example.comptebank.Controllers.Actions.recupererOperations;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class MainController {

    public static Connection connection;

    public MainController() {
        try {
            connection = Connexion.getConnection();
        } catch (Exception e) {
            Actions.afficherMessageErreur("Erreur de connexion à la base de données : " + e.getMessage());
        }
    }
    @FXML
    private TextField numCompteField;

    @FXML
    private TextField montantField;

    @FXML
    private Button validerButton;

    @FXML
    private Button annulerButton;

    @FXML
    private TextField numCompteRetraitField;

    @FXML
    private TextField montantRetraitField;

    @FXML
    private Button validerRetraitButton;

    @FXML
    private Button annulerRetraitButton;

    @FXML
    private TextField numCompteSourceField;

    @FXML
    private TextField montantVirementField;

    @FXML
    private TextField numComptedestinataireField;

    @FXML
    private Button validerVirementButton;

    @FXML
    private Button annulerVirementButton;

    @FXML
    private TableView<Operation> operationTable;
    @FXML
    private TableColumn<Operation, Integer> idColumn;
    @FXML
    private TableColumn<Operation, Integer> codeColumn;
    @FXML
    private TableColumn<Operation, Double> montantColumn;
    @FXML
    private TableColumn<Operation, String> typeOperationColumn;
    @FXML
    private TableColumn<Operation, String> proprietaireColumn;
    @FXML
    private TableColumn<Operation, Date> dateOperationColumn;




    @FXML private Button accueilButton;
    @FXML private Button versementButton;
    @FXML private Button retraitButton;
    @FXML private Button virementButton;
    @FXML private Button listeOperationButton;
    @FXML private Button deconnexionButton;
    @FXML private Button ajoutCompteButton;
    @FXML private Button supprimerCompteButton;


    @FXML private VBox accueilVBox;
    @FXML private VBox versementVBox;
    @FXML private VBox retraitVBox;
    @FXML private VBox virementVBox;
    @FXML private VBox listeOperationVBox;
    @FXML
    private Text nombreOperationsText;

    @FXML
    private Text nombreComptesText;
    @FXML
    private TextField numCompteSoldeField;

    @FXML
    private Label soldeAvantLabel;

    @FXML
    private Label soldeApresLabel;

    @FXML
    private void ajouterCompte() {
        Actions.afficherDialogAjoutCompte(nombreOperationsText,nombreComptesText);
    }

    @FXML
    private void redirectToHomePage() {
        MainApplication.showLoginPage();
        // Implement your logic to load the home page here.
    }
    @FXML
    private void consulterSolde() {
        Solde.afficherSolde(numCompteSoldeField,soldeAvantLabel,soldeApresLabel);
        // Implement your logic to load the home page here.
    }
//JasperRepport methode
    @FXML
    private void voirRapport() {
        JasperPrint jasperPrint;
        Map param=new HashMap();

        try {
            jasperPrint= JasperFillManager.fillReport("/fxml/RapportOperation.jasper",param,MainController.connection);
            JasperViewer viewer=new JasperViewer(jasperPrint,false);
            viewer.setTitle("Rapport des Operations");
            viewer.setVisible(true);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void initialize() {
        //insertion dans la table liste
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        typeOperationColumn.setCellValueFactory(new PropertyValueFactory<>("typeOperation"));
        proprietaireColumn.setCellValueFactory(new PropertyValueFactory<>("proprietaire"));
        dateOperationColumn.setCellValueFactory(new PropertyValueFactory<>("dateOperation"));
        // Associer le bouton "Supprimer un compte" à l'action
        supprimerCompteButton.setOnAction(e -> Actions.afficherDialogSuppressionCompte(nombreOperationsText,nombreComptesText));
        Actions.mettreAJourLesValeurs(nombreOperationsText,nombreComptesText);


        // Initialiser l'état actif et la visibilité des vBox
        setActiveMenu(accueilButton);
        showVBox(accueilVBox);

        // Associer les événements aux boutons
        validerButton.setOnAction(event -> Versement.effectuerVersement(numCompteField,montantField,nombreOperationsText,nombreComptesText));
        annulerButton.setOnAction(event -> Versement.annulerVersement(numCompteField,montantField));


        // Associer les événements aux boutons Retrait
        validerRetraitButton.setOnAction(event -> Retrait.effectuerRetrait(numCompteRetraitField,montantRetraitField,nombreOperationsText,nombreOperationsText));
        annulerRetraitButton.setOnAction(event -> Retrait.annulerRetrait(numCompteRetraitField,montantRetraitField));

        // Associer les événements aux boutons Virement
        validerVirementButton.setOnAction(event -> Virement.effectuerVirement(numCompteSourceField,numComptedestinataireField,montantVirementField, nombreOperationsText,nombreComptesText));
        annulerVirementButton.setOnAction(event -> Virement.annulerVirement(numCompteSourceField,numComptedestinataireField,montantVirementField));

    }


    // Méthode pour afficher le bon VBox
    private void showVBox(VBox vboxToShow) {
        VBox[] vboxes = {accueilVBox, versementVBox, retraitVBox, virementVBox, listeOperationVBox};
        for (VBox vbox : vboxes) {
            if (vbox == vboxToShow) {
                vbox.setVisible(true);
            } else {
                vbox.setVisible(false);
            }
        }
    }

    // Méthodes pour chaque bouton (associées à un événement de clic)
    @FXML
    private void onAccueillButtonClick() {
        setActiveMenu(accueilButton);
        showVBox(accueilVBox);
    }

    @FXML
    private void onVersementButtonClick() {
        setActiveMenu(versementButton);
        showVBox(versementVBox);
    }

    @FXML
    private void onRetraitButtonClick() {
        setActiveMenu(retraitButton);
        showVBox(retraitVBox);
    }

    @FXML
    private void onVirementButtonClick() {
        setActiveMenu(virementButton);
        showVBox(virementVBox);
    }

    private void setActiveMenu(Button activeButton) {
        // Parcourir les boutons du même parent
        for (Node node : activeButton.getParent().getChildrenUnmodifiable()) {
            if (node instanceof Button) {
                Button button = (Button) node;

                // Appliquer un style par défaut à tous les boutons
                if (button == activeButton) {
                    // Style pour le bouton actif
                    button.setStyle("-fx-background-color: #1c4381; -fx-text-fill: white; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: white;");
                } else if (button == deconnexionButton) {
                    // Style spécifique pour le bouton de déconnexion
                    button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: white;");
                } else if (button == supprimerCompteButton) {
                    // Style spécifique pour le bouton supprimerCompte
                    button.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-text-fill: red; -fx-border-radius: 20;");
                } else if (button == ajoutCompteButton) {
                    // Style spécifique pour le bouton ajouterCompte
                    button.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-background-radius: 20;");
                } else {
                    // Style pour les autres boutons inactifs
                    button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
                }

                // Ajouter des effets de survol (hover) pour chaque bouton
                button.setOnMouseEntered(event -> {
                    button.setCursor(Cursor.HAND);
                    if (button == deconnexionButton || button == supprimerCompteButton) {
                        button.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: white;");
                    } else if (button == ajoutCompteButton) {
                        button.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 20;");
                    } else {
                        button.setStyle("-fx-background-color: #2a3b5f; -fx-text-fill: white; -fx-background-radius: 20;");
                    }
                });

                button.setOnMouseExited(event -> {
                    if (button == deconnexionButton) {
                        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: white;");
                    } else if (button == ajoutCompteButton) {
                        button.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-background-radius: 20;");
                    } else if (button == activeButton) {
                        button.setStyle("-fx-background-color: #1c4381; -fx-text-fill: white; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: white;");
                    } else {
                        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
                    }
                });
            }
        }
    }



    @FXML
    private void onListeOperationButtonClick() {
        // Charger les données dans le tableau
        operationTable.setItems(recupererOperations());
        setActiveMenu(listeOperationButton);
        showVBox(listeOperationVBox);
    }

    private void handleLogout(){
        MainApplication.showLoginPage();
    }

}
