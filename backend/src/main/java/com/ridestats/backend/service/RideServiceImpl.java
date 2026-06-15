package com.ridestats.backend.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ridestats.backend.dto.RideResponse;
import com.ridestats.backend.dto.RideStatsResponse;
import com.ridestats.backend.entity.Ride;
import com.ridestats.backend.exception.RideNotFoundException;
import com.ridestats.backend.repository.RideRepository;

/**
 * Default ride read implementation.
 */
@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;

    public RideServiceImpl(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    @Override
    public List<RideResponse> getAllRides() {
        return rideRepository.findAllByOrderByUploadedAtDesc()
                .stream()
                .map(this::toRideResponse)
                .toList();
    }

    @Override
    public RideResponse getRideById(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException(rideId));

        return toRideResponse(ride);
    }

    @Override
    public RideResponse getLatestRide() {
        Ride ride = rideRepository.findTopByOrderByRideDateDesc()
                .orElseThrow(() -> new RideNotFoundException("No rides available"));
        return toRideResponse(ride);
    }

    @Override
    public RideStatsResponse getRideStats() {
        List<Ride> rides = rideRepository.findAll();

        if (rides.isEmpty()) {
            return new RideStatsResponse(
                    0,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    0,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO);
        }

        long totalRides = rides.size();
        BigDecimal totalDistanceKm = BigDecimal.ZERO;
        BigDecimal totalElevationGainM = BigDecimal.ZERO;
        long totalMovingTimeSeconds = 0;
        BigDecimal longestRideKm = BigDecimal.ZERO;

        for (Ride ride : rides) {
            if (ride.getDistanceKm() != null) {
                totalDistanceKm = totalDistanceKm.add(ride.getDistanceKm());
                if (ride.getDistanceKm().compareTo(longestRideKm) > 0) {
                    longestRideKm = ride.getDistanceKm();
                }
            }

            if (ride.getElevationGainM() != null) {
                totalElevationGainM = totalElevationGainM.add(ride.getElevationGainM());
            }

            if (ride.getMovingTimeSeconds() != null) {
                totalMovingTimeSeconds += ride.getMovingTimeSeconds();
            }
        }

        BigDecimal averageSpeedKph = BigDecimal.ZERO;
        if (totalMovingTimeSeconds > 0) {
            averageSpeedKph = totalDistanceKm
                    .divide(BigDecimal.valueOf(totalMovingTimeSeconds), 6, java.math.RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(3600));
            averageSpeedKph = averageSpeedKph.setScale(2, java.math.RoundingMode.HALF_UP);
        }

        return new RideStatsResponse(
                totalRides,
                totalDistanceKm.setScale(2, java.math.RoundingMode.HALF_UP),
                totalElevationGainM.setScale(2, java.math.RoundingMode.HALF_UP),
                totalMovingTimeSeconds,
                averageSpeedKph,
                longestRideKm.setScale(2, java.math.RoundingMode.HALF_UP));
    }

    private RideResponse toRideResponse(Ride ride) {
        return new RideResponse(
                ride.getId(),
                ride.getFileName(),
                ride.getRideDate(),
                ride.getUploadedAt(),
                ride.getDistanceKm() != null ? ride.getDistanceKm().doubleValue() : 0.0,
                ride.getElevationGainM() != null ? ride.getElevationGainM().doubleValue() : 0.0,
                ride.getMovingTimeSeconds() != null ? ride.getMovingTimeSeconds() : 0L,
                ride.getAverageSpeedKph() != null ? ride.getAverageSpeedKph().doubleValue() : 0.0);
    }
}