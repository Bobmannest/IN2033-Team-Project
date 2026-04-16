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
        String roundedPrice = String.format("%.2f", item.getPackage_cost());


        idLabel.setText("#" + item.getDescription());
        packageCostLabel.setText("£" + roundedPrice);
    }
}
