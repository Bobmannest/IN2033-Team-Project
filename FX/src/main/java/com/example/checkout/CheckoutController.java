package com.example.checkout;


import com.example.basket.BasketList;
import com.example.catalogue.CatalogueDatabase;
import com.example.catalogue.CatalogueItem;
import com.example.catalogue.CatalogueItemController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.util.List;


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

        double totalCost = 0;
        for (CatalogueItem item : BasketList.getBasketItems()) {
            totalCost += item.getPackage_cost();
        }
        totalLabel.setText("£" + String.format("%.2f", totalCost));
    }

    //Checks if card numbers are valid based on Luhn's Algorithm
    private boolean isCardValid(String cardNum) {
        if (!cardNum.matches("\\d{13,19}")) {return false;}

        char[] cardDigits = cardNum.toCharArray();
        int sum = 0;
        boolean shouldDoubleDigit = false;

        for (int i = cardDigits.length - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardDigits[i]);
            if (shouldDoubleDigit) {
                digit *= 2;
                if (digit > 9) {digit -= 9;}
            }
            sum += digit;
            shouldDoubleDigit = !shouldDoubleDigit;
        }

        if (sum % 10 == 0) {return true;}
        return false;
    }

    @FXML
    private void handleCheckoutValidation() {
        boolean correctCustomerInfo = true;
        boolean correctPaymentInfo = true;


        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();
        boolean isGuest = guestCheckbox.isSelected();
        String cardNum = cardNumField.getText().trim();
        String expiry = expiryField.getText().trim();
        String cvv = cvvField.getText().trim();

        if (name.isEmpty() || (!email.contains("@") || !email.contains(".")) || address.isEmpty()) {
            correctCustomerInfo = false;
        }

        if (!isCardValid(cardNum) || !expiry.matches("\\d{2}/\\d{2}") || !cvv.matches("\\d{3}")) {
            correctPaymentInfo = false;
        }

        if (correctCustomerInfo && correctPaymentInfo) {
            handleOrderConfirmation();
        } else if (!correctCustomerInfo && !correctPaymentInfo) {
            checkoutErrorLabel.setText("At least one of the entered customer and payment info is incorrect");
        } else if (!correctCustomerInfo) {
            checkoutErrorLabel.setText("At least one of the entered customer info is incorrect");
        } else if (!correctPaymentInfo) {
            checkoutErrorLabel.setText("At least one of the entered payment info is incorrect");
        }
    }


    @FXML
    private void handleOrders() {
        navigate("/com/example/fx/OrderHistory.fxml", 800, 600);
    }

    @FXML
    private void handleCatalogue() {
        navigate("/com/example/fx/Catalogue.fxml", 800, 600);
    }

    @FXML
    private void handleAccount() {
        navigate("/com/example/fx/Account.fxml", 800, 600);
    }

    @FXML
    private void handleBasket() {
        navigate("/com/example/fx/Basket.fxml", 820, 633);
    }

    @FXML
    private void handleHome() {
        navigate("/com/example/fx/Home.fxml", 900, 650);
    }

    @FXML
    private void handleCreatePromotion() {
        navigate("/com/example/fx/Promotion.fxml", 900, 650);
    }

    @FXML
    private void handleManagePromotions() {
        navigate("/com/example/fx/CampaignItem.fxml", 750, 500);
    }

    @FXML
    private void handleOrderConfirmation() {
        navigate("/com/example/fx/OrderConfirmation.fxml", 800, 650);
    }

    private void navigate(String fxml, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Scene scene = new Scene(loader.load(), width, height);
            Stage stage = (Stage) checkoutPane.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
