package com.example.basket;

import com.example.catalogue.CatalogueDatabase;
import com.example.catalogue.CatalogueItem;
import com.example.catalogue.CatalogueItemController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class BasketController {
    @FXML private VBox basketVBox;

    //Displays items to basket
    public void displayItems() {
        basketVBox.getChildren().clear();

        List<CatalogueItem> items = BasketList.getBasketItems();
        for (CatalogueItem item : items) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/basketItemBox.fxml"));
                HBox itemCard = loader.load();

                BasketItemController itemCtrl = loader.getController();
                itemCtrl.setItem(item);

                basketVBox.getChildren().add(itemCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void initialize() {
        BasketList.setListener(this);
        displayItems();
    }

    @FXML
    private void handleCatalogue() {
        navigate("/com/example/fx/Catalogue.fxml");
    }

    @FXML
    private void handleCheckout() {
        navigate("/com/example/fx/Checkout.fxml");
    }

    private void navigate(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) basketVBox.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
