package com.ridestats.backend.dto;

public record RideSummary(
        double distanceKm,
        double elevationGainM,
        long movingTimeSeconds,
        double averageSpeedKph,
        String startTime,
        String endTime
) {
}