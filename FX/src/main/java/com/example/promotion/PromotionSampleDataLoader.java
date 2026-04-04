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
                        "Promotion campaign created from sample data",
                        LocalDateTime.of(2026, 3, 15, 0, 0),
                        LocalDateTime.of(2026, 4, 20, 23, 59),
                        "active",
                        "variable",
                        null,
                        null
                );
                service.createCampaign(marchPromotion);
                System.out.println("Created CAMP0001");
            }

            addIfItemExists(service, "CAMP0001", 10000002, 5.0);
            addIfItemExists(service, "CAMP0001", 10000003, 10.0);
            addIfItemExists(service, "CAMP0001", 10000004, 10.0);
            addIfItemExists(service, "CAMP0001", 10000006, 20.0);

            if (PromotionDAO.getCampaignById("CAMP0002") == null) {
                PromotionCampaign aprilPromotion = new PromotionCampaign(
                        "CAMP0002",
                        "April Promotion",
                        "Promotion campaign created from sample data",
                        LocalDateTime.of(2026, 4, 5, 0, 0),
                        LocalDateTime.of(2026, 4, 10, 23, 59),
                        "active",
                        "variable",
                        null,
                        null
                );
                service.createCampaign(aprilPromotion);
                System.out.println("Created CAMP0002");
            }

            addIfItemExists(service, "CAMP0002", 30000001, 20.0);
            addIfItemExists(service, "CAMP0002", 40000001, 10.0);

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