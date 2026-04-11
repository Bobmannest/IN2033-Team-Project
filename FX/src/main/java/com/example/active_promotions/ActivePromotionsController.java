package com.example.active_promotions;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.members.Member;
import com.example.members.Session;
import javafx.scene.control.Button;

import java.io.IOException;

public class ActivePromotionsController {
    @FXML private BorderPane ActivePromotionsPane;
    @FXML private FlowPane ActivePromotionsFlowPane;
    @FXML private Button btnCreatePromotion;
    @FXML private Button btnManagePromotions;
    @FXML private Button btnOrders;
    @FXML private Button btnAccount;
    @FXML private Button btnLogin;
    @FXML private Button btnLogout;

    @FXML
    public void initialize() {
        testDisplayItems();
        setupNavBar();
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

    @FXML
    private void handleLogin() { navigate("/com/example/fx/Login.fxml"); }

    @FXML
    private void handleLogout() {Session.setMember(null); navigate("/com/example/fx/Login.fxml");}

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
}
