package com.ridestats.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ridestats.backend.dto.RideSummary;
import com.ridestats.backend.dto.SummaryResponse;
import com.ridestats.backend.dto.SummaryStats;

/**
 * Default batch upload implementation.
 */
@Service
public class BatchUploadServiceImpl implements BatchUploadService {

    private final GpxParserService gpxParserService;
    private final SummaryGeneratorService summaryGeneratorService;

    public BatchUploadServiceImpl(
            GpxParserService gpxParserService,
            SummaryGeneratorService summaryGeneratorService) {

        this.gpxParserService = gpxParserService;
        this.summaryGeneratorService = summaryGeneratorService;
    }

    @Override
    public SummaryResponse generateSummary(MultipartFile[] files) throws Exception {
        if (ObjectUtils.isEmpty(files)) {
            throw new IllegalArgumentException("At least one GPX file is required");
        }

        RideSummary[] rideSummaries = new RideSummary[files.length];

        for (int index = 0; index < files.length; index++) {
            rideSummaries[index] = gpxParserService.parse(files[index]);
        }

        SummaryStats summaryStats = toSummaryStats(rideSummaries);
        String generatedSummary = summaryGeneratorService.generate(summaryStats);

        return new SummaryResponse(summaryStats, generatedSummary);
    }

    private static SummaryStats toSummaryStats(RideSummary[] rideSummaries) {
        int completedRides = rideSummaries.length;
        double totalDistanceKm = 0;
        double longestRideKm = 0;
        long totalTimeSeconds = 0;
        double totalClimbM = 0;

        for (RideSummary rideSummary : rideSummaries) {
            totalDistanceKm += rideSummary.distanceKm();
            longestRideKm = Math.max(longestRideKm, rideSummary.distanceKm());
            totalTimeSeconds += rideSummary.movingTimeSeconds();
            totalClimbM += rideSummary.elevationGainM();
        }

        double averageSpeedKph = 0;
        if (totalTimeSeconds > 0) {
            averageSpeedKph = totalDistanceKm / (totalTimeSeconds / 3600.0);
        }

        return new SummaryStats(
                completedRides,
                round(totalDistanceKm),
                round(longestRideKm),
                totalTimeSeconds,
                round(totalClimbM),
                round(averageSpeedKph));
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}