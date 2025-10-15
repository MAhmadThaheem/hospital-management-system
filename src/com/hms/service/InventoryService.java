package com.hms.service;

import com.hms.model.Medicine;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryService {
    private final Map<String, Medicine> stock = new ConcurrentHashMap<>();

    public boolean addMedicine(Medicine m) {
        Objects.requireNonNull(m);
        String key = m.getName().toLowerCase();
        if (stock.containsKey(key)) return false;
        stock.put(key, m);
        return true;
    }

    public boolean deductStock(String name, int qty) {
        if (qty < 0) throw new IllegalArgumentException("Quantity negative");
        Medicine m = stock.get(name.toLowerCase());
        if (m == null) return false;
        synchronized (m) {
            if (m.getQuantity() < qty) return false;
            m.setQuantity(m.getQuantity() - qty);
            return true;
        }
    }

    public boolean isExpired(String name, LocalDate onDate) {
        Medicine m = stock.get(name.toLowerCase());
        if (m == null) throw new IllegalArgumentException("Medicine not found");
        return m.isExpired(onDate);
    }

    public double totalInventoryValue() {
        return stock.values().stream().mapToDouble(Medicine::getSubtotal).sum();
    }

    public Medicine find(String name) {
        return stock.get(name.toLowerCase());
    }
    
    public List<Medicine> getAll() {
        return new ArrayList<>(stock.values());
    }

}
