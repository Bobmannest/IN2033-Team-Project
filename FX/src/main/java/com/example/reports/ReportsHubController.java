package com.example.reports;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.example.members.Member;
import com.example.members.Session;

import java.io.IOException;

public class ReportsHubController {

    @FXML private Label errorLabel;
    @FXML private Button btnCreatePromotion;
    @FXML private Button btnManagePromotions;
    @FXML private Button btnOrders;
    @FXML private Button btnAccount;
    @FXML private Button btnLogin;
    @FXML private Button btnLogout;

    @FXML
    public void initialize() {
        setupNavBar();
    }

    private void setupNavBar() {
        Member member = Session.getMember();
        if (member == null) {
            hide(btnCreatePromotion, btnManagePromotions, btnOrders, btnAccount, btnLogout);
            show(btnLogin);
        } else if (member.getMemberType().equals("admin")) {
            show(btnCreatePromotion, btnManagePromotions, btnOrders, btnAccount, btnLogout);
            hide(btnLogin);
        } else {
            hide(btnCreatePromotion, btnManagePromotions, btnLogin);
            show(btnOrders, btnAccount, btnLogout);
        }
    }

    private void hide(Button... buttons) {
        for (Button b : buttons) { b.setVisible(false); b.setManaged(false); }
    }

    private void show(Button... buttons) {
        for (Button b : buttons) { b.setVisible(true); b.setManaged(true); }
    }

    @FXML private void handleCampaignHits()    { navigate("/com/example/fx/CampaignHitsReport.fxml", 1100, 700); }
    @FXML private void handleSalesReport()     { navigate("/com/example/fx/SalesReport.fxml", 1300, 750); }
    @FXML private void handleAdvertCampaigns() { navigate("/com/example/fx/AdvertCampaignsReport.fxml", 1300, 750); }

    @FXML private void handleHome()             { navigate("/com/example/fx/Home.fxml", 800, 600); }
    @FXML private void handleCatalogue()        { navigate("/com/example/fx/Catalogue.fxml", 800, 600); }
    @FXML private void handleActivePromotions() { navigate("/com/example/fx/ActivePromotions.fxml", 900, 633); }
    @FXML private void handleOrders()           { navigate("/com/example/fx/OrderHistory.fxml", 800, 600); }
    @FXML private void handleAccount()          { navigate("/com/example/fx/Account.fxml", 800, 600); }
    @FXML private void handleBasket()           { navigate("/com/example/fx/Basket.fxml", 820, 633); }
    @FXML private void handleCreatePromotion()  { navigate("/com/example/fx/Promotion.fxml", 900, 650); }
    @FXML private void handleManagePromotions() { navigate("/com/example/fx/CampaignItem.fxml", 900, 650); }
    @FXML private void handleLogin()            { navigate("/com/example/fx/Login.fxml", 800, 600); }
    @FXML private void handleLogout()           { Session.setMember(null); navigate("/com/example/fx/Login.fxml", 800, 600); }

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