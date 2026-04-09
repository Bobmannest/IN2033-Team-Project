package com.example.active_promotions;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ActivePromotionsController {
    @FXML private BorderPane ActivePromotionsPane;
    @FXML private FlowPane ActivePromotionsFlowPane;

    @FXML
    public void initialize() {
        testDisplayItems();
    }

    /*
    public void displayItems() {
        ActivePromotionsFlowPane.getChildren().clear();

        for (ActivePromotionItem item : ActivePromotionsList.getItems()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/ActivePromotionItem.fxml"));
                javafx.scene.layout.HBox itemCard = loader.load();

                ActivePromotionItemController itemCtrl = loader.getController();
                itemCtrl.setItem(item);

                flowPane.getChildren().add(itemCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

     */

    public void testDisplayItems() {
        ActivePromotionsFlowPane.getChildren().clear();

        for (int i = 0; i < 11; i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/ActivePromotionItem.fxml"));
                VBox itemCard = loader.load();
                ActivePromotionsFlowPane.getChildren().add(itemCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
    private void handleCatalogue() {
        navigate("/com/example/fx/Catalogue.fxml");
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
    private void handleActivePromotions() {
        navigate("/com/example/fx/ActivePromotions.fxml");
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
            Stage stage = (Stage) ActivePromotionsPane.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
