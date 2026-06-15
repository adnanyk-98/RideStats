package com.ridestats.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * API representation of a stored ride.
 *
 * @param id ride identifier
 * @param fileName uploaded GPX file name
 * @param rideDate date of the ride
 * @param uploadedAt upload timestamp
 * @param distanceKm ride distance in kilometers
 * @param elevationGainM climb in meters
 * @param movingTimeSeconds moving time in seconds
 * @param averageSpeedKph average speed in kph
 */
public record RideResponse(
        Long id,
        String fileName,
        LocalDateTime rideDate,
        LocalDateTime uploadedAt,
        double distanceKm,
        double elevationGainM,
        long movingTimeSeconds,
        double averageSpeedKph
) {}