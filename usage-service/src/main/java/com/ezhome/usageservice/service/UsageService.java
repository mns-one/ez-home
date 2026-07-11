package com.ezhome.usageservice.service;

import org.springframework.stereotype.Service;

import com.ezhome.usageservice.event.TelemetryEvent;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class UsageService {

    public void processTelemetry(TelemetryEvent event) {

        log.info("Recieved Event -> {}", event);

    }
    
}
