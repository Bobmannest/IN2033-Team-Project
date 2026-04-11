package com.example.fx;

import com.example.members.ChangePassController;
import com.example.members.Member;
import com.example.members.MemberDAO;
import com.example.members.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Could not open registration screen.");
        }
    }

    // continue as guest
    @FXML
    private void handleGuestLogin() {
        try {
            Session.setMember(null);
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Could not open catalogue.");
        }
    }

    // opens change pass screen
    private void openChangePasswordScreen(Member member) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/ChangePassword.fxml"));
            Parent root = loader.load();
            ChangePassController ctrl = loader.getController();
            ctrl.setMember(member);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Could not open password change screen.");
        }
    }

    // opens the main dashboard up
    private void openDashboard(Member member) {
        try {
            // stores the logged in member
            Session.setMember(member);
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Could not open dashboard.");
        }
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

    @FXML
    private void handleForgotPassword() {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            showError("Please enter your email address first.");
            return;
        }

        try {
            String newPassword = MemberDAO.resetPassword(email);
            if (newPassword == null) {
                showError("No account found with that email.");
            } else {
                final String password = newPassword;
                showSuccessMessage("Sending new password to your email...");
                new Thread(() -> {
                    new com.example.email.EmailService().sendRegistrationEmail(email, password);
                    javafx.application.Platform.runLater(() ->
                            showSuccessMessage("A new password has been sent to your email.")
                    );
                }).start();
            }
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }
}