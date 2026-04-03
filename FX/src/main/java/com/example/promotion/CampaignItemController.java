package com.example.promotion;

import com.example.catalogue.CatalogueDatabase;
import com.example.catalogue.CatalogueItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class CampaignItemController {

    @FXML private TextField campaignIdField;
    @FXML private ComboBox<CatalogueItem> itemComboBox;
    @FXML private TextField itemDiscountField;
    @FXML private Label statusLabel;

    private final PromotionService promotionService = new PromotionService();

    @FXML
    public void initialize() {
        itemComboBox.getItems().addAll(CatalogueDatabase.getCatalogueItems());

        itemComboBox.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(CatalogueItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getItem_id() + " - " + item.getDescription());
                }
            }
        });

        itemComboBox.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(CatalogueItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Choose catalogue item");
                } else {
                    setText(item.getItem_id() + " - " + item.getDescription());
                }
            }
        });
    }

    @FXML
    private void handleAddItemToCampaign() {
        try {
            String campaignId = campaignIdField.getText().trim();

            if (campaignId.isEmpty()) {
                throw new IllegalArgumentException("Campaign ID is required.");
            }

            CatalogueItem selectedItem = itemComboBox.getValue();
            if (selectedItem == null) {
                throw new IllegalArgumentException("Please select a catalogue item.");
            }

            int itemId = selectedItem.getItem_id();

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
    private void handleCreatePromotion() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Promotion.fxml"));
            Scene scene = new Scene(loader.load(), 900, 650);
            Stage stage = (Stage) campaignIdField.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            showStatus("Could not open create promotion screen.", false);
        }
    }

    @FXML
    private void handleOrders() {
        showStatus("Orders screen not implemented yet.", false);
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
        showStatus("Basket screen not implemented yet.", false);
    }
}