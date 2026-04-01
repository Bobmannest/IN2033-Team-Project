package com.example.catalogue;

import javafx.application.Platform;
import java.util.ArrayList;
import java.util.List;

public class CatalogueDatabase {
    private static CatalogueController listener;
    private static List<CatalogueItem> catalogue_items = new ArrayList<>();

    public static void setListener(CatalogueController lis) {
        listener = lis;
    }

    public static void updateCatalogueItems(List<CatalogueItem> incoming_catalogue_items) {
        catalogue_items = incoming_catalogue_items;
        if (listener != null) {Platform.runLater(() -> listener.displayItems());}
    }

    public static List<CatalogueItem> getCatalogueItems() {
        return catalogue_items;
    }
}
