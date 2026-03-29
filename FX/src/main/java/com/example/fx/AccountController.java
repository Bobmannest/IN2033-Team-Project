package com.example.fx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AccountController {

    @FXML private Label accountNoLabel;
    @FXML private Label emailLabel;
    @FXML private Label memberTypeLabel;

    // this runs automatically when the screen loads
    @FXML
    public void initialize() {
        // gets the currently logged in member
        Member member = Session.getMember();

        if (member != null) {
            accountNoLabel.setText("Account No: " + member.getAccountNo());
            emailLabel.setText("Email: " + member.getEmail());
            memberTypeLabel.setText("Member Type: " + member.getMemberType());
        }
    }

    // when the logout button is clicked -> goes back to login screen
    @FXML
    private void handleLogout() {
        try {
            // clears the session
            Session.setMember(null);

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Login.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);
            Stage stage = (Stage) accountNoLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}