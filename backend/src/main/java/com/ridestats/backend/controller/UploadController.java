package com.ridestats.backend.controller;

import com.ridestats.backend.dto.RideSummary;
import com.ridestats.backend.dto.SummaryResponse;
import com.ridestats.backend.service.BatchUploadService;
import com.ridestats.backend.service.GpxParserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/upload")
@Validated
@Tag(name = "GPX Upload", description = "Upload GPX files")
public class UploadController {

    private final GpxParserService gpxParserService;
    private final BatchUploadService batchUploadService;

    public UploadController(
            GpxParserService gpxParserService,
            BatchUploadService batchUploadService) {

        this.gpxParserService = gpxParserService;
        this.batchUploadService = batchUploadService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload GPX file")
    public RideSummary upload(
            @RequestParam("file") MultipartFile file) throws Exception {

        return gpxParserService.parse(file);
    }

    /**
     * Uploads multiple GPX files and returns aggregated ride statistics plus a
     * generated summary.
     *
     * @param files GPX files to analyze
     * @return aggregated batch summary response
     * @throws Exception if parsing fails for any uploaded file
     */
    @PostMapping(path = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple GPX files and generate a summary")
    public SummaryResponse uploadBatch(
            @Parameter(description = "GPX files to upload", required = true, array = @ArraySchema(schema = @Schema(type = "string", format = "binary"))) @RequestPart("files") @NotEmpty MultipartFile[] files)
            throws Exception {

        return batchUploadService.generateSummary(files);
    }
}