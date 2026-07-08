package com.ezhome.deviceservice.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "devices")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "serial_no", unique = true, nullable = false, updatable = false)
    private String serialNo;

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "model_name", nullable = false, updatable = false)
    private String modelName;

    @Column(name = "custom_name")
    private String customName;

    @Column(name = "alerts_enabled", nullable = false)
    private Boolean alertsEnabled;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        alertsEnabled = false;
        createdAt = LocalDateTime.now();
    }
    
}

