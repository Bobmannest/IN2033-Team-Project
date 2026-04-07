package com.example.order_confirmation;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class OrderConfirmationController {
    @FXML
    private BorderPane orderConfirmationPane;

    @FXML
    private void handleOrders() {
        navigate("/com/example/fx/OrderHistory.fxml", 800, 600);
    }

    @FXML
    private void handleCatalogue() {
        navigate("/com/example/fx/Catalogue.fxml", 905, 633);
    }

    @FXML
    private void handleAccount() {
        navigate("/com/example/fx/Account.fxml", 800, 600);
    }

    @FXML
    private void handleBasket() {
        navigate("/com/example/fx/Basket.fxml", 820, 633);
    }

    @FXML
    private void handleHome() {
        navigate("/com/example/fx/Home.fxml", 900, 650);
    }

    @FXML
    private void handleCreatePromotion() {
        navigate("/com/example/fx/Promotion.fxml", 900, 650);
    }

    @FXML
    private void handleManagePromotions() {
        navigate("/com/example/fx/CampaignItem.fxml", 750, 500);
    }

    @FXML
    private void handleOrderConfirmation() {
        navigate("/com/example/fx/OrderConfirmation.fxml", 750, 500);
    }

    private void navigate(String fxml, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Scene scene = new Scene(loader.load(), width, height);
            Stage stage = (Stage) orderConfirmationPane.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
