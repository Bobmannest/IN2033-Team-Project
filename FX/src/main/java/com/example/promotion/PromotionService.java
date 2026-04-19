package com.example.promotion;

import com.example.catalogue.CatalogueDatabase;
import com.example.catalogue.CatalogueItem;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// service class for all of promotions, includes validations

public class PromotionService {

    // validations for creating a campaign and adding individual items to a campaign

    public void createCampaign(PromotionCampaign campaign) throws SQLException {
        validateCampaign(campaign);
        PromotionDAO.createCampaign(campaign);
    }

    public String addItemToCampaign(String campaignId, String itemId, Double itemDiscountPct) throws SQLException {
        if (campaignId == null || campaignId.isBlank()) {
            throw new IllegalArgumentException("Campaign ID cannot be empty.");
        }

        if (itemId == null || itemId.isBlank()) {
            throw new IllegalArgumentException("Item ID cannot be empty.");
        }

        CatalogueItem item = findCatalogueItemById(itemId);
        if (item == null) {
            throw new IllegalArgumentException("Catalogue item does not exist in PU.");
        }

        if (itemDiscountPct != null && (itemDiscountPct < 0 || itemDiscountPct > 100)) {
            throw new IllegalArgumentException("Discount must be between 0 and 100.");
        }

        if (PromotionDAO.campaignItemExists(campaignId, itemId)) {
            throw new IllegalArgumentException("This item is already in the selected campaign.");
        }

        PromotionCampaign campaign = PromotionDAO.getCampaignById(campaignId);
        if (campaign == null) {
            throw new IllegalArgumentException("Campaign does not exist.");
        }

        List<PromotionCampaign> overlaps = PromotionDAO.findOverlappingCampaignsForItem(
                itemId,
                campaign.getStartDateTime(),
                campaign.getEndDateTime(),
                campaignId
        );

        PromotionDAO.addItemToCampaign(campaignId, itemId, itemDiscountPct);

        if (overlaps.isEmpty()) {
            return null;
        }

        StringBuilder warning = new StringBuilder();
        warning.append("Overlapping campaign detected for item ")
                .append(itemId)
                .append(". Highest discount will be applied. Conflicts with: ");
        for (int i = 0; i < overlaps.size(); i++) {
            PromotionCampaign c = overlaps.get(i);
            warning.append(c.getCampaignId())
                    .append(" (")
                    .append(c.getCampaignName())
                    .append(")");
            if (i < overlaps.size() - 1) {
                warning.append(", ");
            }
        }
        return warning.toString();
    }

    // returns all stored campaigns

    public List<PromotionCampaign> getAllCampaigns() throws SQLException {
        return PromotionDAO.getAllCampaigns();
    }

    // returns currently active campaigns

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

    // checks if campaign is active right now, against the current date and time

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

    // returns discount given by one specific campaign item

    public Double getDiscountForItem(String campaignId, String itemId) throws SQLException {
        return PromotionDAO.getDiscountForItem(campaignId, itemId);
    }

    // solution for overlapping campaign, returns highest currently active discount for an item

    public Double getEffectiveDiscountForItem(String itemId) throws SQLException {
        if (itemId == null || itemId.isBlank()) {
            throw new IllegalArgumentException("Item ID cannot be empty.");
        }

        List<PromotionCampaign> activeCampaigns = PromotionDAO.getActiveCampaignsForItem(itemId);
        double highestDiscount = 0.0;
        for (PromotionCampaign campaign : activeCampaigns) {
            Double discount = PromotionDAO.getDiscountForItem(campaign.getCampaignId(), itemId);

            if (discount != null && discount > highestDiscount) {
                highestDiscount = discount;
            }
        }
        return highestDiscount;
    }

    // returns the active campaign that "wins", that is the one that has an item set at the highest discount

    public PromotionCampaign getWinningCampaignForItem(String itemId) throws SQLException {
        if (itemId == null || itemId.isBlank()) {
            throw new IllegalArgumentException("Item ID cannot be empty.");
        }

        List<PromotionCampaign> activeCampaigns = PromotionDAO.getActiveCampaignsForItem(itemId);

        PromotionCampaign winningCampaign = null;
        double highestDiscount = 0.0;

        for (PromotionCampaign campaign : activeCampaigns) {
            Double discount = PromotionDAO.getDiscountForItem(campaign.getCampaignId(), itemId);

            if (discount != null && discount > highestDiscount) {
                highestDiscount = discount;
                winningCampaign = campaign;
            }
        }

        return winningCampaign;
    }

    // updating editable campaign details on the manage promotions screen

    public void updateCampaignDetails(
            String campaignId,
            String description,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) throws SQLException {
        if (campaignId == null || campaignId.isBlank()) {
            throw new IllegalArgumentException("Campaign ID cannot be empty.");
        }

        if (startDateTime == null || endDateTime == null) {
            throw new IllegalArgumentException("Start and end date/time are required.");
        }

        if (!endDateTime.isAfter(startDateTime)) {
            throw new IllegalArgumentException("End date/time must be after start date/time.");
        }

        PromotionDAO.updateCampaignDetails(campaignId, description, startDateTime, endDateTime);
    }

    // applies effective discount to original price of an item

    public double getDiscountedPrice(String itemId, double originalPrice) throws SQLException {
        Double discountPct = getEffectiveDiscountForItem(itemId);

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

    // records that a user clicked on a campaign

    public void recordCampaignClick(String campaignId) throws SQLException {
        if (campaignId == null || campaignId.isBlank()) {
            throw new IllegalArgumentException("Campaign ID cannot be empty.");
        }

        PromotionDAO.incrementCampaignHits(campaignId);
    }

    // records that an item was added to an order

    public void recordIncludedInOrder(String campaignId, String itemId, int quantity) throws SQLException {
        if (campaignId == null || campaignId.isBlank()) {
            throw new IllegalArgumentException("Campaign ID cannot be empty.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        PromotionDAO.incrementIncludedInOrder(campaignId, itemId, quantity);
    }

    // records that an item was purchased

    public void recordPurchased(String campaignId, String itemId, int quantity) throws SQLException {
        if (campaignId == null || campaignId.isBlank()) {
            throw new IllegalArgumentException("Campaign ID cannot be empty.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        PromotionDAO.incrementPurchased(campaignId, itemId, quantity);
    }

    // marks a campaign as cancelled

    public void cancelCampaign(String campaignId) throws SQLException {
        if (campaignId == null || campaignId.isBlank()) {
            throw new IllegalArgumentException("Campaign ID cannot be empty.");
        }

        PromotionDAO.updateCampaignStatus(campaignId, "cancelled");
    }

    // for looking up a catalogue item

    public CatalogueItem findCatalogueItemById(String itemId) {
        for (CatalogueItem item : CatalogueDatabase.getCatalogueItems()) {
            if (item.getItem_id() == itemId) {
                return item;
            }
        }
        return null;
    }

    // reactivating a cancelled campaign

    public void reactivateCampaign(String campaignId) throws SQLException {
        if (campaignId == null || campaignId.isBlank()) {
            throw new IllegalArgumentException("Campaign ID cannot be empty.");
        }

        PromotionDAO.updateCampaignStatus(campaignId, "active");
    }

    // validation for a campaign before its inserted into the database

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