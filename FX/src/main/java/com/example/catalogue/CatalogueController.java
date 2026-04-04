package com.example.catalogue;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CatalogueController {
    @FXML private VBox catalogueVBox;

    public void displayItems() {
        catalogueVBox.getChildren().clear();

        List<CatalogueItem> items = CatalogueDatabase.getCatalogueItems();
        for (CatalogueItem item : items) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/catalogueItemBox.fxml"));
                HBox itemCard = loader.load();

                CatalogueItemController itemCtrl = loader.getController();
                itemCtrl.setItem(item);

                catalogueVBox.getChildren().add(itemCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void initialize() {
        CatalogueDatabase.setListener(this);
        displayItems();
    }

    // navigates to the orders page
    @FXML
    private void handleOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/OrderHistory.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) catalogueVBox.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @FXML
    private void handleBasket() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Basket.fxml"));
            Scene scene = new Scene(loader.load(), 820, 633);
            Stage stage = (Stage) catalogueVBox.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHome() {
        // if home just returns to catalogue for now
    }

    @FXML
    private void handleCreatePromotion() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Promotion.fxml"));
            Scene scene = new Scene(loader.load(), 900, 650);
            Stage stage = (Stage) catalogueVBox.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManagePromotions() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/CampaignItem.fxml"));
            Scene scene = new Scene(loader.load(), 750, 500);
            Stage stage = (Stage) catalogueVBox.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
