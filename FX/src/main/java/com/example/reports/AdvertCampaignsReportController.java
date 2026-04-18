package com.example.reports;

import com.example.fx.DatabaseConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdvertCampaignsReportController {

    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    @FXML private Label lblStartPeriod;
    @FXML private Label lblEndPeriod;
    @FXML private Label lblActiveCampaigns;
    @FXML private Label lblGenerated;
    @FXML private VBox headerBox;
    @FXML private TableView<CampaignRow> campaignsTable;
    @FXML private TableColumn<CampaignRow, String> colCampaignId;
    @FXML private TableColumn<CampaignRow, String> colStartDate;
    @FXML private TableColumn<CampaignRow, String> colEndDate;
    @FXML private TableColumn<CampaignRow, String> colItemsIncluded;
    @FXML private TableColumn<CampaignRow, String> colDiscount;
    @FXML private TableColumn<CampaignRow, String> colDescription;
    @FXML private TableColumn<CampaignRow, String> colItemsSold;
    @FXML private TableColumn<CampaignRow, String> colTotalSales;
    @FXML private Label errorLabel;
    @FXML private VBox printableArea;


    // Sets default date range to last 3 months and binds column cell factories
    @FXML
    public void initialize() {
        dateFrom.setValue(LocalDate.now().minusMonths(3));
        dateTo.setValue(LocalDate.now());

        colCampaignId   .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().campaignId()));
        colStartDate    .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().startDate()));
        colEndDate      .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().endDate()));
        colItemsIncluded.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().itemsIncluded()));
        colDiscount     .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().discount()));
        colDescription  .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().description()));
        colItemsSold    .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().itemsSold()));
        colTotalSales   .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().totalSales()));
    }

    // Validates date inputs before loading the report
    @FXML
    private void handleGenerateReport() {
        if (dateFrom.getValue() == null || dateTo.getValue() == null) {
            errorLabel.setText("Please select both dates.");
            return;
        }
        if (dateFrom.getValue().isAfter(dateTo.getValue())) {
            errorLabel.setText("Start date must be before end date.");
            return;
        }
        errorLabel.setText("");
        loadReport(dateFrom.getValue(), dateTo.getValue());
    }

    private void loadReport(LocalDate from, LocalDate to) {
        String campaignSql = """
                SELECT campaign_id, campaign_name, start_datetime, end_datetime,
                       discount_mode, default_discount_pct,
                       (SELECT COUNT(*) FROM PromotionCampaignItem pci WHERE pci.campaign_id = pc.campaign_id) AS item_count
                FROM PromotionCampaign pc
                WHERE DATE(start_datetime) <= ? AND DATE(end_datetime) >= ?
                ORDER BY start_datetime
                """;

        String itemSql = """
                SELECT p.product_name, pci.item_discount_pct,
                       COALESCE(cim.included_in_order_count, 0) AS items_sold,
                       COALESCE(
                           (SELECT SUM(oi.line_total)
                            FROM OnlineOrderItem oi
                            JOIN OnlineOrder o ON o.order_id = oi.order_id
                            WHERE oi.campaign_id = pci.campaign_id
                              AND oi.item_id = pci.item_id
                              AND o.order_status = 'processing'), 0) AS total_sales
                FROM PromotionCampaignItem pci
                JOIN Product p ON p.item_id = pci.item_id
                LEFT JOIN CampaignItemMetrics cim ON cim.campaign_id = pci.campaign_id AND cim.item_id = pci.item_id
                WHERE pci.campaign_id = ?
                ORDER BY pci.item_id
                """;

        ObservableList<CampaignRow> rows = FXCollections.observableArrayList();
        List<String> campaignNames = new ArrayList<>();
        int campaignCount = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(campaignSql)) {

            ps.setDate(1, Date.valueOf(to));
            ps.setDate(2, Date.valueOf(from));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                campaignCount++;
                String campId    = rs.getString("campaign_id");
                String campName  = rs.getString("campaign_name");
                String startDt   = rs.getTimestamp("start_datetime").toLocalDateTime().toLocalDate().toString();
                String endDt     = rs.getTimestamp("end_datetime").toLocalDateTime().toLocalDate().toString();
                String mode      = rs.getString("discount_mode");
                double defDisc   = rs.getDouble("default_discount_pct");
                int    itemCount = rs.getInt("item_count");
                String discStr   = mode.equals("fixed") ? String.format("Fixed, %.0f%%", defDisc) : "Variable";

                campaignNames.add(campId);
                rows.add(new CampaignRow(campId, startDt, endDt, String.valueOf(itemCount), discStr, "", "", ""));

                try (PreparedStatement ps2 = conn.prepareStatement(itemSql)) {
                    ps2.setString(1, campId);
                    ResultSet rs2 = ps2.executeQuery();
                    double campTotal = 0;

                    while (rs2.next()) {
                        String name       = rs2.getString("product_name");
                        double itemDisc   = rs2.getDouble("item_discount_pct");
                        int    sold       = rs2.getInt("items_sold");
                        double salesTotal = rs2.getDouble("total_sales");
                        campTotal += salesTotal;

                        String discDisplay = mode.equals("fixed")
                                ? String.format("%.0f%%", defDisc)
                                : String.format("%.0f%%", itemDisc);

                        rows.add(new CampaignRow("", "", "", "", discDisplay, name, String.valueOf(sold), String.format("£%.2f", salesTotal)));
                    }

                    rows.add(new CampaignRow("Total Sales in campaign:", "", "", "", "", "", "", String.format("£%.2f", campTotal)));
                }
            }

        } catch (SQLException e) {
            errorLabel.setText("Error loading report: " + e.getMessage());
            return;
        }

        lblStartPeriod.setText("Start Period: " + from);
        lblEndPeriod.setText("End Period: " + to);
        lblActiveCampaigns.setText("Active campaigns: " + campaignCount + (campaignNames.isEmpty() ? "" : " (" + String.join(" and ", campaignNames) + ")"));
        lblGenerated.setText("Generated: " + LocalDate.now() + "     Generated by IPOS-PU Operations");
        headerBox.setVisible(true);
        headerBox.setManaged(true);

        campaignsTable.setItems(rows);
    }

    // Generates an HTML version of the report and sends it to the printer via WebView
    @FXML
    private void handlePrint() {
        if (campaignsTable.getItems().isEmpty()) {
            errorLabel.setText("Generate a report before printing.");
            return;
        }

        StringBuilder html = new StringBuilder();
        html.append("""
        <html><head><style>
        body { font-family: Arial, sans-serif; margin: 40px; font-size: 13px; }
        h2 { font-style: italic; }
        .header { display: flex; justify-content: space-between; margin-bottom: 20px; }
        .address { text-align: right; }
        table { width: 100%; border-collapse: collapse; margin-top: 16px; }
        th { background: #eee; border: 1px solid #999; padding: 6px; text-align: left; }
        td { border: 1px solid #ccc; padding: 6px; }
        .footer { margin-top: 16px; font-size: 11px; color: #555; }
        </style></head><body>
        <h2>IPOS-PU Advert Campaigns Report</h2>
        <hr/>
        <div class="header">
          <div>
    """);

        html.append("<p>").append(lblStartPeriod.getText()).append("</p>");
        html.append("<p>").append(lblEndPeriod.getText()).append("</p>");
        html.append("<p>").append(lblActiveCampaigns.getText()).append("</p>");
        html.append("""
          </div>
          <div class="address">
            Cosymed Ltd.,<br/>
            27 Sainsbury Close,<br/>
            3, High Level Drive,<br/>
            Sydenham,<br/>
            SE26 3ET<br/>
            Phone: 0208 778 0124<br/>
            Fax: 0208 778 0125
          </div>
        </div>
        <table>
          <tr>
            <th>Campaign ID</th>
            <th>Start Date</th>
            <th>End Date</th>
            <th>Items Included</th>
            <th>Discount, %</th>
            <th>Description</th>
            <th>Items Sold</th>
            <th>Total Sales, £</th>
          </tr>
    """);

        for (CampaignRow row : campaignsTable.getItems()) {
            html.append("<tr>")
                    .append("<td>").append(row.campaignId()).append("</td>")
                    .append("<td>").append(row.startDate()).append("</td>")
                    .append("<td>").append(row.endDate()).append("</td>")
                    .append("<td>").append(row.itemsIncluded()).append("</td>")
                    .append("<td>").append(row.discount()).append("</td>")
                    .append("<td>").append(row.description()).append("</td>")
                    .append("<td>").append(row.itemsSold()).append("</td>")
                    .append("<td>").append(row.totalSales()).append("</td>")
                    .append("</tr>");
        }

        html.append("</table>");
        html.append("<div class='footer'><p>").append(lblGenerated.getText()).append("</p></div>");
        html.append("</body></html>");

        javafx.scene.web.WebView webView = new javafx.scene.web.WebView();
        webView.getEngine().loadContent(html.toString());

        javafx.scene.web.WebEngine engine = webView.getEngine();
        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                PrinterJob job = PrinterJob.createPrinterJob();
                if (job != null && job.showPrintDialog(printableArea.getScene().getWindow())) {
                    engine.print(job);
                    job.endJob();
                }
            }
        });
    }

    @FXML private void handleBackToReports()    { navigate("/com/example/fx/Reports.fxml"); }
    @FXML private void handleActivePromotions() { navigate("/com/example/fx/ActivePromotions.fxml"); }
    @FXML private void handleLogout()           { navigate("/com/example/fx/Login.fxml"); }
    @FXML private void handleHome()             { navigate("/com/example/fx/Home.fxml"); }
    @FXML private void handleCatalogue()        { navigate("/com/example/fx/Catalogue.fxml"); }
    @FXML private void handleOrders()           { navigate("/com/example/fx/OrderHistory.fxml"); }
    @FXML private void handleAccount()          { navigate("/com/example/fx/Account.fxml"); }
    @FXML private void handleBasket()           { navigate("/com/example/fx/Basket.fxml"); }
    @FXML private void handleCreatePromotion()  { navigate("/com/example/fx/Promotion.fxml"); }
    @FXML private void handleManagePromotions() { navigate("/com/example/fx/CampaignItem.fxml"); }

    // Generic nav helper - preserves maximised window state
    private void navigate(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            javafx.scene.Parent root = loader.load();
            Stage stage = (Stage) campaignsTable.getScene().getWindow();
            boolean wasMaximized = stage.isMaximized();
            stage.getScene().setRoot(root);
            if (wasMaximized) stage.setMaximized(true);
        } catch (IOException e) {
            errorLabel.setText("Navigation error: " + e.getMessage());
        }
    }

    public record CampaignRow(
            String campaignId,
            String startDate,
            String endDate,
            String itemsIncluded,
            String discount,
            String description,
            String itemsSold,
            String totalSales
    ) {}
}