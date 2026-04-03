package com.example.catalogue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/catalogue")
public class CatalogueAPI {

    @PostMapping("/accept")
    public ResponseEntity<String> acceptCatalogueDatabase(@RequestBody List<CatalogueItem> catalogue) {
        try {
            CatalogueDatabase.updateCatalogueItems(catalogue);
            CatalogueSyncDAO.syncCatalogueToProductTable(catalogue);
            CatalogueService.printDatabase();
            return ResponseEntity.ok("Catalogue Database Accepted");
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to sync catalogue to Product table: " + e.getMessage());
        }
    }

    @GetMapping("/items")
    public List<CatalogueItem> getCatalogueItems() {
        return CatalogueDatabase.getCatalogueItems();
    }
}