package com.example.promotion;

import com.example.fx.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

// class for loading the sample data related to promotions/campaigns and their individual campaign items

public class PromotionSampleDataLoader {

    // creates the sample campaigns if they do not already exist, and then it adds the items for those campaigns

    public static void loadSampleData() {
        try {
            PromotionService service = new PromotionService();

            if (PromotionDAO.getCampaignById("CAMP0001") == null) {
                PromotionCampaign marchPromotion = new PromotionCampaign(
                        "CAMP0001",
                        "March Promotion",
                        "Paracetamol 5%, Aspirin 10%, Analgin 10%, Celebrex caps 100mg 20%",
                        LocalDateTime.of(2026, 4, 5, 0, 0),
                        LocalDateTime.of(2026, 4, 20, 23, 59),
                        "active",
                        "variable",
                        null,
                        "manager"
                );
                service.createCampaign(marchPromotion);
                System.out.println("Created CAMP0001");
            }

            addIfProductExists("CAMP0001", "100 00001", 5.0);
            addIfProductExists("CAMP0001", "100 00002", 10.0);
            addIfProductExists("CAMP0001", "100 00003", 10.0);
            addIfProductExists("CAMP0001", "100 00004", 20.0);

            if (PromotionDAO.getCampaignById("CAMP0002") == null) {
                PromotionCampaign aprilPromotion = new PromotionCampaign(
                        "CAMP0002",
                        "April Promotion",
                        "Ospen 20% , Vitamin C 10%",
                        LocalDateTime.of(2026, 4, 5, 0, 0),
                        LocalDateTime.of(2026, 4, 20, 23, 59),
                        "active",
                        "variable",
                        null,
                        "manager"
                );
                service.createCampaign(aprilPromotion);
                System.out.println("Created CAMP0002");
            }

            addIfProductExists("CAMP0002", "300 00001", 20.0);
            addIfProductExists("CAMP0002", "400 00001", 10.0);

            System.out.println("Promotion sample data load finished.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // adds an item to a campaign if the product exists and it isn't already added to it

    private static void addIfProductExists(String campaignId, String itemId, Double discount) {
        try {
            if (!productExists(itemId)) {
                System.out.println("Skipped missing product " + itemId + " for " + campaignId);
                return;
            }

            if (campaignItemAlreadyExists(campaignId, itemId)) {
                System.out.println("Campaign item already exists: " + campaignId + " / " + itemId);
                return;
            }

            PromotionDAO.addItemToCampaign(campaignId, itemId, discount);
            System.out.println("Added item " + itemId + " to " + campaignId);

        } catch (Exception e) {
            System.out.println("Failed adding item " + itemId + " to " + campaignId);
            e.printStackTrace();
        }
    }

    private static boolean productExists(String itemId) throws Exception {
        String sql = "SELECT 1 FROM Product WHERE item_id = ? LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, itemId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static boolean campaignItemAlreadyExists(String campaignId, String itemId) throws Exception {
        String sql = "SELECT 1 FROM PromotionCampaignItem WHERE campaign_id = ? AND item_id = ? LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, campaignId);
            ps.setString(2, itemId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}