package com.example.promotion;

import com.example.catalogue.CatalogueDatabase;
import com.example.catalogue.CatalogueItem;
import com.example.fx.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PromotionDAO {

    public static void createCampaign(PromotionCampaign campaign) throws SQLException {
        String sql = """
                INSERT INTO PromotionCampaign
                (campaign_id, campaign_name, campaign_description, start_datetime, end_datetime,
                 status, discount_mode, default_discount_pct, created_by)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, campaign.getCampaignId());
            ps.setString(2, campaign.getCampaignName());
            ps.setString(3, campaign.getCampaignDescription());
            ps.setTimestamp(4, Timestamp.valueOf(campaign.getStartDateTime()));
            ps.setTimestamp(5, Timestamp.valueOf(campaign.getEndDateTime()));
            ps.setString(6, campaign.getStatus());
            ps.setString(7, campaign.getDiscountMode());

            if (campaign.getDefaultDiscountPct() != null) {
                ps.setDouble(8, campaign.getDefaultDiscountPct());
            } else {
                ps.setNull(8, Types.DECIMAL);
            }

            if (campaign.getCreatedBy() != null && !campaign.getCreatedBy().isBlank()) {
                ps.setString(9, campaign.getCreatedBy());
            } else {
                ps.setNull(9, Types.VARCHAR);
            }

            ps.executeUpdate();
        }

        createCampaignMetricsRow(campaign.getCampaignId());
    }

    public static String generateNextCampaignId() throws SQLException {
        String sql = "SELECT campaign_id FROM PromotionCampaign " +
                "WHERE campaign_id LIKE 'CAMP%' " +
                "ORDER BY campaign_id DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String last = rs.getString("campaign_id");   // e.g. CAMP0002
                int number = Integer.parseInt(last.substring(4));
                return String.format("CAMP%04d", number + 1);
            } else {
                return "CAMP0001";
            }
        }
    }

    public static void addItemToCampaign(String campaignId, String itemId, Double itemDiscountPct) throws SQLException {
        String sql = """
                INSERT INTO PromotionCampaignItem
                (campaign_id, item_id, item_discount_pct)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, campaignId);
            ps.setString(2, itemId);

            if (itemDiscountPct != null) {
                ps.setDouble(3, itemDiscountPct);
            } else {
                ps.setNull(3, Types.DECIMAL);
            }

            ps.executeUpdate();
        }

        createCampaignItemMetricsRow(campaignId, itemId);
    }

    public static PromotionCampaign getCampaignById(String campaignId) throws SQLException {
        String sql = "SELECT * FROM PromotionCampaign WHERE campaign_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, campaignId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCampaign(rs);
                }
            }
        }

        return null;
    }

    public static List<PromotionCampaign> getAllCampaigns() throws SQLException {
        List<PromotionCampaign> campaigns = new ArrayList<>();
        String sql = "SELECT * FROM PromotionCampaign ORDER BY start_datetime DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                campaigns.add(mapCampaign(rs));
            }
        }

        return campaigns;
    }

    public static List<PromotionCampaign> getActiveCampaigns() throws SQLException {
        List<PromotionCampaign> campaigns = new ArrayList<>();

        String sql = """
                SELECT *
                FROM PromotionCampaign
                WHERE status = 'active'
                  AND start_datetime <= NOW()
                  AND end_datetime >= NOW()
                ORDER BY start_datetime ASC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                campaigns.add(mapCampaign(rs));
            }
        }

        return campaigns;
    }

    public static List<PromotionCampaign> getActiveCampaignsForItem(String itemId) throws SQLException {
        String sql = """
            SELECT DISTINCT pc.*
            FROM PromotionCampaign pc
            JOIN PromotionCampaignItem pci
              ON pc.campaign_id = pci.campaign_id
            WHERE pci.item_id = ?
              AND pc.status = 'active'
              AND pc.start_datetime <= NOW()
              AND pc.end_datetime >= NOW()
            ORDER BY pc.start_datetime ASC
            """;

        List<PromotionCampaign> campaigns = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, itemId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    campaigns.add(mapCampaign(rs));
                }
            }
        }
        return campaigns;
    }

    public static List<CatalogueItem> getCampaignCatalogueItems(String campaignId) throws SQLException {
        String sql = "SELECT item_id FROM PromotionCampaignItem WHERE campaign_id = ?";
        List<String> campaignItemIds = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, campaignId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    campaignItemIds.add(rs.getString("item_id"));
                }
            }
        }

        List<CatalogueItem> allItems = CatalogueDatabase.getCatalogueItems();
        List<CatalogueItem> result = new ArrayList<>();

        for (CatalogueItem item : allItems) {
            if (campaignItemIds.contains(item.getItem_id())) {
                result.add(item);
            }
        }

        return result;
    }

    public static void updateCampaignDetails(
            String campaignId,
            String description,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) throws SQLException {
        String sql = """
            UPDATE PromotionCampaign
            SET campaign_description = ?, start_datetime = ?, end_datetime = ?
            WHERE campaign_id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, description);
            ps.setTimestamp(2, Timestamp.valueOf(startDateTime));
            ps.setTimestamp(3, Timestamp.valueOf(endDateTime));
            ps.setString(4, campaignId);

            ps.executeUpdate();
        }
    }

    public static Double getDiscountForItem(String campaignId, String itemId) throws SQLException {
        String sql = """
                SELECT pci.item_discount_pct, pc.default_discount_pct
                FROM PromotionCampaignItem pci
                JOIN PromotionCampaign pc ON pci.campaign_id = pc.campaign_id
                WHERE pci.campaign_id = ? AND pci.item_id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, campaignId);
            ps.setString(2, itemId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double itemDiscount = rs.getDouble("item_discount_pct");
                    boolean itemDiscountWasNull = rs.wasNull();

                    if (!itemDiscountWasNull) {
                        return itemDiscount;
                    }

                    double defaultDiscount = rs.getDouble("default_discount_pct");
                    boolean defaultDiscountWasNull = rs.wasNull();

                    if (!defaultDiscountWasNull) {
                        return defaultDiscount;
                    }
                }
            }
        }

        return 0.0;
    }

    public static void incrementCampaignHits(String campaignId) throws SQLException {
        ensureCampaignMetricsRowExists(campaignId);

        String sql = """
                UPDATE CampaignMetrics
                SET campaign_hits = campaign_hits + 1
                WHERE campaign_id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, campaignId);
            ps.executeUpdate();
        }
    }

    public static void incrementIncludedInOrder(String campaignId, String itemId, int quantity) throws SQLException {
        ensureCampaignItemMetricsRowExists(campaignId, itemId);

        String sql = """
                UPDATE CampaignItemMetrics
                SET included_in_order_count = included_in_order_count + ?
                WHERE campaign_id = ? AND item_id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setString(2, campaignId);
            ps.setString(3, itemId);
            ps.executeUpdate();
        }
    }

    public static void incrementPurchased(String campaignId, String itemId, int quantity) throws SQLException {
        ensureCampaignItemMetricsRowExists(campaignId, itemId);

        String sql = """
                UPDATE CampaignItemMetrics
                SET purchased_count = purchased_count + ?
                WHERE campaign_id = ? AND item_id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setString(2, campaignId);
            ps.setString(3, itemId);
            ps.executeUpdate();
        }
    }

    public static void updateCampaignStatus(String campaignId, String status) throws SQLException {
        String sql = "UPDATE PromotionCampaign SET status = ? WHERE campaign_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, campaignId);
            ps.executeUpdate();
        }
    }

    public static void deleteCampaign(String campaignId) throws SQLException {
        String sql = "DELETE FROM PromotionCampaign WHERE campaign_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, campaignId);
            ps.executeUpdate();
        }
    }

    public static void deleteItemFromCampaign(String campaignId, String itemId) throws SQLException {
        String sql = "DELETE FROM PromotionCampaignItem WHERE campaign_id = ? AND item_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, campaignId);
            ps.setString(2, itemId);
            ps.executeUpdate();
        }
    }

    public static List<String> getCampaignItemLines(String campaignId) throws SQLException {
        String sql = """
                SELECT pci.item_id, p.product_name, pci.item_discount_pct
                FROM PromotionCampaignItem pci
                LEFT JOIN Product p ON pci.item_id = p.item_id
                WHERE pci.campaign_id = ?
                ORDER BY pci.item_id
                """;

        List<String> lines = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, campaignId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String itemId = rs.getString("item_id");
                    String productName = rs.getString("product_name");

                    Double discount = rs.getDouble("item_discount_pct");
                    if (rs.wasNull()) {
                        discount = null;
                    }

                    String line = itemId + " - "
                            + (productName != null ? productName : "Unknown Item")
                            + " | Discount: "
                            + (discount != null ? discount + "%" : "Campaign Default");

                    lines.add(line);
                }
            }
        }

        return lines;
    }

    private static void createCampaignMetricsRow(String campaignId) throws SQLException {
        String sql = """
                INSERT IGNORE INTO CampaignMetrics (campaign_id, campaign_hits)
                VALUES (?, 0)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, campaignId);
            ps.executeUpdate();
        }
    }

    private static void createCampaignItemMetricsRow(String campaignId, String itemId) throws SQLException {
        String sql = """
                INSERT IGNORE INTO CampaignItemMetrics
                (campaign_id, item_id, included_in_order_count, purchased_count)
                VALUES (?, ?, 0, 0)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, campaignId);
            ps.setString(2, itemId);
            ps.executeUpdate();
        }
    }

    private static void ensureCampaignMetricsRowExists(String campaignId) throws SQLException {
        createCampaignMetricsRow(campaignId);
    }

    private static void ensureCampaignItemMetricsRowExists(String campaignId, String itemId) throws SQLException {
        createCampaignItemMetricsRow(campaignId, itemId);
    }

    public static List<PromotionCampaign> findOverlappingCampaignsForItem(
            String itemId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            String excludeCampaignId
    ) throws SQLException {

        String sql = """
            SELECT DISTINCT pc.*
            FROM PromotionCampaign pc
            JOIN PromotionCampaignItem pci
              ON pc.campaign_id = pci.campaign_id
            WHERE pci.item_id = ?
              AND pc.campaign_id <> ?
              AND pc.status IN ('scheduled', 'active')
              AND pc.start_datetime <= ?
              AND pc.end_datetime >= ?
            """;

        List<PromotionCampaign> overlaps = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, itemId);
            ps.setString(2, excludeCampaignId);
            ps.setTimestamp(3, Timestamp.valueOf(endDateTime));
            ps.setTimestamp(4, Timestamp.valueOf(startDateTime));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    overlaps.add(mapCampaign(rs));
                }
            }
        }

        return overlaps;
    }

    public static boolean campaignItemExists(String campaignId, String itemId) throws SQLException {
        String sql = """
            SELECT 1
            FROM PromotionCampaignItem
            WHERE campaign_id = ? AND item_id = ?
            LIMIT 1
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, campaignId);
            ps.setString(2, itemId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static PromotionCampaign mapCampaign(ResultSet rs) throws SQLException {
        Timestamp startTs = rs.getTimestamp("start_datetime");
        Timestamp endTs = rs.getTimestamp("end_datetime");

        Double defaultDiscount = rs.getDouble("default_discount_pct");
        if (rs.wasNull()) {
            defaultDiscount = null;
        }

        return new PromotionCampaign(
                rs.getString("campaign_id"),
                rs.getString("campaign_name"),
                rs.getString("campaign_description"),
                startTs != null ? startTs.toLocalDateTime() : null,
                endTs != null ? endTs.toLocalDateTime() : null,
                rs.getString("status"),
                rs.getString("discount_mode"),
                defaultDiscount,
                rs.getString("created_by")
        );
    }
}