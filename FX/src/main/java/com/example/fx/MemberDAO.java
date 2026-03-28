package com.example.fx;

import java.sql.*;
import java.security.SecureRandom;

public class MemberDAO {

    private static final String PASSWORD_CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";

    // This will check whether or not the email and pass match a member in the db
    public static Member login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM Member WHERE email = ? AND password = ?";

        // opening connection to the db
        Connection conn = DatabaseConnection.getConnection();

        // learnt abt prepared statements, ? are placeholders
        // prevents SQL injection
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ps.setString(2, password);

        // runs the query
        ResultSet rs = ps.executeQuery();

        // if a row came back, with credentials matched it builds and returns the member
        if (rs.next()) {
            return new Member(
                    rs.getString("account_no"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("member_type"),
                    rs.getBoolean("is_first_login"),
                    rs.getInt("order_count")
            );
        }
        // if no match found
        return null;
    }

    // registers a new non-commercial member - generates accountNo, random pass and stores in db
    public static String registerNonCommercialMember(String email)
            throws SQLException, IllegalArgumentException {

        Connection conn = DatabaseConnection.getConnection();

        // checks if the email exists already
        if (emailExists(conn, email)) {
            throw new IllegalArgumentException("This email is already registered.");
        }

        // generate PU0001, PU0002 etc.
        String accountNo = generateNextAccountNo(conn);

        // creates a random 10 character password
        String generatedPassword = generateRandomPassword(10);

        // inserts new member into the database
        String sql = "INSERT INTO Member (account_no, email, password, member_type, is_first_login, order_count) "
                + "VALUES (?, ?, ?, 'non_commercial', TRUE, 0)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, accountNo);
        ps.setString(2, email);
        ps.setString(3, generatedPassword);
        ps.executeUpdate();

        return generatedPassword;
    }

    // updates members pass - calls when member changes pass for the first time
    public static void changePassword(String accountNo, String newPassword)
            throws SQLException {
        String sql = "UPDATE Member SET password = ?, is_first_login = FALSE WHERE account_no = ?";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, newPassword);
        ps.setString(2, accountNo);
        ps.executeUpdate();
    }

    // checks if email already exists in members table
    private static boolean emailExists(Connection conn, String email) throws SQLException {
        String sql = "SELECT 1 FROM Member WHERE email = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    // generates the next account num in the format PU0001
    private static String generateNextAccountNo(Connection conn) throws SQLException {
        String sql = "SELECT account_no FROM Member WHERE account_no LIKE 'PU%' "
                + "ORDER BY account_no DESC LIMIT 1";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String last = rs.getString("account_no");
            int number = Integer.parseInt(last.substring(2));
            return String.format("PU%04d", number + 1);
        } else {
            return "PU0001";
        }
    }

    // generates random pass of given length
    private static String generateRandomPassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(PASSWORD_CHARS.charAt(random.nextInt(PASSWORD_CHARS.length())));
        }
        return sb.toString();
    }
}