package com.example.reports;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ReportsHubController {

    @FXML private Label errorLabel;

    @FXML private void handleCampaignHits()    { navigate("/com/example/fx/CampaignHitsReport.fxml", 1100, 700); }
    @FXML private void handleSalesReport() { navigate("/com/example/fx/SalesReport.fxml", 1300, 750); }
    @FXML private void handleAdvertCampaigns() { navigate("/com/example/fx/AdvertCampaignsReport.fxml", 1300, 750); }

    @FXML private void handleHome()             { navigate("/com/example/fx/Home.fxml", 800, 600); }
    @FXML private void handleCatalogue()        { navigate("/com/example/fx/Catalogue.fxml", 800, 600); }
    @FXML private void handleOrders()           { navigate("/com/example/fx/OrderHistory.fxml", 800, 600); }
    @FXML private void handleAccount()          { navigate("/com/example/fx/Account.fxml", 800, 600); }
    @FXML private void handleBasket()           { navigate("/com/example/fx/Basket.fxml", 820, 633); }
    @FXML private void handleCreatePromotion()  { navigate("/com/example/fx/Promotion.fxml", 900, 650); }
    @FXML private void handleManagePromotions() { navigate("/com/example/fx/CampaignItem.fxml", 900, 650); }

    private void navigate(String fxml, int w, int h) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Scene scene = new Scene(loader.load(), w, h);
            Stage stage = (Stage) errorLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            errorLabel.setText("Navigation error: " + e.getMessage());
        }
    }
}