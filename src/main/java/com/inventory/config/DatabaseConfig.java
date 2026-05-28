package com.inventory.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {
    
    private static final String URL = "jdbc:mysql://localhost:3306/inventory_db";
    private static final String USER = "root";
    private static final String PASSWORD = "MAJINU^25PREETHA>2609"; // Your password here
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load database driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS products (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "name VARCHAR(255) NOT NULL," +
            "price DECIMAL(10, 2) NOT NULL," +
            "quantity INT NOT NULL DEFAULT 0," +
            "low_stock_threshold INT NOT NULL DEFAULT 10," +
            "total_sales INT NOT NULL DEFAULT 0," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
            ")";

        String createSalesTableSQL = "CREATE TABLE IF NOT EXISTS sales (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "product_id INT NOT NULL," +
            "quantity_sold INT NOT NULL," +
            "sale_price DECIMAL(10, 2) NOT NULL," +
            "sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE" +
            ")";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(createTableSQL);
            stmt.execute(createSalesTableSQL);
            System.out.println("✅ Database initialized successfully!");

            // Seed sample products if table is empty
            seedSampleProducts(conn);
            
        } catch (SQLException e) {
            System.err.println("❌ Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Insert a few sample products into the products table if it's empty.
     */
    private static void seedSampleProducts(Connection conn) {
        String countSQL = "SELECT COUNT(*) FROM products";
        String insertSQL = "INSERT INTO products (name, price, quantity, low_stock_threshold, total_sales) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement countStmt = conn.prepareStatement(countSQL);
             ResultSet rs = countStmt.executeQuery()) {

            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }

            if (count == 0) {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                    // Sample product 1
                    insertStmt.setString(1, "Pen");
                    insertStmt.setBigDecimal(2, new java.math.BigDecimal("1.50"));
                    insertStmt.setInt(3, 100);
                    insertStmt.setInt(4, 10);
                    insertStmt.setInt(5, 0);
                    insertStmt.executeUpdate();

                    // Sample product 2
                    insertStmt.setString(1, "Notebook");
                    insertStmt.setBigDecimal(2, new java.math.BigDecimal("3.99"));
                    insertStmt.setInt(3, 50);
                    insertStmt.setInt(4, 10);
                    insertStmt.setInt(5, 0);
                    insertStmt.executeUpdate();

                    // Sample product 3
                    insertStmt.setString(1, "Eraser");
                    insertStmt.setBigDecimal(2, new java.math.BigDecimal("0.50"));
                    insertStmt.setInt(3, 200);
                    insertStmt.setInt(4, 5);
                    insertStmt.setInt(5, 0);
                    insertStmt.executeUpdate();

                    // Sample product 4
                    insertStmt.setString(1, "Pencil");
                    insertStmt.setBigDecimal(2, new java.math.BigDecimal("0.75"));
                    insertStmt.setInt(3, 150);
                    insertStmt.setInt(4, 5);
                    insertStmt.setInt(5, 0);
                    insertStmt.executeUpdate();

                    System.out.println("✅ Sample products inserted into products table.");
                }
            } else {
                System.out.println("ℹ️ Products table already contains data (count=" + count + "), skipping seeding.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error seeding sample products: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Database connection successful!");
                System.out.println("Database: " + conn.getCatalog());
            }
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
