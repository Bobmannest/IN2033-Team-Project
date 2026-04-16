package com.example.order_confirmation;


import com.example.basket.BasketItemController;
import com.example.basket.BasketList;
import com.example.catalogue.CatalogueItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.util.List;
import javafx.scene.control.Button;

public class OrderConfirmationController {
    @FXML private BorderPane orderConfirmationPane;
    @FXML private VBox orderItemsVBox;

    @FXML private Label orderIdLabel;
    @FXML private Label totalLabel;
    @FXML private Label emailLabel;
    @FXML private Label addressLabel;
    @FXML private Button btnCreatePromotion;
    @FXML private Button btnManagePromotions;
    @FXML private Button btnReports;
    @FXML private Button btnLogin;
    @FXML private Button btnLogout;
    @FXML private Button btnOrders;
    @FXML private Button btnAccount;

    public void displayItems(List<CatalogueItem> items) {
        orderItemsVBox.getChildren().clear();

        for (CatalogueItem item : items) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/OrderConfirmationItemBox.fxml"));
                HBox itemCard = loader.load();

                OrderConfirmationItemController itemCtrl = loader.getController();
                itemCtrl.setItem(item);

                orderItemsVBox.getChildren().add(itemCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    public void displayOrderDetails(String orderId, double total, String email, String address) {
        String roundedTotal = String.format("%.2f", total);

        orderIdLabel.setText(orderId);
        totalLabel.setText("£" + roundedTotal);
        emailLabel.setText(email);
        addressLabel.setText(address);
    }

    @FXML
    private void handleOrders() {
        navigate("/com/example/fx/OrderHistory.fxml");
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

    private void navigate(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) orderConfirmationPane.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        setupNavBar();
    }

    private void setupNavBar() {
        com.example.members.Member member = com.example.members.Session.getMember();
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
    private void handleReports() { navigate("/com/example/fx/Reports.fxml"); }

    @FXML
    private void handleActivePromotions() { navigate("/com/example/fx/ActivePromotions.fxml"); }

    @FXML
    private void handleLogin() { navigate("/com/example/fx/Login.fxml"); }

    @FXML
    private void handleLogout() {
        com.example.members.Session.setMember(null);
        navigate("/com/example/fx/Login.fxml");
    }
}
