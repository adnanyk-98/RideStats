package com.ridestats.backend.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ridestats.backend.dto.RideResponse;
import com.ridestats.backend.dto.RideStatsResponse;
import com.ridestats.backend.service.RideService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * Read-only ride API.
 */
@RestController
@RequestMapping(value = "/api/rides", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Rides", description = "Ride retrieval and aggregate statistics")
@SecurityRequirement(name = "basicAuth")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    /**
     * Returns all rides ordered by upload time descending.
     *
     * @return ordered ride list
     */
    @GetMapping
    @Operation(summary = "Get all rides")
    @ApiResponse(responseCode = "200", description = "List of rides")
    public List<RideResponse> getAllRides() {
        return rideService.getAllRides();
    }

    /**
     * Returns a ride by id.
     *
     * @param id ride identifier
     * @return ride details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get ride by id")
    @ApiResponse(responseCode = "200", description = "Ride found")
    @ApiResponse(responseCode = "404", description = "Ride not found")
    public RideResponse getRideById(@PathVariable Long id) {
        return rideService.getRideById(id);
    }

    /**
     * Returns the latest uploaded ride.
     *
     * @return most recent ride
     */
    @GetMapping("/latest")
    @Operation(summary = "Get latest ride")
    @ApiResponse(responseCode = "200", description = "Latest ride returned")
    @ApiResponse(responseCode = "404", description = "No rides available")
    public RideResponse getLatestRide() {
        return rideService.getLatestRide();
    }

    /**
     * Returns aggregate statistics across all rides.
     *
     * @return ride statistics
     */
    @GetMapping("/stats")
    @Operation(summary = "Get ride statistics")
    @ApiResponse(responseCode = "200", description = "Aggregate statistics")
    public RideStatsResponse getRideStats() {
        return rideService.getRideStats();
    }
}