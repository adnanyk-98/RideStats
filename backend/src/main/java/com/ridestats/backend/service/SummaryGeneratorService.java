package com.ridestats.backend.service;

import com.ridestats.backend.dto.SummaryPeriod;
import com.ridestats.backend.dto.SummaryStatsResponse;

/**
 * Generates a human-readable summary from aggregated ride statistics.
 */
public interface SummaryGeneratorService {

    /**
     * Converts aggregated stats into a formatted summary string.
     *
     * @param summaryStats aggregated ride statistics
     * @param period summary time period
     * @return formatted summary text
     */
    String generate(SummaryStatsResponse summaryStats, SummaryPeriod period);
}