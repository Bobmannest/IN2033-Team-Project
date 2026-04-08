package com.example.catalogue;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
        navigate("/com/example/fx/OrderHistory.fxml");
    }

    // navigates to the account screen when the account button is clicked
    @FXML
    private void handleAccount() {
        navigate("/com/example/fx/Account.fxml");
    }

    @FXML
    private void handleBasket() {
        navigate("/com/example/fx/Basket.fxml");
    }

    @FXML
    private void handleHome() {
        navigate("/com/example/fx/Home.fxml");
    }

    @FXML
    private void handleCreatePromotion() {
        navigate("/com/example/fx/Promotion.fxml");
    }

    @FXML
    private void handleManagePromotions() {
        navigate("/com/example/fx/CampaignItem.fxml");
    }

    private void navigate(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) catalogueVBox.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
