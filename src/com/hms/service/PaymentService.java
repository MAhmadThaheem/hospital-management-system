package com.hms.service;

import com.hms.util.HMSUtil;

public class PaymentService {
    private double collected = 0.0;

    public synchronized void acceptPayment(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        collected = HMSUtil.round(collected + amount);
    }

    public double getCollectedAmount() {
        return collected;
    }

    public synchronized double refund(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (amount > collected) throw new IllegalArgumentException("Refund exceeds collected amount");
        collected = HMSUtil.round(collected - amount);
        return amount;
    }
}
