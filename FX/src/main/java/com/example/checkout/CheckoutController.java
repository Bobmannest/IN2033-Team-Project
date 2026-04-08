package com.example.checkout;


import com.example.basket.BasketList;
import com.example.catalogue.CatalogueItem;
import com.example.fx.Member;
import com.example.fx.Session;
import com.example.order_confirmation.OrderConfirmationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;


public class CheckoutController {
    @FXML private BorderPane checkoutPane;
    @FXML private VBox basketVBox;
    @FXML private Label checkoutErrorLabel;

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private CheckBox guestCheckbox;
    @FXML private TextField cardNumField;
    @FXML private TextField expiryField;
    @FXML private TextField cvvField;

    @FXML private Label subtotalLabel;
    @FXML private Label discountLabel;
    @FXML private Label totalLabel;

    public void displayItems() {
        basketVBox.getChildren().clear();

        for (CatalogueItem item : BasketList.getBasketItems()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/CheckoutItemBox.fxml"));
                HBox itemCard = loader.load();

                CheckoutItemController itemCtrl = loader.getController();
                itemCtrl.setItem(item);

                basketVBox.getChildren().add(itemCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void initialize() {
        displayItems();

        //Calculates total
        double totalCost = 0;
        for (CatalogueItem item : BasketList.getBasketItems()) {
            totalCost += item.getPackage_cost();
        }

        //Calculates discounts and displays text
        subtotalLabel.setText("£" + String.format("%.2f", totalCost));
        Member member = Session.getMember();
        if (member != null && member.getOrderCount() % 10 == 9) {
            discountLabel.setText("-10%");
            totalCost *= 0.9;
        }
        totalLabel.setText("£" + String.format("%.2f", totalCost));
    }

    private boolean processPaymentViaAPI(String cardNum, String expiry, String cvv) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String json = String.format(
                    "{\"card_number\":\"%s\",\"expiry_date\":\"%s\",\"cvv\":\"%s\"}",
                    cardNum, expiry, cvv
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/payments/process"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;

        } catch (Exception e) {
            checkoutErrorLabel.setText("Could not connect to payment service.");
            return false;
        }
    }

    @FXML
    private void handleCheckoutValidation() {
        if (BasketList.getBasketItems().isEmpty()) {
            checkoutErrorLabel.setText("Your Basket is empty! Add more items to cart");
        } else {
            boolean correctCustomerInfo = true;
            boolean correctPaymentInfo = true;

            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressField.getText().trim();
            String cardNum = cardNumField.getText().trim();
            String expiry = expiryField.getText().trim();
            String cvv = cvvField.getText().trim();

            if (name.isEmpty() || (!email.contains("@") || !email.contains(".")) || address.isEmpty()) {
                correctCustomerInfo = false;
            }

            if (!processPaymentViaAPI(cardNum, expiry, cvv)) {
                correctPaymentInfo = false;
            }

            if (correctCustomerInfo && correctPaymentInfo) {
                handleOrderConfirmation(name, email, address);
            } else if (!correctCustomerInfo && !correctPaymentInfo) {
                checkoutErrorLabel.setText("At least one of the entered customer and payment info is incorrect");
            } else if (!correctCustomerInfo) {
                checkoutErrorLabel.setText("At least one of the entered customer info is incorrect");
            } else if (!correctPaymentInfo) {
                checkoutErrorLabel.setText("At least one of the entered payment info is incorrect");
            }
        }
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

    @FXML
    private void handleOrderConfirmation(String name, String email, String address) {
        String trackId = UUID.randomUUID().toString();

        try {
            String url = "http://localhost:8080/api/emails/sendPurchase"
                    + "?recipientName=" + name
                    + "&recipientEmail=" + email
                    + "&recipientAddress=" + address
                    + "&trackId=" + trackId;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Calculates total
        double totalCost = 0;
        for (CatalogueItem item : BasketList.getBasketItems()) {
            totalCost += item.getPackage_cost();
        }

        //Calculates discounts
        Member member = Session.getMember();
        if (member != null && member.getOrderCount() % 10 == 9) {
            totalCost *= 0.9;
        }

        //Increments current member order count by 1
        if (member != null) {
            member.setOrderCount(member.getOrderCount() + 1);
        }
        BasketList.clear();

        //Load OrderConfirmation
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/OrderConfirmation.fxml"));
            Parent root = loader.load();

            OrderConfirmationController controller = loader.getController();
            controller.displayOrderDetails("123", totalCost, email, address);

            Stage stage = (Stage) checkoutPane.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Records order and updates CA available stock

    }

    private void navigate(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) checkoutPane.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
