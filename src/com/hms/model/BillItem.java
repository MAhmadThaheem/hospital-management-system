package com.hms.model;

public class BillItem {
    private final String description;
    private final double amount;

    public BillItem(String description, double amount) {
        if (description == null) description = "";
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
        this.description = description;
        this.amount = amount;
    }

    public String getDescription() { return description; }
    public double getAmount() { return amount; }
}
