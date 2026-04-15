package com.example.members;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

public class AccountController {

    @FXML private Label accountNoLabel;
    @FXML private Label emailLabel;
    @FXML private Label memberTypeLabel;
    @FXML private Label orderCountLabel;
    @FXML private Label accountNoLabel2;
    @FXML private Label memberTypeLabel2;
    @FXML private Button btnCreatePromotion;
    @FXML private Button btnManagePromotions;
    @FXML private Button btnOrders;
    @FXML private Button btnReports;

    @FXML
    public void initialize() {
        Member member = Session.getMember();

        if (member != null) {
            accountNoLabel.setText(member.getAccountNo());
            emailLabel.setText(member.getEmail());
            memberTypeLabel.setText(member.getMemberType());
            orderCountLabel.setText(String.valueOf(member.getOrderCount()));
            accountNoLabel2.setText(member.getAccountNo());
            memberTypeLabel2.setText(member.getMemberType());
        }
        setupNavBar();
    }

    private void setupNavBar() {
        Member member = Session.getMember();
        if (member == null) {
            hide(btnCreatePromotion, btnManagePromotions, btnOrders, btnReports);
        } else if (member.getMemberType().equals("admin")) {
            show(btnCreatePromotion, btnManagePromotions, btnOrders, btnReports);
        } else {
            hide(btnCreatePromotion, btnManagePromotions, btnReports);
            show(btnOrders);
        }
    }

    private void hide(Button... buttons) {
        for (Button b : buttons) { b.setVisible(false); b.setManaged(false); }
    }

    private void show(Button... buttons) {
        for (Button b : buttons) { b.setVisible(true); b.setManaged(true); }
    }

    @FXML
    private void handleLogout() {
        try {
            Session.setMember(null);

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Login.fxml"));
            javafx.scene.Parent root = loader.load();
            Stage stage = (Stage) accountNoLabel.getScene().getWindow();
            boolean wasMaximized = stage.isMaximized();
            stage.getScene().setRoot(root);
            if (wasMaximized) stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private void handleOrders() {
        navigate("/com/example/fx/OrderHistory.fxml");
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
    private void handleHome() {
        navigate("/com/example/fx/Home.fxml");
    }

    @FXML
    private void handleActivePromotions() { navigate("/com/example/fx/ActivePromotions.fxml"); }
    
    private void navigate(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) accountNoLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/Reports.fxml"));
            javafx.scene.Parent root = loader.load();
            Stage stage = (Stage) accountNoLabel.getScene().getWindow();
            boolean wasMaximized = stage.isMaximized();
            stage.getScene().setRoot(root);
            if (wasMaximized) stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}