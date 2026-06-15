package com.ridestats.backend.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DurationFormatterTest {

    @Test
    void formatReturnsHoursAndMinutes() {
        assertEquals("28h 07m", DurationFormatter.format(101220));
        assertEquals("0h 00m", DurationFormatter.format(0));
        assertEquals("1h 05m", DurationFormatter.format(3900));
    }
}
