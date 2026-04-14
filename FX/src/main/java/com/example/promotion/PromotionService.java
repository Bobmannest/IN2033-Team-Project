package com.example.promotion;

import com.example.catalogue.CatalogueDatabase;
import com.example.catalogue.CatalogueItem;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PromotionService {

    public void createCampaign(PromotionCampaign campaign) throws SQLException {
        validateCampaign(campaign);
        PromotionDAO.createCampaign(campaign);
    }

    public void addItemToCampaign(String campaignId, String itemId, Double itemDiscountPct) throws SQLException {
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

    public List<PromotionCampaign> getAllCampaigns() throws SQLException {
        return PromotionDAO.getAllCampaigns();
    }

    public List<PromotionCampaign> getActiveCampaigns() throws SQLException {
        List<PromotionCampaign> allCampaigns = PromotionDAO.getAllCampaigns();
        List<PromotionCampaign> activeCampaigns = new ArrayList<>();

        for (PromotionCampaign campaign : allCampaigns) {
            if (isCampaignCurrentlyActive(campaign)) {
                activeCampaigns.add(campaign);
            }
        }

        return activeCampaigns;
    }

    public boolean isCampaignCurrentlyActive(PromotionCampaign campaign) {
        if (campaign == null) {
            return false;
        }

        if (campaign.getStatus() == null || !campaign.getStatus().equalsIgnoreCase("active")) {
            return false;
        }

        if (campaign.getStartDateTime() == null || campaign.getEndDateTime() == null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();

        return !now.isBefore(campaign.getStartDateTime())
                && !now.isAfter(campaign.getEndDateTime());
    }

    public Double getDiscountForItem(String campaignId, String itemId) throws SQLException {
        return PromotionDAO.getDiscountForItem(campaignId, itemId);
    }

    public double getDiscountedPrice(String campaignId, String itemId, double originalPrice) throws SQLException {
        Double discountPct = getDiscountForItem(campaignId, itemId);

        if (discountPct == null || discountPct <= 0) {
            return originalPrice;
        }

        return originalPrice - (originalPrice * discountPct / 100.0);
    }

    public String getActiveCampaignIdForItem(int itemId) throws SQLException {
        List<PromotionCampaign> activeCampaigns = getActiveCampaigns();

        for (PromotionCampaign campaign : activeCampaigns) {
            List<CatalogueItem> items = PromotionDAO.getCampaignCatalogueItems(campaign.getCampaignId());

            for (CatalogueItem item : items) {
                if (item.getItem_id().equals(itemId)) {
                    return campaign.getCampaignId();
                }
            }
        }

        return null;
    }

    public void recordCampaignClick(String campaignId) throws SQLException {
        if (campaignId == null || campaignId.isBlank()) {
            throw new IllegalArgumentException("Campaign ID cannot be empty.");
        }

        PromotionDAO.incrementCampaignHits(campaignId);
    }

    public void recordIncludedInOrder(String campaignId, String itemId, int quantity) throws SQLException {
        if (campaignId == null || campaignId.isBlank()) {
            throw new IllegalArgumentException("Campaign ID cannot be empty.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        PromotionDAO.incrementIncludedInOrder(campaignId, itemId, quantity);
    }

    public void recordPurchased(String campaignId, String itemId, int quantity) throws SQLException {
        if (campaignId == null || campaignId.isBlank()) {
            throw new IllegalArgumentException("Campaign ID cannot be empty.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        PromotionDAO.incrementPurchased(campaignId, itemId, quantity);
    }

    public void cancelCampaign(String campaignId) throws SQLException {
        if (campaignId == null || campaignId.isBlank()) {
            throw new IllegalArgumentException("Campaign ID cannot be empty.");
        }

        PromotionDAO.updateCampaignStatus(campaignId, "cancelled");
    }

    public CatalogueItem findCatalogueItemById(String itemId) {
        for (CatalogueItem item : CatalogueDatabase.getCatalogueItems()) {
            if (item.getItem_id() == itemId) {
                return item;
            }
        }
        return null;
    }

    public void reactivateCampaign(String campaignId) throws SQLException {
        if (campaignId == null || campaignId.isBlank()) {
            throw new IllegalArgumentException("Campaign ID cannot be empty.");
        }

        PromotionDAO.updateCampaignStatus(campaignId, "active");
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