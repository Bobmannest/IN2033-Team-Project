package com.example.catalogue;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CatalogueController {
    @FXML private VBox catalogueVBox;

    public void displayProducts(List<CatalogueProduct> products) {
        catalogueVBox.getChildren().clear();

        for (CatalogueProduct product : products) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/catalogueItem.fxml"));
                HBox itemCard = loader.load();

                CatalogueItemController itemCtrl = loader.getController();
                itemCtrl.setProduct(product);

                catalogueVBox.getChildren().add(itemCard);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //testing method
    @FXML
    public void initialize() {
        List<CatalogueProduct> testProducts = new ArrayList<>();

        for (int i = 0; i < 18; i++) {
            CatalogueProduct p = new CatalogueProduct();
            p.unit = "Unit " + i;
            p.quantity_available = i * 10;
            p.bulk_cost = i * 9.99;
            testProducts.add(p);
        }

        displayProducts(testProducts);
    }

    // navigates to the account screen when the account button is clicked
    @FXML
    private void handleAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Account.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) catalogueVBox.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
