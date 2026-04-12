package com.example.catalogue;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.members.Session;
import com.example.members.Member;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.List;

public class CatalogueController {
    @FXML private VBox catalogueVBox;
    @FXML private Button btnCreatePromotion;
    @FXML private Button btnManagePromotions;
    @FXML private Button btnOrders;
    @FXML private Button btnAccount;
    @FXML private Button btnReports;
    @FXML private Button btnLogin;
    @FXML private Button btnLogout;

    public void displayItems() {
        catalogueVBox.getChildren().clear();

        List<CatalogueItem> items = CatalogueDatabase.getCatalogueItems();
        for (CatalogueItem item : items) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/catalogueItemBox.fxml"));
                HBox itemCard = loader.load();

                CatalogueItemController itemCtrl = loader.getController();
                itemCtrl.setItem(item);

                catalogueVBox.getChildren().add(itemCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void initialize() {
        CatalogueDatabase.setListener(this);
        displayItems();
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
        for (Button b : buttons) {
            b.setVisible(false);
            b.setManaged(false);
        }
    }

    private void show(Button... buttons) {
        for (Button b : buttons) {
            b.setVisible(true);
            b.setManaged(true);
        }
    }

    // navigates to the orders page
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
    private void handleReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/Reports.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) catalogueVBox.getScene().getWindow();
            stage.setWidth(1000);
            stage.setHeight(620);
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogin() { navigate("/com/example/fx/Login.fxml"); }

    @FXML
    private void handleLogout() {Session.setMember(null); navigate("/com/example/fx/Login.fxml");}

    private void navigate(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) catalogueVBox.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
