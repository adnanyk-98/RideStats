package com.ridestats.backend.dto;

/**
 * Time period used for summary generation.
 */
public enum SummaryPeriod {

    FOUR_WEEKS("FOUR WEEK SUMMARY"),
    MONTHLY("MONTHLY SUMMARY"),
    YEARLY("YEARLY SUMMARY");

    private final String title;

    SummaryPeriod(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }
}
