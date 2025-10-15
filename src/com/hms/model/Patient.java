package com.hms.model;

public class Patient {
    private final int id;
    private final String name;
    private final int age;
    private final String insuranceType; // e.g., "gold", "silver", "none"

    public Patient(int id, String name, int age, String insuranceType) {
        if (id <= 0) throw new IllegalArgumentException("Invalid patient id");
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Name required");
        if (age < 0) throw new IllegalArgumentException("Age cannot be negative");
        this.id = id;
        this.name = name;
        this.age = age;
        this.insuranceType = (insuranceType == null) ? "none" : insuranceType;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getInsuranceType() { return insuranceType; }

    @Override
    public String toString() {
        return id + " - " + name + " (" + age + ")";
    }
}
