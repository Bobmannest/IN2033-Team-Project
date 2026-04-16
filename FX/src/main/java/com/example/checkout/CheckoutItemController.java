package com.example.checkout;

import com.example.basket.BasketList;
import com.example.catalogue.CatalogueItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CheckoutItemController {
    private CatalogueItem currentItem;

    @FXML private Label idLabel;
    @FXML private Label packageCostLabel;

    public void setItem(CatalogueItem item) {
        this.currentItem = item;

        double finalPrice = BasketList.getFinalPrice(item);
        double discountPct = BasketList.getDiscountPct(item);

        idLabel.setText("#" + item.getDescription());

        if (discountPct > 0) {
            packageCostLabel.setText(
                    String.format("£%.2f (%.0f%% off)", finalPrice, discountPct)
            );
        } else {
            packageCostLabel.setText(String.format("£%.2f", finalPrice));
        }
    }
}