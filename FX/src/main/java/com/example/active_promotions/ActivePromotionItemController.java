package com.example.active_promotions;

import com.example.catalogue.CatalogueDatabase;
import com.example.promotion.PromotionCampaign;
import com.example.promotion.PromotionService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

public class ActivePromotionItemController {

    @FXML private Label nameLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label discountValueLabel;
    @FXML private Label endsInValueLabel;
    @FXML private Button viewItemsButton;

    private PromotionCampaign currentCampaign;
    private final PromotionService promotionService = new PromotionService();

    public void setCampaign(PromotionCampaign campaign) {
        this.currentCampaign = campaign;

        nameLabel.setText(campaign.getCampaignName());

        if (campaign.getCampaignDescription() != null && !campaign.getCampaignDescription().isBlank()) {
            descriptionLabel.setText(campaign.getCampaignDescription());
        } else {
            descriptionLabel.setText("Enjoy a limited-time discount on selected items!");
        }

        discountValueLabel.setText(getDiscountText(campaign));
        endsInValueLabel.setText(getTimeRemainingText(campaign));
    }

    private String getDiscountText(PromotionCampaign campaign) {
        if (campaign.getDefaultDiscountPct() != null) {
            return removeTrailingZero(campaign.getDefaultDiscountPct()) + "% Off";
        }
        return "Variable";
    }

    private String getTimeRemainingText(PromotionCampaign campaign) {
        if (campaign.getEndDateTime() == null) {
            return "N/A";
        }

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, campaign.getEndDateTime());

        if (duration.isNegative() || duration.isZero()) {
            return "Ended";
        }

        long days = duration.toDays();
        long hours = duration.toHours() % 24;

        if (days > 0) {
            return days + " Days " + hours + " Hours";
        }
        return hours + " Hours";
    }

    private String removeTrailingZero(Double value) {
        if (value == null) {
            return "0";
        }

        if (value == Math.floor(value)) {
            return String.valueOf(value.intValue());
        }

        return String.valueOf(value);
    }

    @FXML
    private void handleViewItems() {
        if (currentCampaign == null) {
            return;
        }

        try {
            promotionService.recordCampaignClick(currentCampaign.getCampaignId());
            CatalogueDatabase.pendingCampaignFilter = currentCampaign.getCampaignId();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fx/Catalogue.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) viewItemsButton.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}