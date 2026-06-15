package com.ridestats.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ridestats.backend.dto.SummaryPeriod;
import com.ridestats.backend.dto.SummaryResponse;
import com.ridestats.backend.dto.SummaryStatsResponse;
import com.ridestats.backend.entity.Ride;
import com.ridestats.backend.repository.RideRepository;

/**
 * Default implementation for period-based ride summaries.
 */
@Service
public class SummaryServiceImpl implements SummaryService {

    private static final String NO_RIDES_MESSAGE = "No rides found for selected period.";

    private final RideRepository rideRepository;
    private final SummaryGeneratorService summaryGeneratorService;

    public SummaryServiceImpl(
            RideRepository rideRepository,
            SummaryGeneratorService summaryGeneratorService) {

        this.rideRepository = rideRepository;
        this.summaryGeneratorService = summaryGeneratorService;
    }

    @Override
    public SummaryResponse getFourWeekSummary() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.minusDays(27).atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();
        return buildSummary(start, end, SummaryPeriod.FOUR_WEEKS);
    }

    @Override
    public SummaryResponse getMonthlySummary() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime end = today.plusMonths(1).withDayOfMonth(1).atStartOfDay();
        return buildSummary(start, end, SummaryPeriod.MONTHLY);
    }

    @Override
    public SummaryResponse getYearlySummary() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.withDayOfYear(1).atStartOfDay();
        LocalDateTime end = today.plusYears(1).withDayOfYear(1).atStartOfDay();
        return buildSummary(start, end, SummaryPeriod.YEARLY);
    }

    private SummaryResponse buildSummary(
            LocalDateTime startInclusive,
            LocalDateTime endExclusive,
            SummaryPeriod period) {

        List<Ride> rides = rideRepository.findByRideDateGreaterThanEqualAndRideDateLessThan(
                startInclusive,
                endExclusive);

        SummaryStatsResponse stats = toSummaryStats(rides);
        if (stats.completedRides() == 0) {
            return new SummaryResponse(emptyStats(), NO_RIDES_MESSAGE);
        }

        return new SummaryResponse(stats, summaryGeneratorService.generate(stats, period));
    }

    private static SummaryStatsResponse toSummaryStats(List<Ride> rides) {
        int completedRides = rides.size();
        double totalDistanceKm = 0;
        double longestRideKm = 0;
        long totalTimeSeconds = 0;
        double totalClimbM = 0;

        for (Ride ride : rides) {
            double distanceKm = toDouble(ride.getDistanceKm());
            totalDistanceKm += distanceKm;
            longestRideKm = Math.max(longestRideKm, distanceKm);
            totalTimeSeconds += ride.getMovingTimeSeconds() != null ? ride.getMovingTimeSeconds() : 0L;
            totalClimbM += toDouble(ride.getElevationGainM());
        }

        double averageSpeedKph = 0;
        if (totalTimeSeconds > 0) {
            averageSpeedKph = totalDistanceKm / (totalTimeSeconds / 3600.0);
        }

        return new SummaryStatsResponse(
                completedRides,
                round(totalDistanceKm),
                round(longestRideKm),
                totalTimeSeconds,
                round(totalClimbM),
                round(averageSpeedKph));
    }

    private static SummaryStatsResponse emptyStats() {
        return new SummaryStatsResponse(0, 0, 0, 0, 0, 0);
    }

    private static double toDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : 0;
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
