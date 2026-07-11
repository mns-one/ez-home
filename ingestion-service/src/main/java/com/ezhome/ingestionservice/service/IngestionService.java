package com.ezhome.ingestionservice.service;

import org.springframework.stereotype.Service;

import com.ezhome.ingestionservice.dto.TelemetryDTO;
import com.ezhome.ingestionservice.event.TelemetryEvent;
import com.ezhome.ingestionservice.exception.CustomException;
import com.ezhome.ingestionservice.kafka.KafkaTelemetryProducer;
import com.ezhome.ingestionservice.redis.DeviceIdCache;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class IngestionService {

    private final DeviceIdCache deviceIdCache;
    private final KafkaTelemetryProducer kafkaTelemetryProducer;

    public IngestionService(DeviceIdCache deviceIdCache, KafkaTelemetryProducer kafkaTelemetryProducer) {
        this.deviceIdCache = deviceIdCache;
        this.kafkaTelemetryProducer = kafkaTelemetryProducer;
    }

    // validate device id and create kafka event
    public void processTelemetry(TelemetryDTO req) {

        log.info("Telemetry Recived -> " + req.toString());

        if(!deviceIdCache.exists(req.getDeviceId())) {
            log.info("Invalid id, discarding request...");
            throw new CustomException("Invalid device");
        }

        TelemetryEvent telemetryEvent = TelemetryEvent.builder()
                            .deviceId(req.getDeviceId())
                            .energyUsage(req.getEnergyUsage())
                            .createdAt(req.getCreatedAt())
                            .build();
        
        try {
            kafkaTelemetryProducer.send(telemetryEvent);
        }
        catch (Exception e) {
            log.error("Kafka Producer Error {} ", e);
        }

        log.info("Kafka event send -> " + telemetryEvent.toString());

    }
    
}
