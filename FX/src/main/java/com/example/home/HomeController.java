package com.example.home;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import com.example.members.Session;
import com.example.members.Member;
import javafx.scene.control.Button;

public class HomeController {
    @FXML private BorderPane homePane;
    @FXML private Button btnCreatePromotion;
    @FXML private Button btnManagePromotions;
    @FXML private Button btnOrders;
    @FXML private Button btnAccount;
    @FXML private Button btnReports;
    @FXML private Button btnLogin;
    @FXML private Button btnLogout;

    @FXML
    public void initialize() {
        setupNavBar();
    }

    private void setupNavBar() {
        Member member = Session.getMember();
        if (member == null) {
            hide(btnCreatePromotion, btnManagePromotions, btnOrders, btnAccount, btnReports, btnLogout);
            show(btnLogin);
        } else if (member.getMemberType().equals("admin")) {
            show(btnCreatePromotion, btnManagePromotions, btnOrders, btnAccount, btnReports, btnLogout);
            hide(btnLogin);
        } else {
            hide(btnCreatePromotion, btnManagePromotions, btnReports, btnLogin);
            show(btnOrders, btnAccount, btnLogout);
        }
    }

    private void hide(Button... buttons) {
        for (Button b : buttons) { b.setVisible(false); b.setManaged(false); }
    }

    private void show(Button... buttons) {
        for (Button b : buttons) { b.setVisible(true); b.setManaged(true); }
    }

    @FXML
    private void handleCatalogue() {
        navigate("/com/example/fx/Catalogue.fxml");
    }

    @FXML
    private void handleAccount() {
        navigate("/com/example/fx/Account.fxml");
    }

    @FXML
    private void handleOrders() {
        navigate("/com/example/fx/OrderHistory.fxml");
    }

    @FXML
    private void handleBasket() {
        navigate("/com/example/fx/Basket.fxml");
    }

    @FXML
    private void handleCreatePromotion() {
        navigate("/com/example/fx/Promotion.fxml");
    }

    @FXML
    private void handleManagePromotions() {
        navigate("/com/example/fx/CampaignItem.fxml");
    }

    @FXML
    private void handleActivePromotions() { navigate("/com/example/fx/ActivePromotions.fxml"); }

    @FXML
    private void handleLogin() { navigate("/com/example/fx/Login.fxml"); }

    @FXML
    private void handleLogout() { Session.setMember(null); navigate("/com/example/fx/Login.fxml"); }

    @FXML
    private void handleReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/Reports.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) homePane.getScene().getWindow();
            stage.setWidth(1000);
            stage.setHeight(620);
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void navigate(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) homePane.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
