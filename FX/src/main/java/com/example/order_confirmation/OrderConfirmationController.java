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

public class OrderConfirmationController {
    @FXML private BorderPane orderConfirmationPane;
    @FXML private VBox orderItemsVBox;

    @FXML private Label orderIdLabel;
    @FXML private Label totalLabel;
    @FXML private Label emailLabel;
    @FXML private Label addressLabel;

    public void displayItems() {
        orderItemsVBox.getChildren().clear();

        List<CatalogueItem> items = BasketList.getBasketItems();
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
    public void initialize() {
        displayItems();
    }


    @FXML
    public void displayOrderDetails(String orderId, double total, String email, String address) {
        orderIdLabel.setText(orderId);
        totalLabel.setText("£" + total);
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
}
