package com.example.members;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class CommercialRegisterController {

    @FXML private TextField companyNameField;
    @FXML private TextField companiesHouseField;
    @FXML private TextField directorNameField;
    @FXML private TextField businessTypeField;
    @FXML private TextField addressField;
    @FXML private TextField emailField;
    @FXML private Label statusLabel;

    @FXML
    private void handleSubmit() {
        String companyName = companyNameField.getText().trim();
        String companiesHouseNo = companiesHouseField.getText().trim();
        String directorName = directorNameField.getText().trim();
        String businessType = businessTypeField.getText().trim();
        String address = addressField.getText().trim();
        String email = emailField.getText().trim();

        if (companyName.isEmpty() || companiesHouseNo.isEmpty() || directorName.isEmpty()
                || businessType.isEmpty() || address.isEmpty() || email.isEmpty()) {
            showStatus("Please fill in all fields.", false);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showStatus("Please enter a valid email address.", false);
            return;
        }

        if (!companiesHouseNo.matches("[A-Z0-9]{8}")) {
            showStatus("Companies House number must be 8 alphanumeric characters (e.g. 12345678).", false);
            return;
        }

        try {
            if (CommercialApplicationDAO.emailExists(email)) {
                showStatus("An application with this email already exists.", false);
                return;
            }

            if (CommercialApplicationDAO.companiesHouseNoExists(companiesHouseNo)) {
                showStatus("An application with this Companies House number already exists.", false);
                return;
            }

            String applicationId = CommercialApplicationDAO.generateNextApplicationId();

            CommercialApplication app = new CommercialApplication(
                    applicationId, companyName, companiesHouseNo,
                    directorName, businessType, address, email, null, "pending"
            );

            CommercialApplicationDAO.submitApplication(app);

            showStatus("Application submitted successfully! Reference: " + applicationId +
                    "\nYou will be contacted at " + email + " once your application has been reviewed.", true);

            companyNameField.setDisable(true);
            companiesHouseField.setDisable(true);
            directorNameField.setDisable(true);
            businessTypeField.setDisable(true);
            addressField.setDisable(true);
            emailField.setDisable(true);

        } catch (SQLException e) {
            showStatus("Database error: " + e.getMessage(), false);
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showStatus("Could not return to sign in screen.", false);
        }
    }

    private void showStatus(String message, boolean success) {
        statusLabel.setStyle(success
                ? "-fx-text-fill: green; -fx-font-size: 12px;"
                : "-fx-text-fill: red; -fx-font-size: 12px;");
        statusLabel.setText(message);
    }
}