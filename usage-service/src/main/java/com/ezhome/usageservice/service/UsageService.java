package com.ezhome.usageservice.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.ezhome.usageservice.entity.DailyUsage;
import com.ezhome.usageservice.entity.Usage;
import com.ezhome.usageservice.event.TelemetryEvent;
import com.ezhome.usageservice.event.UsageEvent;
import com.ezhome.usageservice.kafka.KafkaUsageProducer;
import com.ezhome.usageservice.repository.DailyUsageRepository;
import com.ezhome.usageservice.repository.UsageRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class UsageService {

    private final UsageRepository usageRepository;
    private final DailyUsageRepository dailyUsageRepository;
    private final KafkaUsageProducer kafkaUsageProducer;

    public UsageService(UsageRepository usageRepository, DailyUsageRepository dailyUsageRepository, KafkaUsageProducer kafkaUsageProducer) {
        this.usageRepository = usageRepository;
        this.dailyUsageRepository = dailyUsageRepository;
        this.kafkaUsageProducer = kafkaUsageProducer;
    }

    public void processTelemetry(TelemetryEvent event) {

        log.info("Recieved Event -> {}", event);

        try{

            // persist raw telemetry
            Usage usage = Usage.builder()
                    .deviceId(event.getDeviceId())
                    .metric(event.getEnergyUsage())
                    .metricTimestamp(event.getCreatedAt())
                    .build();
            
            Usage createdUsage = usageRepository.save(usage);

            // upsert the device usage
            LocalDate date = event.getCreatedAt().toLocalDate();

            dailyUsageRepository.upsertDailyUsage(
                event.getDeviceId(),
                date,
                event.getEnergyUsage(),
                1
            );
            // later CRON jobs can easily fetch entries from daily_usage to aggregate Weekly, Monthly and Yearly tables

            DailyUsage dailyUsage = dailyUsageRepository.findByDeviceIdAndDate(event.getDeviceId(), date);

            //produce kafka usage-event for current usage status of device
            UsageEvent usageEvent = UsageEvent.builder()
                            .deviceId(createdUsage.getDeviceId())
                            .usage(createdUsage.getMetric())
                            .dailyUsage(dailyUsage.getTotalUsage())
                            .usageTimestamp(createdUsage.getMetricTimestamp())
                            .build();

            kafkaUsageProducer.send(usageEvent);

            log.info("Telemetry stored in db and kafka usage-event produced -> {}", usageEvent);

        }
        catch (Exception e) {
            log.error("Error processing recieved TelemetryEvent {}", e);
        }

    }

}
