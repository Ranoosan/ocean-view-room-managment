package model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        Connection con = null;
        try {
            System.out.println("Loading MySQL JDBC Driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully.");

            System.out.println("Connecting to database...");
            con = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/oceanview", // database URL
                "root",                                  // XAMPP default user
                ""                                       // XAMPP default password (empty)
            );

            if (con != null) {
                System.out.println("Database connection established ✅");
            } else {
                System.out.println("Failed to establish connection ❌");
            }
        } catch(Exception e) {
            System.out.println("Error while connecting to database:");
            e.printStackTrace();
        }
        return con;
    }
}