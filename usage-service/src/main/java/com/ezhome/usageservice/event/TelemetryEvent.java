package com.ezhome.usageservice.event;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryEvent {

    private String deviceId;
    private Double energyUsage;
    private LocalDateTime createdAt;
    
}
