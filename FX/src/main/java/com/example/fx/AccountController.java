package com.example.fx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
            Scene scene = new Scene(loader.load(), 600, 400);
            Stage stage = (Stage) accountNoLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCatalogue() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Catalogue.fxml"));
            Scene scene = new Scene(loader.load(), 905, 633);
            Stage stage = (Stage) accountNoLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBasket() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/Basket.fxml"));
            Scene scene = new Scene(loader.load(), 820, 633);
            Stage stage = (Stage) accountNoLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/OrderHistory.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) accountNoLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCreatePromotion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/Promotion.fxml"));
            Scene scene = new Scene(loader.load(), 900, 650);
            Stage stage = (Stage) accountNoLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManagePromotions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/CampaignItem.fxml"));
            Scene scene = new Scene(loader.load(), 900, 650);
            Stage stage = (Stage) accountNoLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/Home.fxml"));
            Scene scene = new Scene(loader.load(), 900, 650);
            Stage stage = (Stage) accountNoLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}