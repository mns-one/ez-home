package com.ezhome.deviceregistry.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddDeviceRequestDTO {

    @NotNull
    private final String serialNo;

    @NotNull
    private final String batchNo;

    @NotNull
    private final String modelName;

    @NotNull
    private final String factoryLocation;
    
}
