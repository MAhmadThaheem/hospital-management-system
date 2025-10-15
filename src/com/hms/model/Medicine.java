package com.hms.model;

import java.time.LocalDate;

public class Medicine {
    private final String name;
    private final double unitPrice;
    private int quantity;
    private final LocalDate expiryDate; // can be null

    public Medicine(String name, double unitPrice, int quantity, LocalDate expiryDate) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Name required");
        if (unitPrice < 0) throw new IllegalArgumentException("Price cannot be negative");
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    public String getName() { return name; }
    public double getUnitPrice() { return unitPrice; }
    public synchronized int getQuantity() { return quantity; }
    public synchronized void setQuantity(int q) {
        if (q < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        this.quantity = q;
    }
    public LocalDate getExpiryDate() { return expiryDate; }

    public synchronized double getSubtotal() {
        return unitPrice * quantity;
    }

    public boolean isExpired(LocalDate onDate) {
        if (expiryDate == null) return false;
        return !expiryDate.isAfter(onDate);
    }
    
    public double getPrice() {
        return unitPrice;
    }
}
