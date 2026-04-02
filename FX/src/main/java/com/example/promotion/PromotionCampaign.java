package com.example.promotion;

import java.time.LocalDateTime;

public class PromotionCampaign {
    private String campaignId;
    private String campaignName;
    private String campaignDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String status;
    private String discountMode;
    private Double defaultDiscountPct;
    private String createdBy;

    public PromotionCampaign() {
    }

    public PromotionCampaign(String campaignId, String campaignName, String campaignDescription,
                             LocalDateTime startDateTime, LocalDateTime endDateTime,
                             String status, String discountMode, Double defaultDiscountPct,
                             String createdBy) {
        this.campaignId = campaignId;
        this.campaignName = campaignName;
        this.campaignDescription = campaignDescription;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.status = status;
        this.discountMode = discountMode;
        this.defaultDiscountPct = defaultDiscountPct;
        this.createdBy = createdBy;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignDescription() {
        return campaignDescription;
    }

    public void setCampaignDescription(String campaignDescription) {
        this.campaignDescription = campaignDescription;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiscountMode() {
        return discountMode;
    }

    public void setDiscountMode(String discountMode) {
        this.discountMode = discountMode;
    }

    public Double getDefaultDiscountPct() {
        return defaultDiscountPct;
    }

    public void setDefaultDiscountPct(Double defaultDiscountPct) {
        this.defaultDiscountPct = defaultDiscountPct;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}