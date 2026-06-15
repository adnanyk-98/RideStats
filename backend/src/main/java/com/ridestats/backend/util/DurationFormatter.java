package com.ridestats.backend.util;

import java.util.Locale;

/**
 * Formats durations for human-readable summary output.
 */
public final class DurationFormatter {

    private DurationFormatter() {
    }

    /**
     * Formats a duration as {@code Xh YYm}.
     *
     * @param totalSeconds duration in seconds
     * @return formatted duration string
     */
    public static String format(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        return String.format(Locale.US, "%dh %02dm", hours, minutes);
    }
}
