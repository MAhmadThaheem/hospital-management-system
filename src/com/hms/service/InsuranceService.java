package com.hms.service;

public class InsuranceService {

    // coverage percent by plan
    public double getCoverageRate(String planType) {
        if (planType == null) return 0.0;
        switch (planType.trim().toLowerCase()) {
            case "gold": return 90.0;
            case "silver": return 70.0;
            case "bronze": return 50.0;
            case "none": return 0.0;
            default: return 0.0;
        }
    }

    public boolean validatePolicyNumber(String policyNumber) {
        if (policyNumber == null) return false;
        // simple validation: alphanumeric and length 6-12
        return policyNumber.matches("[A-Za-z0-9]{6,12}");
    }
}
