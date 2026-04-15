package com.example.checkout;


import com.example.basket.BasketList;
import com.example.catalogue.CatalogueItem;
import com.example.fx.DatabaseConnection;
import com.example.members.Member;
import com.example.members.Session;
import com.example.order_confirmation.OrderConfirmationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javafx.scene.control.Button;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Timestamp;



public class CheckoutController {
    @FXML private BorderPane checkoutPane;
    @FXML private VBox basketVBox;
    @FXML private Label checkoutErrorLabel;

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;

    @FXML private ComboBox<String> cardTypeBox;
    @FXML private TextField cardNumField;
    @FXML private TextField expiryField;
    @FXML private TextField cvvField;

    @FXML private Label subtotalLabel;
    @FXML private Label discountLabel;
    @FXML private Label totalLabel;

    @FXML private Button btnCreatePromotion;
    @FXML private Button btnManagePromotions;
    @FXML private Button btnOrders;
    @FXML private Button btnAccount;
    @FXML private Button btnReports;
    @FXML private Button btnLogin;
    @FXML private Button btnLogout;

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

        //Card type listener
        cardTypeBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;
            switch (newVal) {
                case "Visa", "Mastercard" -> {
                    cardNumField.setPromptText("XXXX XXXX XXXX XXXX");
                    cvvField.setPromptText("XXX");
                }
                case "AmEx" -> {
                    cardNumField.setPromptText("XXXX XXXXXX XXXXX");
                    cvvField.setPromptText("XXXX");
                }
            }
        });
        setupNavBar();
    }

    private boolean processPaymentViaAPI(String cardType, String cardNum, String expiry, String cvv) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String json = String.format(
                    "{\"card_type\":\"%s\",\"card_number\":\"%s\",\"expiry_date\":\"%s\",\"cvv\":\"%s\"}",
                    cardType, cardNum, expiry, cvv
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

            String cardType = cardTypeBox.getValue();
            String cardNum = cardNumField.getText().trim().replace(" ", "");
            String expiry = expiryField.getText().trim();
            String cvv = cvvField.getText().trim();

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

            if (name.isEmpty() || (!email.contains("@") || !email.contains(".")) || address.isEmpty()) {
                correctCustomerInfo = false;
            }

            if (!processPaymentViaAPI(cardType, cardNum, expiry, cvv)) {
                correctPaymentInfo = false;
            }

            if (correctCustomerInfo && correctPaymentInfo) {
                handleOrderConfirmation(name, email, address, totalCost, member);

                String first4Digits = cardNum.substring(0, 4);
                String last4Digits = cardNum.substring(cardNum.length() - 1);
                recordPayment(cardType, first4Digits, last4Digits, cardType, expiry, totalCost);
            } else if (!correctCustomerInfo && !correctPaymentInfo) {
                checkoutErrorLabel.setText("At least one of the entered customer and payment info is incorrect");
            } else if (!correctCustomerInfo) {
                checkoutErrorLabel.setText("At least one of the entered customer info is incorrect");
            } else if (!correctPaymentInfo) {
                checkoutErrorLabel.setText("At least one of the entered payment info is incorrect");
            }
        }
    }

    private void recordPayment(String name, String first4Digits, String last4Digits, String cardType, String expiry, double amount) {
        System.out.println("Recording Payment...");
        String sql = """
            INSERT INTO Payments
            (name, card_first4, card_last4, card_expiry, card_type, amount)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, first4Digits);
            ps.setString(3, last4Digits);
            ps.setString(4, expiry);
            ps.setString(5, cardType);
            ps.setDouble(6, amount);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to record payment: " + e.getMessage());
            checkoutErrorLabel.setText("Could not connect to database. Please try again.");
        }
        System.out.println("Payment Successfully Recorded");
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
    private void handleActivePromotions() { navigate("/com/example/fx/ActivePromotions.fxml"); }

    @FXML
    private void handleReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/Reports.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) checkoutPane.getScene().getWindow();
            stage.setWidth(1000);
            stage.setHeight(620);
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogin() {navigate("/com/example/fx/Login.fxml"); }

    @FXML
    private void handleLogout() { Session.setMember(null); navigate("/com/example/fx/Login.fxml"); }

    @FXML
    private void handleOrderConfirmation(String name, String email, String address, double totalCost, Member member) {
        String trackId = UUID.randomUUID().toString();

        name = name.trim().replace(" ", "");
        email = email.trim().replace(" ", "");
        address = address.trim().replace(" ", "");

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

        //Increments current member order count by 1
        if (member != null) {
            member.setOrderCount(member.getOrderCount() + 1);
        }

        //Makes a copy of basket_items to send to controller to display items ordered
        List<CatalogueItem> orderConfirmationItems = new ArrayList<>(BasketList.getBasketItems());
        BasketList.clear();

        //Load OrderConfirmation
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/OrderConfirmation.fxml"));
            Parent root = loader.load();

            OrderConfirmationController controller = loader.getController();
            controller.displayOrderDetails("123", totalCost, email, address);
            controller.displayItems(orderConfirmationItems);

            Stage stage = (Stage) checkoutPane.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    // Hides/Shows nav buttons depending on the user's role
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


}
