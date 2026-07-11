package com.ezhome.ingestionservice.event;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TelemetryEvent {

    private String deviceId;
    private Double energyUsage;
    private LocalDateTime createdAt;

}
