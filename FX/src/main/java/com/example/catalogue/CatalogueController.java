package com.example.catalogue;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CatalogueController {
    @FXML private VBox catalogueVBox;

    public void displayProducts(List<CatalogueProduct> products) {
        catalogueVBox.getChildren().clear();

        HBox currentRow = null;

        for (int i = 0; i < products.size(); i++) {
            if (i % 3 == 0) {
                currentRow = new HBox(10);
                catalogueVBox.getChildren().add(currentRow);
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/catalogueItem.fxml"));
                VBox itemCard = loader.load();

                CatalogueItemController itemCtrl = loader.getController();
                itemCtrl.setProduct(products.get(i));

                currentRow.getChildren().add(itemCard);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    //testing method
    @FXML
    public void initialize() {
        List<CatalogueProduct> testProducts = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            CatalogueProduct p = new CatalogueProduct();
            p.unit = "Unit " + i;
            p.quantity_available = i * 10;
            p.bulk_cost = i * 9.99;
            testProducts.add(p);
        }

        displayProducts(testProducts);
    }
}
