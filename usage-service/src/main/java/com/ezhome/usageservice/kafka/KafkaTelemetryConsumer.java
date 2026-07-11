package com.ezhome.usageservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ezhome.usageservice.event.TelemetryEvent;
import com.ezhome.usageservice.service.UsageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaTelemetryConsumer {

    private final UsageService usageService;

    public KafkaTelemetryConsumer(UsageService usageService) {
        this.usageService = usageService;
    }

    @KafkaListener(topics = "telemetry-events", groupId = "usage-service")
    public void consume(TelemetryEvent event) {
        try{
            usageService.processTelemetry(event);
        }
        catch (Exception e) {
            log.error("Kafka Listener Error {}", e);
        }
    }
    
}
