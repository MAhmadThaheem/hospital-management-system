package com.hms.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HMSUtil {
    public static double round(double v) {
        return new BigDecimal(v).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
