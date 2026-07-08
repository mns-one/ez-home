package com.ezhome.deviceservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditDeviceRequestDTO {

    @NotNull
    private final String serialNo;

    @NotNull
    private final String customName;
    
}
