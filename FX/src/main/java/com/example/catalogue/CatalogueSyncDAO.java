package com.example.catalogue;

import com.example.fx.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CatalogueSyncDAO {

    public static void syncCatalogueToProductTable(List<CatalogueItem> items) throws SQLException {
        String sql = """
                INSERT INTO Product
                (item_id, product_name, package_type, unit, units_in_pack,
                 bulk_cost, quantity_available, stock_limit, is_active)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    product_name = VALUES(product_name),
                    package_type = VALUES(package_type),
                    unit = VALUES(unit),
                    units_in_pack = VALUES(units_in_pack),
                    bulk_cost = VALUES(bulk_cost),
                    quantity_available = VALUES(quantity_available),
                    stock_limit = VALUES(stock_limit),
                    is_active = VALUES(is_active)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (CatalogueItem item : items) {
                ps.setString(1, item.getItem_id());
                ps.setString(2, item.getDescription());
                ps.setString(3, item.getPackage_type());
                ps.setString(4, item.getUnit());
                ps.setInt(5, item.getUnits_per_pack());
                ps.setDouble(6, item.getPackage_cost());
                ps.setInt(7, item.getAvailability());
                ps.setInt(8, item.getStock_limit());

                boolean isActive = item.getStatus() == null
                        || !item.getStatus().equalsIgnoreCase("discontinued");
                ps.setBoolean(9, isActive);

                ps.addBatch();
            }

            ps.executeBatch();
        }
    }
}