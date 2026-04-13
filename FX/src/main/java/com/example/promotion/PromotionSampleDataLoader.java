package com.example.promotion;

import com.example.catalogue.CatalogueItem;

import java.time.LocalDateTime;

public class PromotionSampleDataLoader {

    public static void loadSampleData() {
        try {
            PromotionService service = new PromotionService();

            if (PromotionDAO.getCampaignById("CAMP0001") == null) {
                PromotionCampaign marchPromotion = new PromotionCampaign(
                        "CAMP0001",
                        "March Promotion",
                        "Aspirin 5%, Analgin 10%, Celebrex 100mg 10%, Retin-A Tretin 30g 20%",
                        LocalDateTime.of(2026, 4, 12, 0, 0),
                        LocalDateTime.of(2026, 4, 20, 23, 59),
                        "active",
                        "variable",
                        null,
                        null
                );
                service.createCampaign(marchPromotion);
                System.out.println("Created CAMP0001");
            }

            addIfItemExists(service, "CAMP0001", 1, 5.0);
            addIfItemExists(service, "CAMP0001", 2, 10.0);
            addIfItemExists(service, "CAMP0001", 3, 10.0);
            addIfItemExists(service, "CAMP0001", 4, 20.0);

            if (PromotionDAO.getCampaignById("CAMP0002") == null) {
                PromotionCampaign aprilPromotion = new PromotionCampaign(
                        "CAMP0002",
                        "April Promotion",
                        "Ospen 20%, Vitamin C 10%",
                        LocalDateTime.of(2026, 4, 12, 0, 0),
                        LocalDateTime.of(2026, 4, 20, 23, 59),
                        "active",
                        "variable",
                        null,
                        null
                );
                service.createCampaign(aprilPromotion);
                System.out.println("Created CAMP0002");
            }

            addIfItemExists(service, "CAMP0002", 3, 20.0);
            addIfItemExists(service, "CAMP0002", 4, 10.0);

            System.out.println("Promotion sample data load finished.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addIfItemExists(PromotionService service, String campaignId, int itemId, Double discount) {
        try {
            CatalogueItem item = service.findCatalogueItemById(itemId);

            if (item != null) {
                service.addItemToCampaign(campaignId, itemId, discount);
                System.out.println("Added item " + itemId + " to " + campaignId);
            } else {
                System.out.println("Skipped missing catalogue item " + itemId + " for " + campaignId);
            }
        } catch (Exception e) {
            System.out.println("Failed adding item " + itemId + " to " + campaignId);
            e.printStackTrace();
        }
    }
}