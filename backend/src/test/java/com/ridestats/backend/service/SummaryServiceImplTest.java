package com.ridestats.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ridestats.backend.dto.SummaryPeriod;
import com.ridestats.backend.dto.SummaryResponse;
import com.ridestats.backend.dto.SummaryStatsResponse;
import com.ridestats.backend.entity.Ride;
import com.ridestats.backend.repository.RideRepository;

@ExtendWith(MockitoExtension.class)
class SummaryServiceImplTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private SummaryGeneratorService summaryGeneratorService;

    @InjectMocks
    private SummaryServiceImpl summaryService;

    @Test
    void getFourWeekSummaryReturnsEmptyResponseWhenNoRidesFound() {
        LocalDate today = LocalDate.now();
        when(rideRepository.findByRideDateGreaterThanEqualAndRideDateLessThan(
                today.minusDays(27).atStartOfDay(),
                today.plusDays(1).atStartOfDay())).thenReturn(List.of());

        SummaryResponse response = summaryService.getFourWeekSummary();

        assertEquals(0, response.stats().completedRides());
        assertEquals(0, response.stats().totalDistanceKm());
        assertEquals(0, response.stats().longestRideKm());
        assertEquals(0, response.stats().totalTimeSeconds());
        assertEquals(0, response.stats().totalClimbM());
        assertEquals(0, response.stats().averageSpeedKph());
        assertEquals("No rides found for selected period.", response.generatedSummary());
    }

    @Test
    void getMonthlySummaryAggregatesRidesByRideDate() {
        LocalDate today = LocalDate.now();
        Ride firstRide = ride(50.0, 30.0, 3600L, 500.0);
        Ride secondRide = ride(30.0, 20.0, 1800L, 300.0);

        when(rideRepository.findByRideDateGreaterThanEqualAndRideDateLessThan(
                today.withDayOfMonth(1).atStartOfDay(),
                today.plusMonths(1).withDayOfMonth(1).atStartOfDay()))
                .thenReturn(List.of(firstRide, secondRide));
        when(summaryGeneratorService.generate(any(SummaryStatsResponse.class), eq(SummaryPeriod.MONTHLY)))
                .thenReturn("monthly summary");

        SummaryResponse response = summaryService.getMonthlySummary();

        assertEquals(2, response.stats().completedRides());
        assertEquals(80.0, response.stats().totalDistanceKm());
        assertEquals(50.0, response.stats().longestRideKm());
        assertEquals(5400L, response.stats().totalTimeSeconds());
        assertEquals(50.0, response.stats().totalClimbM());
        assertEquals(53.33, response.stats().averageSpeedKph());
        assertEquals("monthly summary", response.generatedSummary());
    }

    @Test
    void getYearlySummaryQueriesCurrentCalendarYear() {
        LocalDate today = LocalDate.now();
        when(rideRepository.findByRideDateGreaterThanEqualAndRideDateLessThan(
                today.withDayOfYear(1).atStartOfDay(),
                today.plusYears(1).withDayOfYear(1).atStartOfDay()))
                .thenReturn(List.of(ride(10.0, 5.0, 3600L, 100.0)));
        when(summaryGeneratorService.generate(any(SummaryStatsResponse.class), eq(SummaryPeriod.YEARLY)))
                .thenReturn("yearly summary");

        SummaryResponse response = summaryService.getYearlySummary();

        verify(rideRepository).findByRideDateGreaterThanEqualAndRideDateLessThan(
                today.withDayOfYear(1).atStartOfDay(),
                today.plusYears(1).withDayOfYear(1).atStartOfDay());
        assertEquals(1, response.stats().completedRides());
        assertEquals(10.0, response.stats().averageSpeedKph());
        assertEquals("yearly summary", response.generatedSummary());
    }

    private static Ride ride(double distanceKm, double elevationGainM, long movingTimeSeconds, double averageSpeedKph) {
        return Ride.builder()
                .id(1L)
                .fileName("ride.gpx")
                .distanceKm(BigDecimal.valueOf(distanceKm))
                .elevationGainM(BigDecimal.valueOf(elevationGainM))
                .movingTimeSeconds(movingTimeSeconds)
                .averageSpeedKph(BigDecimal.valueOf(averageSpeedKph))
                .rideDate(LocalDateTime.now())
                .uploadedAt(LocalDateTime.now())
                .build();
    }
}
