package com.ridestats.backend.dto;

public record SummaryStats(
        int completedRides,
        double totalDistanceKm,
        double longestRideKm,
        long totalTimeSeconds,
        double totalClimbM,
        double averageSpeedKph
) {
}