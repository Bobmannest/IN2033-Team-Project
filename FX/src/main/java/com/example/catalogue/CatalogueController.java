package com.example.catalogue;

import com.example.promotion.PromotionDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.members.Session;
import com.example.members.Member;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CatalogueController {
    @FXML private VBox catalogueVBox;
    @FXML private Label filterErrorLabel;
    @FXML private Label insufficientStockErrorLabel;
    public static Label stockErrorLabel;

    @FXML private TextField searchBar;
    @FXML private CheckBox inStockCheckbox;
    @FXML private CheckBox maxPriceCheckBox;
    @FXML private TextField maxPriceField;
    @FXML private CheckBox minPriceCheckBox;
    @FXML private TextField minPriceField;
    @FXML private Button clearFiltersButton;

    @FXML private Button btnCreatePromotion;
    @FXML private Button btnManagePromotions;
    @FXML private Button btnOrders;
    @FXML private Button btnAccount;
    @FXML private Button btnReports;
    @FXML private Button btnLogin;
    @FXML private Button btnLogout;

    private String currentCampaignFilter = null;

    public void displayItems() {
        currentCampaignFilter = null;
        refreshDisplayedItems();
    }

    public void displayPromotionItems(String campaignId) {
        currentCampaignFilter = campaignId;
        refreshDisplayedItems();
    }


    @FXML
    private void handleApplyFilters() {
        refreshDisplayedItems();
    }

    @FXML
    private void handleClearFilters() {
        clearFiltersButton.setVisible(false);
        clearFiltersButton.setManaged(false);
        displayItems();
    }

    private void refreshDisplayedItems() {
        catalogueVBox.getChildren().clear();
        filterErrorLabel.setText("");
        filterErrorLabel.setManaged(false);


        List<CatalogueItem> items;

        try {
            if (currentCampaignFilter != null) {
                items = PromotionDAO.getCampaignCatalogueItems(currentCampaignFilter);
                System.out.println("Campaign ID: " + currentCampaignFilter);
                System.out.println("Items found: " + items.size());
            } else {
                items = CatalogueDatabase.getCatalogueItems();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        double minPrice = 0;
        double maxPrice = Double.MAX_VALUE;
        boolean inStockOnly = inStockCheckbox.isSelected();
        String search = searchBar.getText().trim().toLowerCase();

        try {
            if (minPriceCheckBox.isSelected() && !minPriceField.getText().trim().isEmpty()) {
                minPrice = Double.parseDouble(minPriceField.getText().trim());
            }
            if (maxPriceCheckBox.isSelected() && !maxPriceField.getText().trim().isEmpty()) {
                maxPrice = Double.parseDouble(maxPriceField.getText().trim());
            }
        } catch (NumberFormatException e) {
            filterErrorLabel.setText("Enter a valid price");
            filterErrorLabel.setManaged(true);
        }

        double finalMinPrice = minPrice;
        double finalMaxPrice = maxPrice;

        items = items.stream()
                .filter(item -> item.getPackage_cost() >= finalMinPrice)
                .filter(item -> item.getPackage_cost() <= finalMaxPrice)
                .filter(item -> !inStockOnly || item.getAvailability() > 0)
                .filter(item -> search.isEmpty() || item.getDescription().toLowerCase().contains(search))
                .toList();

        for (CatalogueItem item : items) {
            try {
                System.out.println("Displaying item: " + item.getItem_id() + " - " + item.getDescription());

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
        stockErrorLabel = insufficientStockErrorLabel;
        CatalogueDatabase.setListener(this);
        setupNavBar();

        if (CatalogueDatabase.pendingCampaignFilter != null) {
            currentCampaignFilter = CatalogueDatabase.pendingCampaignFilter;
            CatalogueDatabase.pendingCampaignFilter = null;
            clearFiltersButton.setVisible(true);
            clearFiltersButton.setManaged(true);
        }

        refreshDisplayedItems();
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

    @FXML
    private void handleOrders() {
        navigate("/com/example/fx/OrderHistory.fxml");
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
            Stage stage = (Stage) catalogueVBox.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}