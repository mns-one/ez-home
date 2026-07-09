package com.ezhome.ingestionservice.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class TelemetryDTO {
    
    @NotNull
    private final String deviceId;

    @NotNull
    private final Double energyUsage;

    private final LocalDateTime createdAt = LocalDateTime.now();
    
}
