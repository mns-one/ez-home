package com.ezhome.deviceservice.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class DeviceDTO {

    private String serialNo;
    private String userId;
    private String modelName;
    private String customName;
    private Boolean alertsEnabled;
    private LocalDateTime createdAt;

}
