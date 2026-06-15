package com.ridestats.backend.service;

import com.ridestats.backend.dto.SummaryResponse;

import org.springframework.web.multipart.MultipartFile;

/**
 * Coordinates batch ride parsing and summary generation.
 */
public interface BatchUploadService {

    /**
     * Parses multiple GPX files, aggregates their statistics, and generates summary text.
     *
     * @param files GPX files to analyze
     * @return batch summary response
     * @throws Exception if any file cannot be parsed
     */
    SummaryResponse generateSummary(MultipartFile[] files) throws Exception;
}