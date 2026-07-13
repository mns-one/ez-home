package com.ezhome.alertservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ezhome.alertservice.event.UsageEvent;
import com.ezhome.alertservice.notification.ProcessUsage;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class KafkaUsageConsumer {

    private final ProcessUsage processUsage;

    public KafkaUsageConsumer(ProcessUsage processUsage) {
        this.processUsage = processUsage;
    }

    @KafkaListener(topics = "usage-events", groupId = "alert-service")
    public void consume(UsageEvent event) {
        try{
            processUsage.validate(event);
        }
        catch (Exception e) {
            log.error("Kafka Listener Error {}", e);
        }
    }
    
}
