package com.ezhome.usageservice.event;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageEvent {

    private String deviceId;
    private double usage;
    private double dailyUsage;
    private LocalDateTime usageTimestamp;

}
