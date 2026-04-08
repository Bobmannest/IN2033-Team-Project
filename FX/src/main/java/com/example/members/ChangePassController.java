package com.example.members;

import com.example.fx.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

// this screen shows when a member logs in for the first time
public class ChangePassController {

    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    // the member changing their pass
    private Member member;

    // called from controller.java to pass the logged in member over
    public void setMember(Member member) {
        this.member = member;
    }

    // calls in when the set password button is clicked
    @FXML
    private void handleChangePassword() {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // below are password condition checks
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in both fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }

        if (newPassword.length() < 8) {
            showError("Password must be at least 8 characters.");
            return;
        }

        try {
            // update the pass in the db and set first login bool to false
            MemberDAO.changePassword(member.getAccountNo(), newPassword);
            member.setFirstLogin(false);

            // brings up a success message and takes you back to login screen
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/fx/Login.fxml"));
            Parent root = loader.load();
            Controller loginCtrl = loader.getController();
            loginCtrl.showSuccessMessage("Password changed! Please log in again.");
            Stage stage = (Stage) newPasswordField.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        } catch (IOException e) {
            showError("Password changed but could not navigate. Please restart.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }
}