package com.example.basket;

import com.example.catalogue.CatalogueItem;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class BasketList {
    private static List<CatalogueItem> basket_items = new ArrayList<>();
    private static BasketController listener;

    //Listener Methods
    public static void setListener(BasketController l) {listener = l;}

    private static void notifyListener() {
        if (listener != null) {
            Platform.runLater(() -> listener.displayItems());
        }
    }

    //Basket Methods
    public static void addItem(CatalogueItem item) {basket_items.add(item);}

    public static List<CatalogueItem> getBasketItems() {return basket_items;}

    public static void removeItem(CatalogueItem item) {
        basket_items.remove(item);
        notifyListener();
    }

    public static void clear() {basket_items.clear();}
}
