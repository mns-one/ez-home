package com.ezhome.ingestionservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezhome.ingestionservice.dto.TelemetryDTO;
import com.ezhome.ingestionservice.service.IngestionService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/ingestion/v1")
public class IngestionController {

    private final IngestionService ingestionService;

    public IngestionController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/telemetry")
    public void getTelemetry(@Valid @RequestBody TelemetryDTO req) {
        ingestionService.processTelemetry(req);
    }
    
}
