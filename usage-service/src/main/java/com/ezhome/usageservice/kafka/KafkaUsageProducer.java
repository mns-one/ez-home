package com.ezhome.usageservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ezhome.usageservice.event.UsageEvent;


@Service
public class KafkaUsageProducer {

    private final KafkaTemplate<String, UsageEvent> kafkaTemplate;

    public KafkaUsageProducer(KafkaTemplate<String, UsageEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(UsageEvent event) {
        kafkaTemplate.send("usage-events", event);
    }
    
}
