package com.inventory;

import com.inventory.config.DatabaseConfig;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("Testing Database Connection...");
        DatabaseConfig.testConnection();
        DatabaseConfig.initializeDatabase();
    }
}
