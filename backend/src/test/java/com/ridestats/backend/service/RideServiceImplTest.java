package com.ridestats.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ridestats.backend.dto.RideResponse;
import com.ridestats.backend.dto.RideStatsResponse;
import com.ridestats.backend.entity.Ride;
import com.ridestats.backend.exception.RideNotFoundException;
import com.ridestats.backend.repository.RideRepository;

@ExtendWith(MockitoExtension.class)
class RideServiceImplTest {

    @Mock
    private RideRepository rideRepository;

    @InjectMocks
    private RideServiceImpl rideService;

    @Test
    void getAllRidesReturnsRidesInRepositoryOrder() {
        Ride firstRide = ride(1L, "ride-1.gpx", new BigDecimal("19.44"), new BigDecimal("58.50"), 3526L, new BigDecimal("19.85"), LocalDateTime.of(2026, 6, 15, 18, 30));
        Ride secondRide = ride(2L, "ride-2.gpx", new BigDecimal("42.00"), new BigDecimal("120.00"), 7200L, new BigDecimal("21.00"), LocalDateTime.of(2026, 6, 15, 19, 0));

        when(rideRepository.findAllByOrderByUploadedAtDesc()).thenReturn(List.of(secondRide, firstRide));

        List<RideResponse> rides = rideService.getAllRides();

        assertEquals(2, rides.size());
        assertEquals(2L, rides.get(0).id());
        assertEquals(1L, rides.get(1).id());
    }

    @Test
    void getRideByIdReturnsMappedRide() {
        Ride storedRide = ride(1L, "Morning_Ride.gpx", new BigDecimal("19.44"), new BigDecimal("58.50"), 3526L, new BigDecimal("19.85"), LocalDateTime.of(2026, 6, 15, 18, 30));
        when(rideRepository.findById(1L)).thenReturn(Optional.of(storedRide));

        RideResponse response = rideService.getRideById(1L);

        assertEquals(1L, response.id());
        assertEquals("Morning_Ride.gpx", response.fileName());
    }

    @Test
    void getRideByIdThrowsWhenMissing() {
        when(rideRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RideNotFoundException.class, () -> rideService.getRideById(99L));
    }

    @Test
    void getLatestRideReturnsMostRecentRide() {
        Ride storedRide = ride(3L, "latest.gpx", new BigDecimal("55.00"), new BigDecimal("250.00"), 8400L, new BigDecimal("23.57"), LocalDateTime.of(2026, 6, 15, 20, 15));
        when(rideRepository.findTopByOrderByRideDateDesc()).thenReturn(Optional.of(storedRide));

        RideResponse response = rideService.getLatestRide();

        assertEquals(3L, response.id());
        assertEquals("latest.gpx", response.fileName());
    }

    @Test
    void getRideStatsAggregatesRideData() {
        Ride firstRide = ride(1L, "ride-1.gpx", new BigDecimal("19.44"), new BigDecimal("58.50"), 3526L, new BigDecimal("19.85"), LocalDateTime.of(2026, 6, 15, 18, 30));
        Ride secondRide = ride(2L, "ride-2.gpx", new BigDecimal("42.00"), new BigDecimal("120.00"), 7200L, new BigDecimal("21.00"), LocalDateTime.of(2026, 6, 15, 19, 0));
        when(rideRepository.findAll()).thenReturn(List.of(firstRide, secondRide));

        RideStatsResponse response = rideService.getRideStats();

        assertEquals(2L, response.totalRides());
        assertEquals(new BigDecimal("61.44"), response.totalDistanceKm());
        assertEquals(new BigDecimal("178.50"), response.totalElevationGainM());
        assertEquals(10726L, response.totalMovingTimeSeconds());
        assertEquals(new BigDecimal("20.62"), response.averageSpeedKph());
        assertEquals(new BigDecimal("42.00"), response.longestRideKm());
    }

    private static Ride ride(
            Long id,
            String fileName,
            BigDecimal distanceKm,
            BigDecimal elevationGainM,
            Long movingTimeSeconds,
            BigDecimal averageSpeedKph,
            LocalDateTime uploadedAt) {

        return Ride.builder()
                .id(id)
                .fileName(fileName)
                .distanceKm(distanceKm)
                .elevationGainM(elevationGainM)
                .movingTimeSeconds(movingTimeSeconds)
                .averageSpeedKph(averageSpeedKph)
                .uploadedAt(uploadedAt)
                .build();
    }
}
