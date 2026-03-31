package com.example.catalogue;

import java.util.List;

public class CatalogueService {
    public static void printDatabase() {
        List<CatalogueItem> items = CatalogueDatabase.getCatalogueItems();

        System.out.println("Current Catalogue Database:");

        if (items.isEmpty()) {
            System.out.println("No items in the database.");
        } else {
            for (CatalogueItem item : items) {
                System.out.println("Item ID: " + item.getItem_id());
                System.out.println("Description: " + item.getDescription());
                System.out.println("Package Type: " + item.getPackage_type());
                System.out.println("Unit: " + item.getUnit());
                System.out.println("Units per Pack: " + item.getUnits_per_pack());
                System.out.println("Package Cost: " + item.getPackage_cost());
                System.out.println("Availability: " + item.getAvailability());
                System.out.println("Stock Limit: " + item.getStock_limit());
                System.out.println("Status: " + item.getStatus());
                System.out.println("Order Percentage: " + item.getOrder_percentage());
            }
        }
    }
}
