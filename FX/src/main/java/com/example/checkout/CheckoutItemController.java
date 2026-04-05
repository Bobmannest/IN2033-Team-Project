package com.example.checkout;

import com.example.basket.BasketList;
import com.example.catalogue.CatalogueItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class CheckoutItemController {
    private CatalogueItem currentItem;

    @FXML private Label idLabel;
    @FXML private Label quantityLabel;
    @FXML private Label packageCostLabel;


    public void setItem(CatalogueItem item) {
        this.currentItem = item;

        idLabel.setText("#" + item.getItem_id());
        quantityLabel.setText("Qty - X");
        packageCostLabel.setText("£" + item.getPackage_cost());
    }
}
