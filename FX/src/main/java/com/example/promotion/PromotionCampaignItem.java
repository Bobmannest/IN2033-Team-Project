package com.example.promotion;

public class PromotionCampaignItem {
    private long campaignItemId;
    private String campaignId;
    private int itemId;
    private Double itemDiscountPct;

    public PromotionCampaignItem() {
    }

    public PromotionCampaignItem(long campaignItemId, String campaignId, int itemId, Double itemDiscountPct) {
        this.campaignItemId = campaignItemId;
        this.campaignId = campaignId;
        this.itemId = itemId;
        this.itemDiscountPct = itemDiscountPct;
    }

    public long getCampaignItemId() {
        return campaignItemId;
    }

    public void setCampaignItemId(long campaignItemId) {
        this.campaignItemId = campaignItemId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public Double getItemDiscountPct() {
        return itemDiscountPct;
    }

    public void setItemDiscountPct(Double itemDiscountPct) {
        this.itemDiscountPct = itemDiscountPct;
    }
}