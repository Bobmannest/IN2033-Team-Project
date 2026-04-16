package com.example.basket;

import com.example.catalogue.CatalogueItem;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasketList {
    private static List<CatalogueItem> basket_items = new ArrayList<>();
    private static BasketController listener;

    private static final Map<CatalogueItem, Double> finalPriceMap = new HashMap<>();
    private static final Map<CatalogueItem, Double> discountPctMap = new HashMap<>();
    private static final Map<CatalogueItem, String> campaignIdMap = new HashMap<>();

    public static void setListener(BasketController l) {
        listener = l;
    }

    private static void notifyListener() {
        if (listener != null) {
            Platform.runLater(() -> listener.displayItems());
        }
    }

    public static void addItem(CatalogueItem item) {
        basket_items.add(item);
        finalPriceMap.put(item, item.getPackage_cost());
        discountPctMap.put(item, 0.0);
        campaignIdMap.put(item, null);
        notifyListener();
    }

    public static void addItem(CatalogueItem item, double discountPct, double finalPrice, String campaignId) {
        basket_items.add(item);
        finalPriceMap.put(item, finalPrice);
        discountPctMap.put(item, discountPct);
        campaignIdMap.put(item, campaignId);
        notifyListener();
    }

    public static List<CatalogueItem> getBasketItems() {
        return basket_items;
    }

    public static double getFinalPrice(CatalogueItem item) {
        return finalPriceMap.getOrDefault(item, item.getPackage_cost());
    }

    public static double getDiscountPct(CatalogueItem item) {
        return discountPctMap.getOrDefault(item, 0.0);
    }

    public static String getCampaignId(CatalogueItem item) {
        return campaignIdMap.get(item);
    }

    public static void removeItem(CatalogueItem item) {
        basket_items.remove(item);
        finalPriceMap.remove(item);
        discountPctMap.remove(item);
        campaignIdMap.remove(item);
        notifyListener();
    }

    public static void clear() {
        basket_items.clear();
        finalPriceMap.clear();
        discountPctMap.clear();
        campaignIdMap.clear();
        notifyListener();
    }
}