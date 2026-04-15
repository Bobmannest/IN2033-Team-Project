package com.example.members;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

            new Thread(() -> {
                try {
                    java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
                    String encodedPassword = java.net.URLEncoder.encode(generatedPassword, "UTF-8");
                    String encodedEmail = java.net.URLEncoder.encode(email, "UTF-8");
                    java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                            .uri(java.net.URI.create("http://localhost:8080/api/emails/sendRegistration"
                                    + "?recipient_email=" + encodedEmail
                                    + "&generated_password=" + encodedPassword))
                            .POST(java.net.http.HttpRequest.BodyPublishers.noBody())
                            .build();
                    client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            showStatus("Registration successful! Your login details have been sent to " + email, true);
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
                    getClass().getResource("/com/example/fx/Login.fxml"));
            javafx.scene.Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            boolean wasMaximized = stage.isMaximized();
            stage.getScene().setRoot(root);
            if (wasMaximized) stage.setMaximized(true);
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

    // navigates to the commercial account application screen
    @FXML
    private void handleCommercialRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/CommercialRegister.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showStatus("Could not open commercial registration.", false);
        }
    }
}