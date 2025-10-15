package com.hms.model;

public class Doctor {
    private final int id;
    private final String name;
    private final String specialization;
    private final double feePerHour;

    public Doctor(int id, String name, String specialization, double feePerHour) {
        if (id <= 0) throw new IllegalArgumentException("Invalid doctor id");
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Name required");
        if (specialization == null) specialization = "general";
        if (feePerHour < 0) throw new IllegalArgumentException("Fee cannot be negative");
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.feePerHour = feePerHour;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public double getFeePerHour() { return feePerHour; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor)) return false;
        Doctor d = (Doctor) o;
        return this.id == d.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
    
    public double getFee() {
        return feePerHour;
    }
}
