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

    @FXML private void handleCampaignHits()    { navigate("/com/example/fx/CampaignHitsReport.fxml"); }
    @FXML private void handleSalesReport()     { navigate("/com/example/fx/SalesReport.fxml"); }
    @FXML private void handleAdvertCampaigns() { navigate("/com/example/fx/AdvertCampaignsReport.fxml"); }

    @FXML private void handleHome()             { navigate("/com/example/fx/Home.fxml"); }
    @FXML private void handleCatalogue()        { navigate("/com/example/fx/Catalogue.fxml"); }
    @FXML private void handleActivePromotions() { navigate("/com/example/fx/ActivePromotions.fxml"); }
    @FXML private void handleOrders()           { navigate("/com/example/fx/OrderHistory.fxml"); }
    @FXML private void handleAccount()          { navigate("/com/example/fx/Account.fxml"); }
    @FXML private void handleBasket()           { navigate("/com/example/fx/Basket.fxml"); }
    @FXML private void handleCreatePromotion()  { navigate("/com/example/fx/Promotion.fxml"); }
    @FXML private void handleManagePromotions() { navigate("/com/example/fx/CampaignItem.fxml"); }
    @FXML private void handleLogin()            { navigate("/com/example/fx/Login.fxml"); }
    @FXML private void handleLogout()           { Session.setMember(null); navigate("/com/example/fx/Login.fxml"); }

    private void navigate(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            javafx.scene.Parent root = loader.load();
            Stage stage = (Stage) errorLabel.getScene().getWindow();
            boolean wasMaximized = stage.isMaximized();
            stage.getScene().setRoot(root);
            if (wasMaximized) stage.setMaximized(true);
        } catch (IOException e) {
            errorLabel.setText("Navigation error: " + e.getMessage());
        }
    }
}