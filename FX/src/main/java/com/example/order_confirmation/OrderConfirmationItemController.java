package com.example.order_confirmation;

import com.example.basket.BasketList;
import com.example.catalogue.CatalogueItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class OrderConfirmationItemController {
    private CatalogueItem currentItem;

    @FXML
    private Label idLabel;
    @FXML private Label packageCostLabel;

    public void setItem(CatalogueItem item) {
        this.currentItem = item;
        idLabel.setText("#" + item.getItem_id());
        packageCostLabel.setText("Price - £" + item.getPackage_cost());
    }
}