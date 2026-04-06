package com.example.catalogue;

import com.example.basket.BasketList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class CatalogueItemController {
    private CatalogueItem currentItem;
    @FXML private TextField qtyField;

    @FXML private Label idLabel;
    @FXML private Label packageCostLabel;
    @FXML private Label packageTypeLabel;
    @FXML private Label availabilityLabel;
    @FXML private Label statusLabel;

    public void setItem(CatalogueItem item) {
        this.currentItem = item;

        idLabel.setText("#" + item.getItem_id());
        packageCostLabel.setText("Price - £" + item.getPackage_cost());
        packageTypeLabel.setText(item.getPackage_type());
        availabilityLabel.setText("Available - " + item.getAvailability());
        statusLabel.setText("[" + item.getStatus() + "]");
    }

    @FXML
    private void handleAddToBasket() {
        int quantity = 1;
        String qtyFieldInput = qtyField.getText().trim();

        if (!qtyFieldInput.isEmpty()) {quantity = Integer.parseInt(qtyFieldInput);}

        if (quantity <= currentItem.getAvailability()) {
            for (int i = 0; i < quantity; i++) {
                BasketList.addItem(currentItem);
                currentItem.setAvailability(currentItem.getAvailability() - 1);
            }
            availabilityLabel.setText("Available - " + currentItem.getAvailability());
        } else {
            //error
        }
    }
}
