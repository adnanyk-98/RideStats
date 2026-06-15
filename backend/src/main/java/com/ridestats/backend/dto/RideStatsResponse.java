package com.ridestats.backend.dto;

import java.math.BigDecimal;

/**
 * Aggregate statistics across all stored rides.
 *
 * @param totalRides total number of rides
 * @param totalDistanceKm total distance in kilometers
 * @param totalElevationGainM total climb in meters
 * @param totalMovingTimeSeconds total moving time in seconds
 * @param averageSpeedKph average speed in kph
 * @param longestRideKm longest ride distance in kilometers
 */
public record RideStatsResponse(
        long totalRides,
        BigDecimal totalDistanceKm,
        BigDecimal totalElevationGainM,
        long totalMovingTimeSeconds,
        BigDecimal averageSpeedKph,
        BigDecimal longestRideKm
) {
}