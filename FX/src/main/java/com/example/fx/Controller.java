package com.example.fx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Controller {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    // this is called when the login button is clicked
    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // making sure neither field is empty before proceeding
        if (email.isEmpty() || password.isEmpty()) {
            showError("Please enter your email and password.");
            return;
        }

        try {

            // checks credentials against db
            Member member = MemberDAO.login(email, password);

            if (member == null) {
                showError("Invalid email or password.");
            } else if (member.isFirstLogin()) {
                // first login = force password change
                openChangePasswordScreen(member);
            } else {
                // goes to dashboard (normal login)
                openDashboard(member);
            }

        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    // called when register button is clicked -> switches to register screen
    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Register.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            showError("Could not open registration screen.");
        }
    }

    // opens change pass screen
    private void openChangePasswordScreen(Member member) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/ChangePassword.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);
            ChangePassController ctrl = loader.getController();
            ctrl.setMember(member);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            showError("Could not open password change screen.");
        }
    }

    // opens the main dashboard up (not implemented properly yet)
    private void openDashboard(Member member) {
        // need to replace the login dashboard with the real one
        showError("Login successful! Welcome " + member.getEmail());
    }

    private void showError(String message) {
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setText(message);
    }

    // success message in green
    public void showSuccessMessage(String message) {
        errorLabel.setStyle("-fx-text-fill: green;");
        errorLabel.setText(message);
    }
}