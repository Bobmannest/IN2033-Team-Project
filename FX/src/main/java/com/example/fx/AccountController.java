package com.example.fx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AccountController {

    @FXML private Label accountNoLabel;
    @FXML private Label emailLabel;
    @FXML private Label memberTypeLabel;
    @FXML private Label orderCountLabel;
    @FXML private Label accountNoLabel2;
    @FXML private Label memberTypeLabel2;

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
    }

    @FXML
    private void handleLogout() {
        try {
            Session.setMember(null);

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) accountNoLabel.getScene().getWindow();
            stage.setScene(scene);
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
}