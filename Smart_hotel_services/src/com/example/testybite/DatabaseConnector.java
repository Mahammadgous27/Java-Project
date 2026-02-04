package com.example.testybite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/Hotel_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "manager";
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    // ------------------ GET CONNECTION ------------------
    public static Connection getConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("✅ Database connected successfully!");
            return c;
        } catch (Exception e) {
            System.err.println("❌ Database Connection Error: " + e.getMessage());
            return null;
        }
    }

    // ------------------ LOGIN CHECK ------------------
    public static boolean checkLogin(String email, String password, String role) {
        String table = "Customer";
        if ("owner".equalsIgnoreCase(role)) {
            table = "hotel_own"; // ✅ Correct table name
        }

        String sql = "SELECT * FROM `" + table + "` WHERE `email` = ? AND `password` = ?";
        System.out.println("Checking login in table: " + table);
        System.out.println("Email: '" + email + "', Password: '" + password + "'");

        try (Connection conn = getConnection();
             PreparedStatement pst = (conn != null) ? conn.prepareStatement(sql) : null) {

            if (pst == null) return false;

            pst.setString(1, email.trim());
            pst.setString(2, password.trim());

            try (ResultSet rs = pst.executeQuery()) {
                boolean found = rs.next();
                System.out.println("Login found: " + found);
                return found;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ------------------ INSERT OWNER ------------------
    public static boolean insertOwner(String name, String email, String phone, String password) {
        String sql = "INSERT INTO `hotel_own` (`name`, `email`, `phone`, `password`) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pst = (conn != null) ? conn.prepareStatement(sql) : null) {

            if (pst == null) return false;

            pst.setString(1, name.trim());
            pst.setString(2, email.trim());
            pst.setString(3, phone.trim());
            pst.setString(4, password.trim());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ------------------ INSERT CUSTOMER ------------------
    public static boolean insertCustomer(String name, String email, String phone, String password) {
        String sql = "INSERT INTO `Customer` (`name`, `email`, `phone`, `password`) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pst = (conn != null) ? conn.prepareStatement(sql) : null) {

            if (pst == null) return false;

            pst.setString(1, name.trim());
            pst.setString(2, email.trim());
            pst.setString(3, phone.trim());
            pst.setString(4, password.trim());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ------------------ BOOK DINING ------------------
    public static boolean bookDining(String customerName, java.sql.Date sqlDate, String time, int numPersons) {
        String sql = "INSERT INTO bookDinning (Customer_name, dateOfBooking, timing, noOfPersons) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, customerName);         // Customer_name VARCHAR(20)
            pst.setDate(2, sqlDate);               // dateOfBooking DATE
            pst.setTime(3, java.sql.Time.valueOf(time + ":00")); // timing TIME
            pst.setInt(4, numPersons);             // noOfPersons INT

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // ------------------ HOTEL DETAILS ------------------
    public static boolean insertHotelDetails(String hotelName, double rating) {
        String sql = "INSERT INTO `HotelDetailsPanel` (`hotel_name`, `rating`) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pst = (conn != null) ? conn.prepareStatement(sql) : null) {

            if (pst == null) return false;

            pst.setString(1, hotelName.trim());
            pst.setDouble(2, rating);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getAllHotelNames() {
        List<String> hotels = new ArrayList<>();
        String sql = "SELECT `hotel_name` FROM `HotelDetailsPanel`";

        try (Connection conn = getConnection();
             Statement stmt = (conn != null) ? conn.createStatement() : null;
             ResultSet rs = (stmt != null) ? stmt.executeQuery(sql) : null) {

            if (rs != null) {
                while (rs.next()) {
                    hotels.add(rs.getString("hotel_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotels;
    }

    public static double getHotelRating(String hotelName) {
        double rating = 0.0;
        String sql = "SELECT `rating` FROM `HotelDetailsPanel` WHERE `hotel_name` = ?";

        try (Connection conn = getConnection();
             PreparedStatement pst = (conn != null) ? conn.prepareStatement(sql) : null) {

            if (pst == null) return rating;

            pst.setString(1, hotelName.trim());
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    rating = rs.getDouble("rating");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rating;
    }

    // ------------------ INSERT ORDER ------------------
    public static boolean insertOrder(int reservationId, int staffId, String itemName, double itemPrice, int quantity, java.sql.Timestamp orderTime, String status) {
        String sql = "INSERT INTO Orders (reservation_id, staff_id, item_name, item_price, quantity, order_time, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, reservationId);
            pst.setInt(2, staffId);
            pst.setString(3, itemName);
            pst.setDouble(4, itemPrice);
            pst.setInt(5, quantity);
            pst.setTimestamp(6, orderTime);
            pst.setString(7, status);

            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // ------------------ TEST CONNECTION ------------------
    public static void main(String[] args) {
        Connection c = getConnection();
        if (c != null) {
            try { c.close(); } catch (SQLException ignored) {}
            System.out.println("✅ Connection Test Passed!");
        } else {
            System.out.println("❌ Connection Test Failed!");
        }

        List<String> hotels = getAllHotelNames();
        System.out.println("Hotels: " + hotels);
    }
}
