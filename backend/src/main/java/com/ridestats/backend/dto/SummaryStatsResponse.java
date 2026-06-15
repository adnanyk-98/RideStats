package com.ridestats.backend.dto;

/**
 * Aggregated ride statistics for a selected time period.
 *
 * @param completedRides number of rides in the period
 * @param totalDistanceKm sum of ride distances in kilometres
 * @param longestRideKm longest single ride distance in kilometres
 * @param totalTimeSeconds sum of moving time in seconds
 * @param totalClimbM sum of elevation gain in metres
 * @param averageSpeedKph distance-weighted average speed in km/h
 */
public record SummaryStatsResponse(
        int completedRides,
        double totalDistanceKm,
        double longestRideKm,
        long totalTimeSeconds,
        double totalClimbM,
        double averageSpeedKph
) {
}
