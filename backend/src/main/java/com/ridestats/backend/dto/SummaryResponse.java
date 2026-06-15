package com.ridestats.backend.dto;

/**
 * Response payload for batch ride summary generation.
 *
 * @param stats aggregated ride statistics
 * @param generatedSummary human-readable generated summary text
 */
public record SummaryResponse(
        SummaryStats stats,
        String generatedSummary
) {
}