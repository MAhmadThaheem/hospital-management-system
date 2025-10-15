package com.hms.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Appointment {
    private static final AtomicInteger ID_GEN = new AtomicInteger(1);

    private final int id;
    private final Patient patient;
    private final Doctor doctor;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime; // endTime must be after startTime
    private final double durationHours;

    public Appointment(Patient patient, Doctor doctor, LocalDateTime startTime, LocalDateTime endTime) {
        if (patient == null || doctor == null || startTime == null || endTime == null)
            throw new IllegalArgumentException("Null argument");
        if (!endTime.isAfter(startTime)) throw new IllegalArgumentException("End time must be after start time");
        this.id = ID_GEN.getAndIncrement();
        this.patient = patient;
        this.doctor = doctor;
        this.startTime = startTime;
        this.endTime = endTime;
        long seconds = java.time.Duration.between(startTime, endTime).getSeconds();
        this.durationHours = Math.max(0.0, seconds / 3600.0);
    }

    public int getId() { return id; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public double getDurationHours() { return durationHours; }

    @Override
    public String toString() {
        return "Appt#" + id + " " + patient.getName() + " with Dr." + doctor.getName();
    }
}
