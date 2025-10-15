package com.hms.service;

import com.hms.model.Appointment;
import com.hms.model.Medicine;
import com.hms.util.HMSUtil;

import java.time.LocalDate;
import java.util.List;

public class BillingService {
    // Calculate subtotal (medicines + consultations)
    public double calculateSubTotal(List<Medicine> meds, List<Appointment> appts) {
        double medsTotal = 0.0;
        LocalDate today = LocalDate.now();
        if (meds != null) {
            for (Medicine m : meds) {
                if (m.isExpired(today)) throw new IllegalArgumentException("Expired medicine in bill: " + m.getName());
                medsTotal += m.getUnitPrice() * m.getQuantity();
            }
        }
        double consultTotal = 0.0;
        if (appts != null) {
            for (Appointment a : appts) {
                consultTotal += a.getDoctor().getFeePerHour() * a.getDurationHours();
            }
        }
        return HMSUtil.round(medsTotal + consultTotal);
    }

    // apply insurance coverage (percentage 0..100)
    public double applyInsurance(double subtotal, double coveragePercent) {
        if (coveragePercent < 0 || coveragePercent > 100) throw new IllegalArgumentException("Invalid coverage");
        double after = subtotal - subtotal * (coveragePercent / 100.0);
        return HMSUtil.round(after);
    }

    // apply tax with allowed rate cap (0..25)
    public double applyTax(double amount, double taxPercent) {
        if (taxPercent < 0 || taxPercent > 25) throw new IllegalArgumentException("Invalid tax");
        double after = amount + amount * (taxPercent / 100.0);
        return HMSUtil.round(after);
    }

    // final amount = subtotal -> insurance -> tax
    public double finalAmount(List<Medicine> meds, List<Appointment> appts, double coveragePercent, double taxPercent) {
        double subtotal = calculateSubTotal(meds, appts);
        double afterInsurance = applyInsurance(subtotal, coveragePercent);
        return applyTax(afterInsurance, taxPercent);
    }
}
