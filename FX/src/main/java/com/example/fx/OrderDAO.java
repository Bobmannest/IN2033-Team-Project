package com.example.fx;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public static List<Order> getOrdersForMember(String accountNo) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE account_no = ? ORDER BY order_date DESC";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, accountNo);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            orders.add(new Order(
                    rs.getString("order_id"),
                    rs.getString("account_no"),
                    rs.getTimestamp("order_date").toLocalDateTime(),
                    rs.getDouble("total_amount"),
                    rs.getString("delivery_address")
            ));
        }

        return orders;
    }
}