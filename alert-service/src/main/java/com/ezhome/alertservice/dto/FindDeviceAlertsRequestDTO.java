package com.ezhome.alertservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@NotNull
public class FindDeviceAlertsRequestDTO {

    @NotNull
    private String deviceId;
    
}
