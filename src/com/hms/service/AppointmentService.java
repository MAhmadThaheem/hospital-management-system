package com.hms.service;

import com.hms.model.Appointment;
import com.hms.model.Doctor;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AppointmentService {
    private final List<Appointment> booked = new ArrayList<>();

    // Book appointment if no clash for same doctor
    public synchronized boolean book(Appointment appt) {
        for (Appointment a : booked) {
            if (a.getDoctor().equals(appt.getDoctor())) {
                LocalDateTime s1 = a.getStartTime();
                LocalDateTime e1 = a.getEndTime();
                LocalDateTime s2 = appt.getStartTime();
                LocalDateTime e2 = appt.getEndTime();

                // Check time overlap (clash)
                if (!(e2.isBefore(s1) || s2.isAfter(e1))) {
                    return false; // Time clash for same doctor
                }
            }
        }
        booked.add(appt); // No clash → book it
        return true;
    }

    // Cancel an appointment
    public synchronized boolean cancel(int id) {
        return booked.removeIf(a -> a.getId() == id);
    }

    // Get all booked appointments
    public List<Appointment> getAll() {
        return new ArrayList<>(booked);
    }

    // Get appointments by doctor
    public List<Appointment> getByDoctor(Doctor d) {
        return booked.stream()
                .filter(a -> a.getDoctor().equals(d))
                .collect(Collectors.toList());
    }

    // Find appointment by ID
    public Appointment findById(int id) {
        return booked.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
