package com.example.catalogue;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class CatalogueItemController {
    @FXML
    private Label idLabel;
    @FXML
    private Label packageCostLabel;
    @FXML
    private Label packageTypeLabel;
    @FXML
    private Label statusLabel;

    public void setItem(CatalogueItem item) {
        idLabel.setText("#" + item.getItem_id());
        packageCostLabel.setText("Price - £" + item.getPackage_cost());
        packageTypeLabel.setText(item.getPackage_type());
        statusLabel.setText("[" + item.getStatus() + "]");

    }
}
