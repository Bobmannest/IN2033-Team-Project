package com.example.promotion;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class CampaignItemController {

    @FXML private TextField campaignIdField;
    @FXML private TextField itemIdField;
    @FXML private TextField itemDiscountField;
    @FXML private Label statusLabel;

    private final PromotionService promotionService = new PromotionService();

    @FXML
    private void handleAddItemToCampaign() {
        try {
            String campaignId = campaignIdField.getText().trim();

            if (campaignId.isEmpty()) {
                throw new IllegalArgumentException("Campaign ID is required.");
            }

            int itemId;
            try {
                itemId = Integer.parseInt(itemIdField.getText().trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Item ID must be a valid integer.");
            }

            Double itemDiscount = null;
            String discountText = itemDiscountField.getText().trim();
            if (!discountText.isEmpty()) {
                try {
                    itemDiscount = Double.parseDouble(discountText);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Item discount must be a valid number.");
                }
            }

            promotionService.addItemToCampaign(campaignId, itemId, itemDiscount);
            showStatus("Item added to campaign successfully.", true);

        } catch (IllegalArgumentException e) {
            showStatus(e.getMessage(), false);
        } catch (SQLException e) {
            showStatus("Database error: " + e.getMessage(), false);
        }
    }

    private void showStatus(String message, boolean success) {
        statusLabel.setStyle(success ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
        statusLabel.setText(message);
    }
}