package com.ridestats.backend.service;

import com.ridestats.backend.dto.SummaryResponse;

/**
 * Provides aggregated ride summaries for defined time periods.
 */
public interface SummaryService {

    /**
     * Returns summary statistics for rides in the last 28 days by ride date.
     *
     * @return four-week summary response
     */
    SummaryResponse getFourWeekSummary();

    /**
     * Returns summary statistics for rides in the current calendar month by ride date.
     *
     * @return monthly summary response
     */
    SummaryResponse getMonthlySummary();

    /**
     * Returns summary statistics for rides in the current calendar year by ride date.
     *
     * @return yearly summary response
     */
    SummaryResponse getYearlySummary();
}
