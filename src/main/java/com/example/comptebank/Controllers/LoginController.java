package com.example.comptebank.Controllers;

import com.example.comptebank.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private VBox loginVBox, registerVBox;

    @FXML
    private TextField loginEmailField, registerEmailField;

    @FXML
    private PasswordField loginPasswordField, registerPasswordField, registerConfirmPasswordField;

    @FXML
    private Label loginErrorLabel, registerErrorLabel;
    @FXML
    private void handleLogin() {
        String email = loginEmailField.getText();
        String password = loginPasswordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            loginErrorLabel.setTextFill(Color.RED);
            loginErrorLabel.setText("Please fill all fields!");
            return;
        }

        try (Connection connection=Connexion.getConnection() ){
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                loginErrorLabel.setTextFill(Color.GREEN);
                loginErrorLabel.setText("Login successful! Redirecting...");
                MainApplication.showMainPage();
                // Redirect to the home page
            } else {
                loginErrorLabel.setTextFill(Color.RED);
                loginErrorLabel.setText("Invalid email or password!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            loginErrorLabel.setTextFill(Color.RED);
            loginErrorLabel.setText("An error occurred while logging in.");
        }
    }

    @FXML
    private void handleRegister() {
        String email = registerEmailField.getText();
        String password = registerPasswordField.getText();
        String confirmPassword = registerConfirmPasswordField.getText();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            registerErrorLabel.setTextFill(Color.RED);
            registerErrorLabel.setText("Please fill all fields!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            registerErrorLabel.setTextFill(Color.RED);
            registerErrorLabel.setText("Passwords do not match!");
            return;
        }

        try (Connection connection=Connexion.getConnection()){
            String query = "INSERT INTO users (email, password) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                registerErrorLabel.setTextFill(Color.GREEN);
                registerErrorLabel.setText("Account created successfully! Please log in.");
                switchToLogin();
            } else {
                registerErrorLabel.setTextFill(Color.RED);
                registerErrorLabel.setText("Failed to create account.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            registerErrorLabel.setTextFill(Color.RED);
            registerErrorLabel.setText("An error occurred during registration.");
        }
    }

    @FXML
    private void switchToLogin() {
        registerVBox.setVisible(false);
        loginVBox.setVisible(true);
        registerEmailField.clear();
        registerPasswordField.clear();
        registerConfirmPasswordField.clear();

    }

    @FXML
    private void switchToRegister() {
        loginVBox.setVisible(false);
        registerVBox.setVisible(true);
        loginEmailField.clear();
        loginPasswordField.clear();
    }

}
