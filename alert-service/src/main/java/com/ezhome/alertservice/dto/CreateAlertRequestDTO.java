package com.ezhome.alertservice.dto;

import com.ezhome.alertservice.entity.AlertWindow;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreateAlertRequestDTO {

    @NotNull
    private String deviceId;

    @NotNull
    private String alertName;

    @NotNull
    private double usageTarget;

    @NotNull
    private AlertWindow alertWindow;
    
}
