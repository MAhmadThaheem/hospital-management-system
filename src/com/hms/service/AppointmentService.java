package com.hms.service;

import com.hms.model.Appointment;
import com.hms.model.Doctor;
import java.util.*;
import java.util.stream.Collectors;

public class AppointmentService {
    private final List<Appointment> booked = new ArrayList<>();

    // Book appointment if no clash for same doctor
    public synchronized boolean book(Appointment appt) {
        for (Appointment a : booked) {
            if (a.getDoctor().equals(appt.getDoctor()) &&
                a.getStartTime().isBefore(appt.getEndTime()) &&
                appt.getStartTime().isBefore(a.getEndTime())) {
                return false; // clash
            }
        }
        booked.add(appt);
        return true;
    }

    public synchronized boolean cancel(int appointmentId) {
        return booked.removeIf(a -> a.getId() == appointmentId);
    }

    public List<Appointment> getAppointmentsForDoctor(int doctorId) {
        return booked.stream()
                .filter(a -> a.getDoctor().getId() == doctorId)
                .collect(Collectors.toList());
    }

    public List<Appointment> getAll() {
        return new ArrayList<>(booked);
    }
}
