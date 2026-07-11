package com.ezhome.ingestionservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ezhome.ingestionservice.event.TelemetryEvent;


@Service
public class KafkaTelemetryProducer {

    private final KafkaTemplate<String, TelemetryEvent> kafkaTemplate;

    public KafkaTelemetryProducer(KafkaTemplate<String, TelemetryEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(TelemetryEvent event) {
        kafkaTemplate.send("telemetry-events", event);
    }
    
}
