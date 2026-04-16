package com.example.basket;

import com.example.catalogue.CatalogueItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BasketItemController {
    private CatalogueItem currentItem;

    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label packageCostLabel;
    @FXML private Label packageTypeLabel;
    @FXML private Label statusLabel;

    public void setItem(CatalogueItem item) {
        this.currentItem = item;

        double finalPrice = BasketList.getFinalPrice(item);
        double discountPct = BasketList.getDiscountPct(item);

        idLabel.setText("#" + item.getItem_id());
        nameLabel.setText(item.getDescription());

        if (discountPct > 0) {
            packageCostLabel.setText(String.format("Price - £%.2f (%.0f%% off)", finalPrice, discountPct));
        } else {
            packageCostLabel.setText(String.format("Price - £%.2f", finalPrice));
        }

        packageTypeLabel.setText(item.getPackage_type());
        statusLabel.setText("[" + item.getStatus() + "]");
    }

    @FXML
    private void handleRemoveFromBasket() {
        BasketList.removeItem(currentItem);
        currentItem.setAvailability(currentItem.getAvailability() + 1);
    }
}