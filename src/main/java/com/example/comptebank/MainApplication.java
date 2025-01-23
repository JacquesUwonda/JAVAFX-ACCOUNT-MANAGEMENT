
package com.example.comptebank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {


    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        MainApplication.primaryStage = primaryStage;
        showLoginPage();
    }

    public static void showLoginPage() {
        try {
            Parent root = FXMLLoader.load(MainApplication.class.getResource("/fxml/login.fxml"));
            primaryStage.setTitle("Connexion");
            primaryStage.setScene(new Scene(root, 646, 454));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showMainPage() {
        try {
            Parent root = FXMLLoader.load(MainApplication.class.getResource("/fxml/main.fxml"));
            primaryStage.setTitle("Gestion des Comptes");
            primaryStage.setScene(new Scene(root, 646, 454));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}
