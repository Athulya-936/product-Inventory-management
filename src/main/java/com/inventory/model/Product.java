package com.inventory.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {
    private int id;
    private String name;
    private BigDecimal price;
    private int quantity;
    private int lowStockThreshold;
    private int totalSales;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product() {
        this.lowStockThreshold = 10;
        this.totalSales = 0;
    }

    public Product(String name, BigDecimal price, int quantity) {
        this();
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(int id, String name, BigDecimal price, int quantity, 
                   int lowStockThreshold, int totalSales) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.lowStockThreshold = lowStockThreshold;
        this.totalSales = totalSales;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(int lowStockThreshold) { 
        this.lowStockThreshold = lowStockThreshold; 
    }

    public int getTotalSales() { return totalSales; }
    public void setTotalSales(int totalSales) { this.totalSales = totalSales; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { 
        this.updatedAt = updatedAt; 
    }

    // Business methods
    public boolean isLowStock() {
        return quantity <= lowStockThreshold;
    }

    public BigDecimal getTotalValue() {
        return price.multiply(new BigDecimal(quantity));
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", lowStockThreshold=" + lowStockThreshold +
                ", totalSales=" + totalSales +
                '}';
    }
}
