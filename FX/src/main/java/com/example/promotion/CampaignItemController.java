package com.example.promotion;

import com.example.catalogue.CatalogueDatabase;
import com.example.catalogue.CatalogueItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.example.members.Member;
import com.example.members.Session;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CampaignItemController {

    @FXML private ListView<PromotionCampaign> campaignListView;
    @FXML private ListView<String> campaignItemsListView;
    @FXML private ComboBox<CatalogueItem> itemComboBox;
    @FXML private TextField itemDiscountField;
    @FXML private Label selectedCampaignLabel;
    @FXML private Label statusLabel;
    @FXML private Button btnCreatePromotion;
    @FXML private Button btnOrders;
    @FXML private Button btnAccount;
    @FXML private Button btnReports;
    @FXML private Button btnLogin;
    @FXML private Button btnLogout;

    private final PromotionService promotionService = new PromotionService();

    @FXML
    public void initialize() {
        configureCampaignList();
        configureItemComboBox();
        loadCampaigns();

        campaignListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedCampaignLabel.setText("Selected: " + newVal.getCampaignId() + " - " + newVal.getCampaignName());
                loadCampaignItems(newVal.getCampaignId());
            } else {
                selectedCampaignLabel.setText("No campaign selected");
                campaignItemsListView.getItems().clear();
            }
        });
        setupNavBar();
    }

    private void configureCampaignList() {
        campaignListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(PromotionCampaign campaign, boolean empty) {
                super.updateItem(campaign, empty);
                if (empty || campaign == null) {
                    setText(null);
                } else {
                    setText(campaign.getCampaignId() + " | " + campaign.getCampaignName() + " | " + campaign.getStatus());
                }
            }
        });
    }

    private void configureItemComboBox() {
        itemComboBox.getItems().setAll(CatalogueDatabase.getCatalogueItems());

        itemComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(CatalogueItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getItem_id() + " - " + item.getDescription());
                }
            }
        });

        itemComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(CatalogueItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Choose catalogue item");
                } else {
                    setText(item.getItem_id() + " - " + item.getDescription());
                }
            }
        });
    }

    @FXML
    private void handleRefreshCampaigns() {
        loadCampaigns();
        showStatus("Campaign list refreshed.", true);
    }

    @FXML
    private void handleAddItemToSelectedCampaign() {
        try {
            PromotionCampaign selectedCampaign = campaignListView.getSelectionModel().getSelectedItem();
            if (selectedCampaign == null) {
                throw new IllegalArgumentException("Please select a campaign first.");
            }

            CatalogueItem selectedItem = itemComboBox.getValue();
            if (selectedItem == null) {
                throw new IllegalArgumentException("Please select a catalogue item.");
            }

            Double itemDiscount = null;
            String discountText = itemDiscountField.getText().trim();
            if (!discountText.isEmpty()) {
                try {
                    itemDiscount = Double.parseDouble(discountText);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Item discount must be a valid number.");
                }
            }

            promotionService.addItemToCampaign(
                    selectedCampaign.getCampaignId(),
                    selectedItem.getItem_id(),
                    itemDiscount
            );

            loadCampaignItems(selectedCampaign.getCampaignId());
            itemDiscountField.clear();
            itemComboBox.setValue(null);

            showStatus("Item added successfully.", true);

        } catch (IllegalArgumentException e) {
            showStatus(e.getMessage(), false);
        } catch (SQLException e) {
            showStatus("Database error: " + e.getMessage(), false);
        }
    }

    @FXML
    private void handleDeleteCampaign() {
        try {
            PromotionCampaign selectedCampaign = campaignListView.getSelectionModel().getSelectedItem();
            if (selectedCampaign == null) {
                throw new IllegalArgumentException("Please select a campaign to delete.");
            }

            PromotionDAO.deleteCampaign(selectedCampaign.getCampaignId());
            loadCampaigns();
            campaignItemsListView.getItems().clear();
            selectedCampaignLabel.setText("No campaign selected");

            showStatus("Campaign deleted successfully.", true);

        } catch (IllegalArgumentException e) {
            showStatus(e.getMessage(), false);
        } catch (SQLException e) {
            showStatus("Database error: " + e.getMessage(), false);
        }
    }

    @FXML
    private void handleDeleteSelectedItem() {
        try {
            PromotionCampaign selectedCampaign = campaignListView.getSelectionModel().getSelectedItem();
            String selectedItemLine = campaignItemsListView.getSelectionModel().getSelectedItem();

            if (selectedCampaign == null) {
                throw new IllegalArgumentException("Please select a campaign first.");
            }

            if (selectedItemLine == null || selectedItemLine.isBlank()) {
                throw new IllegalArgumentException("Please select an item to delete.");
            }

            String itemId = selectedItemLine.split(" - ")[0];

            PromotionDAO.deleteItemFromCampaign(selectedCampaign.getCampaignId(), itemId);
            loadCampaignItems(selectedCampaign.getCampaignId());

            showStatus("Campaign item deleted successfully.", true);

        } catch (IllegalArgumentException e) {
            showStatus(e.getMessage(), false);
        } catch (SQLException e) {
            showStatus("Database error: " + e.getMessage(), false);
        }
    }

    @FXML
    private void handleCancelCampaign() {
        PromotionCampaign selected = campaignListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            statusLabel.setText("Please select a campaign first.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            String campaignId = selected.getCampaignId();

            PromotionService promotionService = new PromotionService();
            promotionService.cancelCampaign(campaignId);

            statusLabel.setText("Campaign cancelled successfully.");
            statusLabel.setStyle("-fx-text-fill: green;");

            handleRefreshCampaigns();

        } catch (Exception e) {
            statusLabel.setText("Error cancelling campaign: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void loadCampaigns() {
        try {
            List<PromotionCampaign> campaigns = PromotionDAO.getAllCampaigns();
            campaignListView.getItems().setAll(campaigns);
        } catch (SQLException e) {
            showStatus("Could not load campaigns: " + e.getMessage(), false);
        }
    }

    private void loadCampaignItems(String campaignId) {
        try {
            List<String> itemLines = PromotionDAO.getCampaignItemLines(campaignId);
            campaignItemsListView.getItems().setAll(itemLines);
        } catch (SQLException e) {
            showStatus("Could not load campaign items: " + e.getMessage(), false);
        }
    }

    private void showStatus(String message, boolean success) {
        statusLabel.setStyle(success ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
        statusLabel.setText(message);
    }

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showStatus("Could not open home.", false);
        }
    }

    @FXML
    private void handleCatalogue() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Catalogue.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showStatus("Could not open catalogue.", false);
        }
    }

    @FXML
    private void handleCreatePromotion() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Promotion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showStatus("Could not open create promotion screen.", false);
        }
    }

    @FXML
    private void handleOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/OrderHistory.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showStatus("Could not open create orders screen.", false);
        };
    }

    @FXML
    private void handleAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Account.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showStatus("Could not open account screen.", false);
        }
    }

    @FXML
    private void handleBasket() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fx/Basket.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showStatus("Could not open basket screen.", false);
        }
    }

    @FXML private void handleActivePromotions() { navigate("/com/example/fx/ActivePromotions.fxml"); }

    @FXML
    private void handleReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/Reports.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setWidth(1000);
            stage.setHeight(620);
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showStatus("Navigation error: " + e.getMessage(), false);
        }
    }

    @FXML private void handleLogin() { navigate("/com/example/fx/Login.fxml"); }
    @FXML private void handleLogout() { Session.setMember(null); navigate("/com/example/fx/Login.fxml"); }

    private void setupNavBar() {
        Member member = Session.getMember();
        if (member == null) {
            hide(btnCreatePromotion, btnOrders, btnAccount, btnReports, btnLogout);
            show(btnLogin);
        } else if (member.getMemberType().equals("admin")) {
            show(btnCreatePromotion, btnOrders, btnAccount, btnReports, btnLogout);
            hide(btnLogin);
        } else {
            hide(btnCreatePromotion, btnReports, btnLogin);
            show(btnOrders, btnAccount, btnLogout);
        }
    }

    private void hide(Button... buttons) {
        for (Button b : buttons) { b.setVisible(false); b.setManaged(false); }
    }

    private void show(Button... buttons) {
        for (Button b : buttons) { b.setVisible(true); b.setManaged(true); }
    }

    private void navigate(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showStatus("Navigation error: " + e.getMessage(), false);
        }
    }
}