package com.ridestats.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.ridestats.backend.dto.RideSummary;
import com.ridestats.backend.dto.SummaryPeriod;
import com.ridestats.backend.dto.SummaryResponse;
import com.ridestats.backend.dto.SummaryStatsResponse;

@ExtendWith(MockitoExtension.class)
class BatchUploadServiceImplTest {

    @Mock
    private GpxParserService gpxParserService;

    @Mock
    private SummaryGeneratorService summaryGeneratorService;

    @InjectMocks
    private BatchUploadServiceImpl batchUploadService;

    @Test
    void generateSummaryAggregatesRideStatistics() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("files", "ride-1.gpx", "application/gpx+xml", "a".getBytes());
        MockMultipartFile secondFile = new MockMultipartFile("files", "ride-2.gpx", "application/gpx+xml", "b".getBytes());

        when(gpxParserService.parse(firstFile)).thenReturn(new RideSummary(19.44, 58.5, 3526, 19.85, null, null));
        when(gpxParserService.parse(secondFile)).thenReturn(new RideSummary(40.56, 121.5, 7200, 20.28, null, null));
        when(summaryGeneratorService.generate(any(SummaryStatsResponse.class), eq(SummaryPeriod.FOUR_WEEKS)))
                .thenReturn("generated summary");

        SummaryResponse response = batchUploadService.generateSummary(new MockMultipartFile[]{firstFile, secondFile});

        assertEquals(2, response.stats().completedRides());
        assertEquals(60.0, response.stats().totalDistanceKm());
        assertEquals(40.56, response.stats().longestRideKm());
        assertEquals(10726L, response.stats().totalTimeSeconds());
        assertEquals(180.0, response.stats().totalClimbM());
        assertEquals(20.14, response.stats().averageSpeedKph());
        assertEquals("generated summary", response.generatedSummary());
    }

    @Test
    void generateSummaryRejectsEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> batchUploadService.generateSummary(new MockMultipartFile[0]));
    }
}