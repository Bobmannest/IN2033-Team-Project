package com.example.basket;

import com.example.catalogue.CatalogueItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BasketItemController {
    private CatalogueItem currentItem;

    @FXML private Label idLabel;
    @FXML private Label packageCostLabel;
    @FXML private Label packageTypeLabel;
    @FXML private Label statusLabel;

    public void setItem(CatalogueItem item) {
        this.currentItem = item;
        idLabel.setText("#" + item.getItem_id());
        packageCostLabel.setText("Price - £" + item.getPackage_cost());
        packageTypeLabel.setText(item.getPackage_type());
        statusLabel.setText("[" + item.getStatus() + "]");
    }

    @FXML
    private void handleRemoveFromBasket() {
        BasketList.removeItem(currentItem);
        currentItem.setAvailability(currentItem.getAvailability() + 1);
    }
}
