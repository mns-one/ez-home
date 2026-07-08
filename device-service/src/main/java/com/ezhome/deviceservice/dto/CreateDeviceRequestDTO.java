package com.ezhome.deviceservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateDeviceRequestDTO {

    @NotNull
    private String serialNo;

    @NotNull
    private String modelName;

    
}
