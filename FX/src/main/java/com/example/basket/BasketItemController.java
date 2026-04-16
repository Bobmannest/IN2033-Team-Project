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
        String roundedPrice = String.format("%.2f", item.getPackage_cost());


        idLabel.setText("#" + item.getItem_id());
        nameLabel.setText(item.getDescription());
        packageCostLabel.setText("Price - £" + roundedPrice);
        packageTypeLabel.setText(item.getPackage_type());
        statusLabel.setText("[" + item.getStatus() + "]");
    }

    @FXML
    private void handleRemoveFromBasket() {
        BasketList.removeItem(currentItem);
        currentItem.setAvailability(currentItem.getAvailability() + 1);
    }
}
