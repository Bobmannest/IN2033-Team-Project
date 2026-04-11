package com.example.members;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderHistory {

    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Void> colRowNum;
    @FXML private TableColumn<Order, String> colOrderId;
    @FXML private TableColumn<Order, LocalDateTime> colDate;
    @FXML private TableColumn<Order, Double> colAmount;
    @FXML private TableColumn<Order, String> colAddress;
    @FXML private Label errorLabel;
    @FXML private Button btnCreatePromotion;
    @FXML private Button btnManagePromotions;
    @FXML private Button btnReports;
    @FXML private Button btnLogin;
    @FXML private Button btnLogout;
    @FXML private Button btnOrders;
    @FXML private Button btnAccount;

    @FXML
    public void initialize() {
        colRowNum.setCellFactory(col -> new TableCell<>() {
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });

        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));

        colDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colDate.setCellFactory(col -> new TableCell<>() {
            private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : fmt.format(item));
            }
        });

        colAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colAmount.setCellFactory(col -> new TableCell<>() {
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("£%.2f", item));
            }
        });

        colAddress.setCellValueFactory(new PropertyValueFactory<>("deliveryAddress"));

        Member member = Session.getMember();
        if (member == null) return;

        try {
            List<Order> orders = OrderDAO.getOrdersForMember(member.getAccountNo());
            if (orders.isEmpty()) {
                errorLabel.setText("You have no past orders.");
            } else {
                ordersTable.getItems().addAll(orders);
            }
        } catch (SQLException e) {
            errorLabel.setText("Could not load orders: " + e.getMessage());
        }
        setupNavBar();
    }

    @FXML
    private void handleHome() {
        navigate("/com/example/fx/Home.fxml");
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
    private void handleReports() { navigate("/com/example/fx/Reports.fxml"); }

    @FXML
    private void handleLogin() { navigate("/com/example/fx/Login.fxml"); }

    @FXML
    private void handleLogout() {Session.setMember(null); navigate("/com/example/fx/Login.fxml");}

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


    private void navigate(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) ordersTable.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}