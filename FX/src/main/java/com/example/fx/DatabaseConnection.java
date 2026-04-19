package com.example.fx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ipos_pu"; //MySQL URL
    private static final String USER = "root"; //MySQL username
    private static final String PASS = "Ipos2026@"; //MySQL password

    // This method opens and returns a connection to the MySQL database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

}
