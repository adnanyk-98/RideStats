package com.ridestats.backend.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ridestats.backend.dto.SummaryResponse;
import com.ridestats.backend.service.SummaryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Period-based ride summary API.
 */
@RestController
@RequestMapping(value = "/api/summary", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Summary", description = "Ride summaries by time period")
@SecurityRequirement(name = "basicAuth")
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    /**
     * Returns aggregated statistics for rides in the last 28 days by ride date.
     *
     * @return four-week summary
     */
    @GetMapping("/four-weeks")
    @Operation(summary = "Get four-week ride summary")
    @ApiResponse(responseCode = "200", description = "Four-week summary returned")
    public SummaryResponse getFourWeekSummary() {
        return summaryService.getFourWeekSummary();
    }

    /**
     * Returns aggregated statistics for rides in the current calendar month by ride date.
     *
     * @return monthly summary
     */
    @GetMapping("/monthly")
    @Operation(summary = "Get monthly ride summary")
    @ApiResponse(responseCode = "200", description = "Monthly summary returned")
    public SummaryResponse getMonthlySummary() {
        return summaryService.getMonthlySummary();
    }

    /**
     * Returns aggregated statistics for rides in the current calendar year by ride date.
     *
     * @return yearly summary
     */
    @GetMapping("/yearly")
    @Operation(summary = "Get yearly ride summary")
    @ApiResponse(responseCode = "200", description = "Yearly summary returned")
    public SummaryResponse getYearlySummary() {
        return summaryService.getYearlySummary();
    }
}
