package com.ridestats.backend.exception;

/**
 * Thrown when a ride id cannot be resolved.
 */
public class RideNotFoundException extends RuntimeException {

    public RideNotFoundException(Long rideId) {
        super("Ride not found with id: " + rideId);
    }

    public RideNotFoundException(String message) {
        super(message);
    }
}