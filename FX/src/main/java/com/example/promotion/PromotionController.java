package com.example.promotion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class PromotionController {

    @FXML private TextField campaignIdField;
    @FXML private TextField campaignNameField;
    @FXML private TextArea campaignDescriptionArea;
    @FXML private DatePicker startDatePicker;
    @FXML private TextField startTimeField;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField endTimeField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private ComboBox<String> discountModeComboBox;
    @FXML private TextField defaultDiscountField;
    @FXML private TextField createdByField;
    @FXML private Label statusLabel;

    private final PromotionService promotionService = new PromotionService();

    @FXML
    public void initialize() {
        statusComboBox.getItems().addAll("scheduled", "active", "cancelled", "expired");
        discountModeComboBox.getItems().addAll("fixed", "variable");
        statusComboBox.setValue("scheduled");
        discountModeComboBox.setValue("fixed");
    }

    @FXML
    private void handleCreateCampaign() {
        try {
            PromotionCampaign campaign = buildCampaignFromForm();
            promotionService.createCampaign(campaign);
            showStatus("Campaign created successfully.", true);
            clearForm();
        } catch (IllegalArgumentException e) {
            showStatus(e.getMessage(), false);
        } catch (SQLException e) {
            showStatus("Database error: " + e.getMessage(), false);
        } catch (Exception e) {
            showStatus("Error: " + e.getMessage(), false);
        }
    }

    private PromotionCampaign buildCampaignFromForm() {
        String campaignId = campaignIdField.getText().trim();
        String campaignName = campaignNameField.getText().trim();
        String description = campaignDescriptionArea.getText().trim();

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Please choose both start and end dates.");
        }

        String startTimeText = startTimeField.getText().trim();
        String endTimeText = endTimeField.getText().trim();

        if (startTimeText.isEmpty() || endTimeText.isEmpty()) {
            throw new IllegalArgumentException("Please enter both start and end times in HH:mm format.");
        }

        LocalTime startTime;
        LocalTime endTime;

        try {
            startTime = LocalTime.parse(startTimeText);
            endTime = LocalTime.parse(endTimeText);
        } catch (Exception e) {
            throw new IllegalArgumentException("Time must be in HH:mm format.");
        }

        Double defaultDiscount = null;
        String discountText = defaultDiscountField.getText().trim();
        if (!discountText.isEmpty()) {
            try {
                defaultDiscount = Double.parseDouble(discountText);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Default discount must be a valid number.");
            }
        }

        String createdBy = createdByField.getText().trim();

        return new PromotionCampaign(
                campaignId,
                campaignName,
                description,
                LocalDateTime.of(startDate, startTime),
                LocalDateTime.of(endDate, endTime),
                statusComboBox.getValue(),
                discountModeComboBox.getValue(),
                defaultDiscount,
                createdBy.isEmpty() ? null : createdBy
        );
    }

    @FXML
    private void clearForm() {
        campaignIdField.clear();
        campaignNameField.clear();
        campaignDescriptionArea.clear();
        startDatePicker.setValue(null);
        startTimeField.clear();
        endDatePicker.setValue(null);
        endTimeField.clear();
        defaultDiscountField.clear();
        createdByField.clear();
        statusComboBox.setValue("scheduled");
        discountModeComboBox.setValue("fixed");
    }

    private void showStatus(String message, boolean success) {
        statusLabel.setStyle(success ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
        statusLabel.setText(message);
    }

    @FXML
    private void handleHome() {
        handleCatalogue();
    }

    @FXML
    private void handleCatalogue() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Catalogue.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) campaignIdField.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            showStatus("Could not open catalogue.", false);
        }
    }

    @FXML
    private void handleManagePromotions() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/CampaignItem.fxml"));
            Scene scene = new Scene(loader.load(), 750, 500);
            Stage stage = (Stage) campaignIdField.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            showStatus("Could not open manage promotions screen.", false);
        }
    }

    @FXML
    private void handleOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/OrderHistory.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) campaignIdField.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            showStatus("Could not open orders screen.", false);
        }
    }

    @FXML
    private void handleAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Account.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) campaignIdField.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            showStatus("Could not open account screen.", false);
        }
    }

    @FXML
    private void handleBasket() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Basket.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 650);
            Stage stage = (Stage) campaignIdField.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}