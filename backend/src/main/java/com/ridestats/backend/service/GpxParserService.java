package com.ridestats.backend.service;

import com.ridestats.backend.dto.RideSummary;
import org.springframework.web.multipart.MultipartFile;

public interface GpxParserService {

    RideSummary parse(MultipartFile file) throws Exception;

}