package com.ridestats.backend.service;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ridestats.backend.dto.RideSummary;
import com.ridestats.backend.entity.Ride;
import com.ridestats.backend.repository.RideRepository;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;

@Service
public class GpxParserServiceImpl implements GpxParserService {

    private final RideRepository rideRepository;

    public GpxParserServiceImpl(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    @Override
    public RideSummary parse(MultipartFile file) throws Exception {

        Path tempFile = Files.createTempFile("ride-", ".gpx");
        file.transferTo(tempFile);

        GPX gpx = GPX.read(tempFile);

        double distanceMeters = 0;
        double elevationGain = 0;

        Instant startTime = null;
        Instant endTime = null;

        WayPoint previous = null;

        for (Track track : gpx.getTracks()) {

            for (TrackSegment segment : track.getSegments()) {

                List<WayPoint> points = segment.getPoints();

                for (WayPoint point : points) {

                    if (startTime == null && point.getTime().isPresent()) {
                        startTime = point.getTime().get();
                    }

                    if (point.getTime().isPresent()) {
                        endTime = point.getTime().get();
                    }

                    if (previous != null) {

                        distanceMeters += haversine(
                                previous.getLatitude().doubleValue(),
                                previous.getLongitude().doubleValue(),
                                point.getLatitude().doubleValue(),
                                point.getLongitude().doubleValue());

                        if (previous.getElevation().isPresent()
                                && point.getElevation().isPresent()) {

                            double diff = point.getElevation().get().doubleValue()
                                    - previous.getElevation().get().doubleValue();

                            if (diff > 0) {
                                elevationGain += diff;
                            }
                        }
                    }

                    previous = point;
                }
            }
        }

        long movingTimeSeconds = 0;

        if (startTime != null && endTime != null) {
            movingTimeSeconds = Duration.between(startTime, endTime).getSeconds();
        }

        double distanceKm = distanceMeters / 1000.0;

        double averageSpeedKph = 0;

        if (movingTimeSeconds > 0) {
            averageSpeedKph = distanceKm / (movingTimeSeconds / 3600.0);
        }

        RideSummary rideSummary = new RideSummary(
                round(distanceKm),
                round(elevationGain),
                movingTimeSeconds,
                round(averageSpeedKph),
                startTime != null ? startTime.toString() : null,
                endTime != null ? endTime.toString() : null);

        Ride ride = Ride.builder()
                .fileName(file.getOriginalFilename())
            .distanceKm(BigDecimal.valueOf(rideSummary.distanceKm()))
            .elevationGainM(BigDecimal.valueOf(rideSummary.elevationGainM()))
                .movingTimeSeconds(rideSummary.movingTimeSeconds())
            .averageSpeedKph(BigDecimal.valueOf(rideSummary.averageSpeedKph()))
                .uploadedAt(LocalDateTime.now())
                .build();
        rideRepository.save(ride);
        return rideSummary;
    }

    private static double haversine(
            double lat1,
            double lon1,
            double lat2,
            double lon2) {

        final double R = 6371000;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2)
                        * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}