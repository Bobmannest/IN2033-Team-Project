package com.example.members;

import com.example.fx.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommercialApplicationDAO {

    public static void submitApplication(CommercialApplication app) throws SQLException {
        String sql = "INSERT INTO CommercialApplication (application_id, company_name, companies_house_no, " +
                "director_name, business_type, address, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, app.getApplicationId());
        ps.setString(2, app.getCompanyName());
        ps.setString(3, app.getCompaniesHouseNo());
        ps.setString(4, app.getDirectorName());
        ps.setString(5, app.getBusinessType());
        ps.setString(6, app.getAddress());
        ps.setString(7, app.getEmail());
        ps.executeUpdate();
        sendToSA(app);
    }

    // send to sa mock integration
    private static void sendToSA(CommercialApplication app) {
        try {
            String json = String.format("""
                {
                  "application_id": "%s",
                  "company_name": "%s",
                  "companies_house_no": "%s",
                  "director_name": "%s",
                  "business_type": "%s",
                  "address": "%s",
                  "email": "%s",
                  "status": "pending"
                }
                """,
                    app.getApplicationId(), app.getCompanyName(),
                    app.getCompaniesHouseNo(), app.getDirectorName(),
                    app.getBusinessType(), app.getAddress(), app.getEmail()
            );

            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create("http://SA_TEAM_IP:PORT/api/applications")) // dummy endpoint
                    .header("Content-Type", "application/json")
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            System.out.println("Application sent to SA successfully.");
        } catch (Exception e) {
            System.out.println("Could not send to SA (non-critical): " + e.getMessage());
        }
    }

    public static boolean emailExists(String email) throws SQLException {
        String sql = "SELECT 1 FROM CommercialApplication WHERE email = ?";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public static boolean companiesHouseNoExists(String companiesHouseNo) throws SQLException {
        String sql = "SELECT 1 FROM CommercialApplication WHERE companies_house_no = ?";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, companiesHouseNo);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public static String generateNextApplicationId() throws SQLException {
        String sql = "SELECT application_id FROM CommercialApplication WHERE application_id LIKE 'CA%' " +
                "ORDER BY application_id DESC LIMIT 1";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String last = rs.getString("application_id");
            int number = Integer.parseInt(last.substring(2));
            return String.format("CA%04d", number + 1);
        }
        return "CA0001";
    }
}