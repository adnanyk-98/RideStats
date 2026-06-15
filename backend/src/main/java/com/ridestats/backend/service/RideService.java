package com.ridestats.backend.service;

import java.util.List;

import com.ridestats.backend.dto.RideResponse;
import com.ridestats.backend.dto.RideStatsResponse;

/**
 * Provides ride retrieval and aggregate statistics.
 */
public interface RideService {

    /**
     * Returns all stored rides sorted by most recent upload first.
     *
     * @return ordered list of rides
     */
    List<RideResponse> getAllRides();

    /**
     * Returns a single ride by id.
     *
     * @param rideId ride identifier
     * @return ride response
     */
    RideResponse getRideById(Long rideId);

    /**
     * Returns the most recently uploaded ride.
     *
     * @return latest ride response
     */
    RideResponse getLatestRide();

    /**
     * Returns aggregate statistics across all rides.
     *
     * @return ride statistics response
     */
    RideStatsResponse getRideStats();
}