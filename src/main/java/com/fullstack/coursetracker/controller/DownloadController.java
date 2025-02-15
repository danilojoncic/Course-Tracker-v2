package com.fullstack.coursetracker.controller;

import com.fullstack.coursetracker.service.DownloadService;
import org.atmosphere.config.service.Get;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/download")
public class DownloadController {
    private final DownloadService downloadService;

    public DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @GetMapping("/csv")
    public ResponseEntity<?> downloadCsv() {
        byte[] csvFile = downloadService.downlaodCsv();
        HttpHeaders headers = new HttpHeaders();
        //or text/csv
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // Set content type to binary
        headers.setContentDispositionFormData("attachment", "transactions.csv"); // Set filename for download
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvFile);
    }
}
