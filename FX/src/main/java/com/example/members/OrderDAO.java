package com.example.members;

import com.example.fx.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Retrieves all past orders for a given member, ordered by most recent first
    public static List<Order> getOrdersForMember(String accountNo) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT order_id, member_account_no, created_at, total_amount, delivery_address, order_status FROM OnlineOrder WHERE member_account_no = ? ORDER BY created_at DESC";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, accountNo);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            // Build an Order object from each row, defaulting empty fields where necessary
            orders.add(new Order(
                    String.valueOf(rs.getLong("order_id")),
                    rs.getString("member_account_no"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getDouble("total_amount"),
                    rs.getString("delivery_address") != null ? rs.getString("delivery_address") : "",
                    rs.getString("order_status") != null ? rs.getString("order_status") : "pending"
            ));
        }

        return orders;
    }
}