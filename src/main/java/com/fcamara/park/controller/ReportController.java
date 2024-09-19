package com.fcamara.park.controller;

import com.fcamara.park.service.impl.ReportParkServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@Slf4j
public class ReportController {

    private final ReportParkServiceImpl reportParkService;

    @GetMapping("/park/{spotId}")
    public ResponseEntity<Object> exportParkingPdfReport(@PathVariable(value = "spotId") UUID spotId,
                                                         @RequestParam(value = "initialDate", required = false) String inicialDate,
                                                         @RequestParam(value = "finalDate", required = false) String finalDate) {

        log.info("state=init-extract-report , spotId={}, initialDate={}, , finalDate={}", spotId, inicialDate, finalDate);
        try {
            InputStreamResource resource = reportParkService.report(spotId, inicialDate, finalDate);
            log.info("state=end-success-extract-report , spotId={}, initialDate={}, , finalDate={}", spotId, inicialDate, finalDate);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"park_report.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (IllegalArgumentException e) {
            log.info("state=error-not-found-extract-report , spotId={}, initialDate={}, , finalDate={}", spotId, inicialDate, finalDate);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.info("state=error-extract-report , spotId={}, initialDate={}, , finalDate={}", spotId, inicialDate, finalDate);
            return ResponseEntity.internalServerError().body("Um erro interno ocorreu");
        }
    }

}
