package com.blockchain.accesscontrol.access_control_system.utils;

import java.util.Arrays;
import java.util.List;

public class GiniCalculator {
    
    public static double calculateForDoubles(List<Double> values) {
        double[] sorted = values.stream()
            .mapToDouble(Double::doubleValue)
            .sorted()
            .toArray();
        
        return calculateGini(sorted);
    }

    public static double calculateForLongs(List<Long> values) {
        double[] sorted = values.stream()
            .mapToDouble(Long::doubleValue)
            .sorted()
            .toArray();
        
        return calculateGini(sorted);
    }

    private static double calculateGini(double[] sortedValues) {
        double sum = Arrays.stream(sortedValues).sum();
        if(sum == 0) return 0;

        double giniSum = 0;
        int n = sortedValues.length;
        
        for(int i=0; i<n; i++) {
            giniSum += (2*(i+1) - n - 1) * sortedValues[i];
        }
        
        return giniSum / (n * sum);
    }
}