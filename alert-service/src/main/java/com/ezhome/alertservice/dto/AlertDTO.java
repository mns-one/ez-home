package com.ezhome.alertservice.dto;

import java.time.LocalDateTime;

import com.ezhome.alertservice.entity.AlertWindow;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class AlertDTO {

    private String alertId;
    private String deviceId;
    private String userId;
    private String alertName;
    private Boolean isActive;
    private double usageTarget;
    private AlertWindow alertWindow;
    private LocalDateTime createdAt;
    
}
