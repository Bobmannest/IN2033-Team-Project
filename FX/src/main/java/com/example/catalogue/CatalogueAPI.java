package com.example.catalogue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogue")
public class CatalogueAPI {
    @PostMapping("/accept")
    public ResponseEntity<String> acceptCatalogueDatabase(@RequestBody List<CatalogueItem> catalogue) {
        CatalogueDatabase.updateCatalogueItems(catalogue);
        CatalogueService.printDatabase();
        return ResponseEntity.ok("Sales Database Accepted");
    }

    @GetMapping("/items")
    public List<CatalogueItem> getCatalogueItems() {
        return CatalogueDatabase.getCatalogueItems();
    }
}
