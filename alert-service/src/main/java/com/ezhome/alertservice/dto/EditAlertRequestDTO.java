package com.ezhome.alertservice.dto;

import com.ezhome.alertservice.entity.AlertWindow;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class EditAlertRequestDTO {

    @NotNull
    private String alertId;

    @NotNull
    private String alertName;

    @NotNull
    private double usageTarget;

    @NotNull
    private AlertWindow alertWindow;

    @NotNull Boolean isActive;
    
}
