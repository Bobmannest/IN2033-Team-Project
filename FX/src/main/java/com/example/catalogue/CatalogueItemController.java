package com.example.catalogue;

import com.example.basket.BasketList;
import com.example.promotion.PromotionService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CatalogueItemController {
    private CatalogueItem currentItem;

    @FXML private TextField qtyField;
    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label packageCostLabel;
    @FXML private Label packageTypeLabel;
    @FXML private Label availabilityLabel;
    @FXML private Label statusLabel;

    private String selectedCampaignId;
    private final PromotionService promotionService = new PromotionService();

    public void setItem(CatalogueItem item) {
        this.currentItem = item;

        idLabel.setText("#" + item.getItem_id());
        nameLabel.setText(item.getDescription());
        packageTypeLabel.setText(item.getPackage_type());
        availabilityLabel.setText("Available - " + item.getAvailability());
        statusLabel.setText("[" + item.getStatus() + "]");

        updatePromotionDisplay();
    }

    public void setSelectedCampaignId(String selectedCampaignId) {
        this.selectedCampaignId = selectedCampaignId;
        updatePromotionDisplay();
    }

    private void updatePromotionDisplay() {
        if (currentItem == null) {
            return;
        }

        double originalPrice = currentItem.getPackage_cost();

        try {
            if (selectedCampaignId != null && !selectedCampaignId.isBlank()) {
                Double discount = promotionService.getDiscountForItem(selectedCampaignId, currentItem.getItem_id());

                if (discount != null && discount > 0) {
                    double discountedPrice = originalPrice - (originalPrice * (discount / 100.0));
                    packageCostLabel.setText(
                            String.format("Price - £%.2f (%s%% off)", discountedPrice, removeTrailingZero(discount))
                    );
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        packageCostLabel.setText(String.format("Price - £%.2f", originalPrice));
    }

    private String removeTrailingZero(Double value) {
        if (value == null) {
            return "0";
        }

        if (value == Math.floor(value)) {
            return String.valueOf(value.intValue());
        }

        return String.valueOf(value);
    }

    @FXML
    private void handleAddToBasket() {
        int quantity = 1;
        String qtyFieldInput = qtyField.getText().trim();

        if (!qtyFieldInput.isEmpty()) {
            quantity = Integer.parseInt(qtyFieldInput);
        }

        if (quantity <= currentItem.getAvailability()) {
            CatalogueController.stockErrorLabel.setText("");

            double discount = 0.0;
            double finalPrice = currentItem.getPackage_cost();
            String appliedCampaignId = null;

            try {
                if (selectedCampaignId != null && !selectedCampaignId.isBlank()) {
                    Double selectedCampaignDiscount =
                            promotionService.getDiscountForItem(selectedCampaignId, currentItem.getItem_id());

                    if (selectedCampaignDiscount != null && selectedCampaignDiscount > 0) {
                        discount = selectedCampaignDiscount;
                        finalPrice = currentItem.getPackage_cost()
                                - (currentItem.getPackage_cost() * discount / 100.0);
                        appliedCampaignId = selectedCampaignId;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < quantity; i++) {
                BasketList.addItem(currentItem, discount, finalPrice, appliedCampaignId);
                currentItem.setAvailability(currentItem.getAvailability() - 1);
            }

            availabilityLabel.setText("Available - " + currentItem.getAvailability());

            if (appliedCampaignId != null) {
                try {
                    promotionService.recordIncludedInOrder(appliedCampaignId, currentItem.getItem_id(), quantity);
                } catch (Exception e) {
                    System.out.println("Failed to record campaign item metric: " + e.getMessage());
                }
            }
        } else {
            CatalogueController.stockErrorLabel.setText("Insufficient Stock Available");
        }
    }
}