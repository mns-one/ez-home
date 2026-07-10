package com.ezhome.ingestionservice.service;

import org.springframework.stereotype.Service;

import com.ezhome.ingestionservice.dto.TelemetryDTO;
import com.ezhome.ingestionservice.exception.CustomException;
import com.ezhome.ingestionservice.redis.DeviceIdCache;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class IngestionService {

    private final DeviceIdCache deviceIdCache;

    public IngestionService(DeviceIdCache deviceIdCache) {
        this.deviceIdCache = deviceIdCache;
    }

    // validate device id and create kafka event
    public void processTelemetry(TelemetryDTO req) {

        log.info("Telemetry Recived -> " + req);

        if(deviceIdCache.exists(req.getDeviceId())) {
            log.info("Sencing kafka event...");
        }
        else{
            log.info("Invalid id, discarding request...");
            throw new CustomException("Invalid device");
        }

    }
    
}
