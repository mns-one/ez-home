package com.ezhome.deviceregistry.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceDTO {

    private final String serialNo;
    private final String batchNo;
    private final String modelName;
    private final String factoryLocation;
    private final String createdAt;
    
}
