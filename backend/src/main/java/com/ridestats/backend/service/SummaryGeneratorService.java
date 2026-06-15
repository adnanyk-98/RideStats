package com.ridestats.backend.service;

import com.ridestats.backend.dto.SummaryStats;

/**
 * Generates a human-readable summary from aggregated ride statistics.
 */
public interface SummaryGeneratorService {

    /**
     * Converts aggregated stats into a formatted summary string.
     *
     * @param summaryStats aggregated ride statistics
     * @return formatted summary text
     */
    String generate(SummaryStats summaryStats);
}