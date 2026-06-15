package com.ridestats.backend.dto;

/**
 * Standard API error payload.
 *
 * @param success whether the request succeeded
 * @param message human readable error message
 * @param errorCode application specific error code
 * @param timestamp ISO-8601 timestamp of the error
 */
public record ErrorResponse(
        boolean success,
        String message,
        String errorCode,
        String timestamp
) {
}