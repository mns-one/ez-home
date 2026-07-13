package com.ezhome.alertservice.event;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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

