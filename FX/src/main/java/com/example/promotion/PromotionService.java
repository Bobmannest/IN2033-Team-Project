package com.example.promotion;

import com.example.catalogue.CatalogueDatabase;
import com.example.catalogue.CatalogueItem;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class PromotionService {

    public void createCampaign(PromotionCampaign campaign) throws SQLException {
        validateCampaign(campaign);
        PromotionDAO.createCampaign(campaign);
    }

    public void addItemToCampaign(String campaignId, int itemId, Double itemDiscountPct) throws SQLException {
        if (campaignId == null || campaignId.isBlank()) {
            throw new IllegalArgumentException("Campaign ID cannot be empty.");
        }

        CatalogueItem item = findCatalogueItemById(itemId);
        if (item == null) {
            throw new IllegalArgumentException("Catalogue item does not exist in PU.");
        }

        if (itemDiscountPct != null && (itemDiscountPct < 0 || itemDiscountPct > 100)) {
            throw new IllegalArgumentException("Discount must be between 0 and 100.");
        }

        PromotionDAO.addItemToCampaign(campaignId, itemId, itemDiscountPct);
    }

    public List<PromotionCampaign> getActiveCampaigns() throws SQLException {
        return PromotionDAO.getActiveCampaigns();
    }

    public List<CatalogueItem> getCampaignCatalogueItems(String campaignId) throws SQLException {
        return PromotionDAO.getCampaignCatalogueItems(campaignId);
    }

    public void recordCampaignClick(String campaignId) throws SQLException {
        PromotionDAO.incrementCampaignHits(campaignId);
    }

    public void recordIncludedInOrder(String campaignId, int itemId, int quantity) throws SQLException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        PromotionDAO.incrementIncludedInOrder(campaignId, itemId, quantity);
    }

    public void recordPurchased(String campaignId, int itemId, int quantity) throws SQLException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        PromotionDAO.incrementPurchased(campaignId, itemId, quantity);
    }

    public double calculateDiscountedPrice(String campaignId, int itemId, double originalPrice) throws SQLException {
        Double discountPct = PromotionDAO.getDiscountForItem(campaignId, itemId);

        if (discountPct == null || discountPct <= 0) {
            return originalPrice;
        }

        return originalPrice - (originalPrice * (discountPct / 100.0));
    }

    public String getActiveCampaignIdForItem(int itemId) throws SQLException {
        List<PromotionCampaign> campaigns = PromotionDAO.getActiveCampaigns();

        for (PromotionCampaign campaign : campaigns) {
            List<CatalogueItem> items = PromotionDAO.getCampaignCatalogueItems(campaign.getCampaignId());
            for (CatalogueItem item : items) {
                if (item.getItem_id() == itemId) {
                    return campaign.getCampaignId();
                }
            }
        }

        return null;
    }

    public CatalogueItem findCatalogueItemById(int itemId) {
        for (CatalogueItem item : CatalogueDatabase.getCatalogueItems()) {
            if (item.getItem_id() == itemId) {
                return item;
            }
        }
        return null;
    }

    public void cancelCampaign(String campaignId) throws SQLException {
        PromotionDAO.updateCampaignStatus(campaignId, "cancelled");
    }

    public boolean isCampaignCurrentlyActive(PromotionCampaign campaign) {
        if (campaign == null) {
            return false;
        }

        if (!"active".equalsIgnoreCase(campaign.getStatus())) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(campaign.getStartDateTime()) && !now.isAfter(campaign.getEndDateTime());
    }

    private void validateCampaign(PromotionCampaign campaign) {
        if (campaign == null) {
            throw new IllegalArgumentException("Campaign cannot be null.");
        }

        if (campaign.getCampaignId() == null || campaign.getCampaignId().isBlank()) {
            throw new IllegalArgumentException("Campaign ID cannot be empty.");
        }

        if (campaign.getCampaignName() == null || campaign.getCampaignName().isBlank()) {
            throw new IllegalArgumentException("Campaign name cannot be empty.");
        }

        if (campaign.getStartDateTime() == null || campaign.getEndDateTime() == null) {
            throw new IllegalArgumentException("Start and end date/time are required.");
        }

        if (!campaign.getEndDateTime().isAfter(campaign.getStartDateTime())) {
            throw new IllegalArgumentException("End date/time must be after start date/time.");
        }

        if (campaign.getDiscountMode() == null || campaign.getDiscountMode().isBlank()) {
            throw new IllegalArgumentException("Discount mode is required.");
        }

        if (!campaign.getDiscountMode().equalsIgnoreCase("fixed")
                && !campaign.getDiscountMode().equalsIgnoreCase("variable")) {
            throw new IllegalArgumentException("Discount mode must be 'fixed' or 'variable'.");
        }

        if (campaign.getDefaultDiscountPct() != null
                && (campaign.getDefaultDiscountPct() < 0 || campaign.getDefaultDiscountPct() > 100)) {
            throw new IllegalArgumentException("Default discount must be between 0 and 100.");
        }
    }
}