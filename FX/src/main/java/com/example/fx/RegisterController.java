package com.example.fx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {

    @FXML private TextField emailField;
    @FXML private Label statusLabel;

    // gets called when the register button is clicked
    @FXML
    private void handleRegister() {
        String email = emailField.getText().trim();

        // checking email field isn't empty
        if (email.isEmpty()) {
            showStatus("Please enter your email address.", false);
            return;
        }

        // checks email format
        if (!email.contains("@") || !email.contains(".")) {
            showStatus("Please enter a valid email address.", false);
            return;
        }


        try {
            // registers the member in the db and get back the generated pass
            String generatedPassword = MemberDAO.registerNonCommercialMember(email);

            // shows the generated login details to the user
            // will update later to email login details
            showStatus("Registration successful!\n"
                     + "Your login details:\n"
                     + "Email: " + email + "\n"
                     + "Password: " + generatedPassword + "\n"
                     + "You will be asked to change your password on first login.",
                     true);

            emailField.setDisable(true);

        } catch (IllegalArgumentException e) {
            // email already exists in the db
            showStatus(e.getMessage(), false);
        } catch (SQLException e) {
            showStatus("Database error: " + e.getMessage(), false);
        }
    }

    // called when the back button is clicked -> goes back to login screen
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/fx/App.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            showStatus("Could not return to sign in screen.", false);
        }
    }

    // helper method for status messages
    private void showStatus(String message, boolean success) {
        statusLabel.setStyle(success
            ? "-fx-text-fill: green; -fx-font-size: 12px;"
            : "-fx-text-fill: red; -fx-font-size: 12px;");
        statusLabel.setText(message);
    }
}