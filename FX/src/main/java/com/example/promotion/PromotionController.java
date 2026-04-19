package com.example.promotion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.example.members.Member;
import com.example.members.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// controller for promotion/campaign creation screen

public class PromotionController {

    @FXML private TextField campaignIdField;
    @FXML private TextField campaignNameField;
    @FXML private TextArea campaignDescriptionArea;
    @FXML private DatePicker startDatePicker;
    @FXML private TextField startTimeField;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField endTimeField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private ComboBox<String> discountModeComboBox;
    @FXML private TextField defaultDiscountField;
    @FXML private TextField createdByField;
    @FXML private Label statusLabel;
    @FXML private Button btnManagePromotions;
    @FXML private Button btnOrders;
    @FXML private Button btnAccount;
    @FXML private Button btnReports;
    @FXML private Button btnLogin;
    @FXML private Button btnLogout;

    private final PromotionService promotionService = new PromotionService();

    // initialises the different boxes used on the form

    @FXML
    public void initialize() {
        statusComboBox.getItems().addAll("scheduled", "active", "cancelled", "expired");
        discountModeComboBox.getItems().addAll("fixed", "variable");
        statusComboBox.setValue("scheduled");
        discountModeComboBox.setValue("fixed");

        setNextCampaignId();
        setCreatedByFromSession();
        setupNavBar();
    }

    // reads the details entered on the create promotion/campaign form and saves a new campaign if valid

    @FXML
    private void handleCreateCampaign() {
        try {
            PromotionCampaign campaign = buildCampaignFromForm();
            promotionService.createCampaign(campaign);
            showStatus("Campaign created successfully.", true);
            clearForm();
        } catch (IllegalArgumentException e) {
            showStatus(e.getMessage(), false);
        } catch (SQLException e) {
            showStatus("Database error: " + e.getMessage(), false);
        } catch (Exception e) {
            showStatus("Error: " + e.getMessage(), false);
        }
    }

    // generates the next campaign ID in the form so the user does not have to enter manually

    private void setNextCampaignId() {
        try {
            String nextId = PromotionDAO.generateNextCampaignId();
            campaignIdField.setText(nextId);
            campaignIdField.setEditable(false);
            campaignIdField.setFocusTraversable(false);
        } catch (Exception e) {
            showStatus("Could not generate campaign ID.", false);
        }
    }

    private void setCreatedByFromSession() {
        Member member = Session.getMember();

        if (member != null) {
            createdByField.setText(member.getAccountNo());
            createdByField.setEditable(false);
            createdByField.setFocusTraversable(false);
        } else {
            createdByField.setText("");
        }
    }

    // creates a PromotionCampaign object from what the user put in the form

    private PromotionCampaign buildCampaignFromForm() throws SQLException {
        String campaignId = PromotionDAO.generateNextCampaignId();
        String campaignName = campaignNameField.getText().trim();
        String description = campaignDescriptionArea.getText().trim();

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Please choose both start and end dates.");
        }

        String startTimeText = startTimeField.getText().trim();
        String endTimeText = endTimeField.getText().trim();

        if (startTimeText.isEmpty() || endTimeText.isEmpty()) {
            throw new IllegalArgumentException("Please enter both start and end times in HH:mm format.");
        }

        LocalTime startTime;
        LocalTime endTime;

        try {
            startTime = LocalTime.parse(startTimeText);
            endTime = LocalTime.parse(endTimeText);
        } catch (Exception e) {
            throw new IllegalArgumentException("Time must be in HH:mm format.");
        }

        Double defaultDiscount = null;
        String discountText = defaultDiscountField.getText().trim();
        if (!discountText.isEmpty()) {
            try {
                defaultDiscount = Double.parseDouble(discountText);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Default discount must be a valid number.");
            }
        }

        Member member = Session.getMember();
        String createdBy = (member != null) ? member.getAccountNo() : null;

        return new PromotionCampaign(
                campaignId,
                campaignName,
                description,
                LocalDateTime.of(startDate, startTime),
                LocalDateTime.of(endDate, endTime),
                statusComboBox.getValue(),
                discountModeComboBox.getValue(),
                defaultDiscount,
                createdBy.isEmpty() ? null : createdBy
        );
    }

    // puts clears the form after the promotion/campaign is created or the user manually clears

    @FXML
    private void clearForm() {
        campaignNameField.clear();
        campaignDescriptionArea.clear();
        startDatePicker.setValue(null);
        startTimeField.clear();
        endDatePicker.setValue(null);
        endTimeField.clear();
        defaultDiscountField.clear();
        setCreatedByFromSession();
        statusComboBox.setValue("scheduled");
        discountModeComboBox.setValue("fixed");
        setNextCampaignId();
    }

    private void showStatus(String message, boolean success) {
        statusLabel.setStyle(success ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
        statusLabel.setText(message);
    }

    // takes you to home

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) campaignIdField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showStatus("Could not open home.", false);
        }
    }

    // takes you to catalogue

    @FXML
    private void handleCatalogue() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Catalogue.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) campaignIdField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showStatus("Could not open catalogue.", false);
        }
    }

    // takes you to manage promotions

    @FXML
    private void handleManagePromotions() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/CampaignItem.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) campaignIdField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showStatus("Could not open manage promotions screen.", false);
        }
    }

    // takes you to orders

    @FXML
    private void handleOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/OrderHistory.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) campaignIdField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showStatus("Could not open orders screen.", false);
        }
    }

    // takes you to account

    @FXML
    private void handleAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Account.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) campaignIdField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showStatus("Could not open account screen.", false);
        }
    }

    // takes you to basket

    @FXML
    private void handleBasket() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/Basket.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) campaignIdField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // navbar setup

    private void setupNavBar() {
        Member member = Session.getMember();
        if (member == null) {
            hide(btnManagePromotions, btnOrders, btnAccount, btnReports, btnLogout);
            show(btnLogin);
        } else if (member.getMemberType().equals("admin")) {
            show(btnManagePromotions, btnOrders, btnAccount, btnReports, btnLogout);
            hide(btnLogin);
        } else {
            hide(btnManagePromotions, btnReports, btnLogin);
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

    // takes you to active promotions

    @FXML
    private void handleActivePromotions() {
        navigate("/com/example/fx/ActivePromotions.fxml");
    }

    // takes you to reports

    @FXML
    private void handleReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/Reports.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) campaignIdField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showStatus("Navigation error: " + e.getMessage(), false);
        }
    }

    // login, logout screens

    @FXML
    private void handleLogin() {
        navigate("/com/example/fx/Login.fxml");
    }

    @FXML
    private void handleLogout() {
        Session.setMember(null);
        navigate("/com/example/fx/Login.fxml");
    }

    private void navigate(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) campaignIdField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showStatus("Navigation error: " + e.getMessage(), false);
        }
    }
}