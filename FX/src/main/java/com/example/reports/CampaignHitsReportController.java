package com.example.reports;

import com.example.fx.DatabaseConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CampaignHitsReportController {

    @FXML private ComboBox<String> campaignCombo;
    @FXML private Label lblCampaignId;
    @FXML private Label lblCampaignDesc;
    @FXML private Label lblStartDate;
    @FXML private Label lblEndDate;
    @FXML private Label lblGeneratedDate;
    @FXML private TableView<HitsRow> hitsTable;
    @FXML private TableColumn<HitsRow, String>  colCounterId;
    @FXML private TableColumn<HitsRow, String>  colDescription;
    @FXML private TableColumn<HitsRow, Integer> colHits;
    @FXML private TableColumn<HitsRow, String>  colPurchases;
    @FXML private TableColumn<HitsRow, String>  colConversion;
    @FXML private Label errorLabel;
    @FXML private VBox printableArea;
    @FXML private VBox headerBox;

    private final List<String[]> campaignList = new ArrayList<>();

    @FXML
    public void initialize() {
        setupColumns();
        loadCampaignCombo();
    }

    private void setupColumns() {
        colCounterId  .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().counterId()));
        colDescription.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().description()));
        colHits       .setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().hits()).asObject());
        colPurchases  .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().purchases()));
        colConversion .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().conversionRate()));
    }

    private void loadCampaignCombo() {
        String sql = "SELECT campaign_id, campaign_name FROM PromotionCampaign ORDER BY start_datetime DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ObservableList<String> items = FXCollections.observableArrayList();
            while (rs.next()) {
                String id      = rs.getString("campaign_id");
                String name    = rs.getString("campaign_name");
                String display = id + " – " + name;
                campaignList.add(new String[]{id, display});
                items.add(display);
            }
            campaignCombo.setItems(items);

        } catch (SQLException e) {
            errorLabel.setText("Could not load campaigns: " + e.getMessage());
        }
    }

    @FXML
    private void handleGenerateReport() {
        int selectedIdx = campaignCombo.getSelectionModel().getSelectedIndex();
        if (selectedIdx < 0) {
            errorLabel.setText("Please select a campaign first.");
            return;
        }
        errorLabel.setText("");
        loadReport(campaignList.get(selectedIdx)[0]);
    }

    private void loadReport(String campaignId) {

        headerBox.setVisible(true);
        headerBox.setManaged(true);

        String headerSql = """
                SELECT campaign_id, campaign_name, campaign_description,
                       start_datetime, end_datetime
                FROM PromotionCampaign
                WHERE campaign_id = ?
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(headerSql)) {

            ps.setString(1, campaignId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lblCampaignId.setText("Campaign ID: " + rs.getString("campaign_id"));
                lblCampaignDesc.setText("Description: " + rs.getString("campaign_name")
                        + (rs.getString("campaign_description") != null
                           ? " – " + rs.getString("campaign_description") : ""));
                lblStartDate.setText("Start Period: " + rs.getTimestamp("start_datetime").toLocalDateTime().toLocalDate());
                lblEndDate.setText("End Period:   " + rs.getTimestamp("end_datetime").toLocalDateTime().toLocalDate());
                lblGeneratedDate.setText("Generated: " + java.time.LocalDate.now());
            }

        } catch (SQLException e) {
            errorLabel.setText("Error loading header: " + e.getMessage());
            return;
        }

        ObservableList<HitsRow> rows = FXCollections.observableArrayList();

        String campHitsSql = "SELECT campaign_hits FROM CampaignMetrics WHERE campaign_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(campHitsSql)) {

            ps.setString(1, campaignId);
            ResultSet rs = ps.executeQuery();
            int campHits = rs.next() ? rs.getInt("campaign_hits") : 0;
            rows.add(new HitsRow(campaignId, "Campaign hits", campHits, "N/A", "N/A"));

        } catch (SQLException e) {
            errorLabel.setText("Error loading campaign hits: " + e.getMessage());
            return;
        }

        String itemSql = """
                SELECT p.product_name, cim.item_id,
                       cim.included_in_order_count, cim.purchased_count
                FROM CampaignItemMetrics cim
                JOIN Product p ON p.item_id = cim.item_id
                WHERE cim.campaign_id = ?
                ORDER BY cim.item_id
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(itemSql)) {

            ps.setString(1, campaignId);
            ResultSet rs = ps.executeQuery();
            int itemNo = 1;
            while (rs.next()) {
                String name      = rs.getString("product_name");
                int    hits      = rs.getInt("included_in_order_count");
                int    purchased = rs.getInt("purchased_count");
                String conversionStr = hits == 0 ? "N/A" : String.format(
                        "%.0f / %.0f = %.2f (%.1f%%)",
                        (double) purchased, (double) hits,
                        (double) purchased / hits, ((double) purchased / hits) * 100);

                rows.add(new HitsRow(
                        "Item(" + itemNo + ")",
                        "\"" + name + "\" hits",
                        hits,
                        String.valueOf(purchased),
                        conversionStr
                ));
                itemNo++;
            }

        } catch (SQLException e) {
            errorLabel.setText("Error loading item metrics: " + e.getMessage());
            return;
        }

        hitsTable.setItems(rows);
    }

    @FXML
    private void handlePrint() {
        if (hitsTable.getItems().isEmpty()) {
            errorLabel.setText("Generate a report before printing.");
            return;
        }
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(printableArea.getScene().getWindow())) {
            boolean success = job.printPage(printableArea);
            if (success) job.endJob();
        }
    }

    @FXML private void handleHome()               { navigate("/com/example/fx/Home.fxml", 800, 600); }
    @FXML private void handleCatalogue()          { navigate("/com/example/fx/Catalogue.fxml", 800, 600); }
    @FXML private void handleOrders()             { navigate("/com/example/fx/OrderHistory.fxml", 800, 600); }
    @FXML private void handleAccount()            { navigate("/com/example/fx/Account.fxml", 800, 600); }
    @FXML private void handleBasket()             { navigate("/com/example/fx/Basket.fxml", 800, 600); }
    @FXML private void handleCreatePromotion()    { navigate("/com/example/fx/Promotion.fxml", 900, 650); }
    @FXML private void handleManagePromotions()   { navigate("/com/example/fx/CampaignItem.fxml", 900, 650); }
    @FXML private void handleReports() { navigate("/com/example/fx/Reports.fxml", 1000, 620); }        {}

    private void navigate(String fxml, int w, int h) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Scene scene = new Scene(loader.load(), w, h);
            Stage stage = (Stage) hitsTable.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            errorLabel.setText("Navigation error: " + e.getMessage());
        }
    }

    public record HitsRow(
            String counterId,
            String description,
            int    hits,
            String purchases,
            String conversionRate
    ) {}
}