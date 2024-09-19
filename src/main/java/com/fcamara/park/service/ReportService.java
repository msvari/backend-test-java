package com.fcamara.park.service;

import org.springframework.core.io.InputStreamResource;
import java.util.List;
import java.util.Map;

public interface ReportService {
    InputStreamResource generateReport(String title, List<String> columnHeaders, List<Map<String, String>> rows, Map<String, Long> summary);
}

