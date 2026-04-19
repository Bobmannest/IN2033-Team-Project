package com.example.active_promotions;

import com.example.members.Member;
import com.example.members.Session;
import com.example.promotion.PromotionCampaign;
import com.example.promotion.PromotionService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ActivePromotionsController {

    @FXML private BorderPane ActivePromotionsPane;
    @FXML private FlowPane ActivePromotionsFlowPane;
    @FXML private Button btnCreatePromotion;
    @FXML private Button btnManagePromotions;
    @FXML private Button btnOrders;
    @FXML private Button btnAccount;
    @FXML private Button btnLogin;
    @FXML private Button btnLogout;
    @FXML private Button btnReports;

    private final PromotionService promotionService = new PromotionService();

    @FXML
    public void initialize() {
        displayActiveCampaigns();
        setupNavBar();
    }

    //Displays campaigns to the Active Campaigns page
    private void displayActiveCampaigns() {
        ActivePromotionsFlowPane.getChildren().clear();

        try {
            //Creates list of all active campaigns
            List<PromotionCampaign> campaigns = promotionService.getActiveCampaigns();
            System.out.println("Active campaigns found: " + campaigns.size());

            for (PromotionCampaign campaign : campaigns) {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/example/fx/ActivePromotionItem.fxml")
                );

                VBox itemCard = loader.load();

                ActivePromotionItemController itemController = loader.getController();
                itemController.setCampaign(campaign);

                ActivePromotionsFlowPane.getChildren().add(itemCard);
            }

            System.out.println("Cards added: " + ActivePromotionsFlowPane.getChildren().size());

        } catch (Exception e) {
            System.out.println("Error loading active promotions:");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOrders() {
        navigate("/com/example/fx/OrderHistory.fxml");
    }

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
    private void handleCreatePromotion() {
        navigate("/com/example/fx/Promotion.fxml");
    }

    @FXML
    private void handleManagePromotions() {
        navigate("/com/example/fx/CampaignItem.fxml");
    }

    @FXML
    private void handleLogin() {
        navigate("/com/example/fx/Login.fxml");
    }

    @FXML
    private void handleLogout() {
        Session.setMember(null);
        navigate("/com/example/fx/Login.fxml");
    }

    @FXML
    private void handleReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/Reports.fxml"));
            javafx.scene.Parent root = loader.load();
            Stage stage = (Stage) ActivePromotionsPane.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void setupNavBar() {
        Member member = Session.getMember();

        if (member == null) {
            hide(btnCreatePromotion, btnManagePromotions, btnOrders, btnAccount, btnReports, btnLogout);
            show(btnLogin);
        } else if ("admin".equalsIgnoreCase(member.getMemberType())) {
            show(btnCreatePromotion, btnManagePromotions, btnOrders, btnAccount, btnReports, btnLogout);
            hide(btnLogin);
        } else {
            hide(btnCreatePromotion, btnManagePromotions, btnReports, btnLogin);
            show(btnOrders, btnAccount, btnLogout);
        }
    }

    private void hide(Button... buttons) {
        for (Button button : buttons) {
            button.setVisible(false);
            button.setManaged(false);
        }
    }

    private void show(Button... buttons) {
        for (Button button : buttons) {
            button.setVisible(true);
            button.setManaged(true);
        }
    }
}