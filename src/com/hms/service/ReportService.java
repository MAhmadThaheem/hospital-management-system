package com.hms.service;

import com.hms.model.Appointment;
import java.util.*;
import java.util.stream.Collectors;

public class ReportService {
    // group appointments by doctor id and return earnings per doctor
    public Map<Integer, Double> earningsByDoctor(List<Appointment> appointments) {
        if (appointments == null) return Collections.emptyMap();
        return appointments.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getDoctor().getId(),
                        Collectors.summingDouble(a -> a.getDoctor().getFeePerHour() * a.getDurationHours())
                ));
    }
}
