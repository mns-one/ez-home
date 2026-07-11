package com.ezhome.usageservice.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(
    name = "daily_usage",
    indexes = {
        @Index(name = "idx_device_date", columnList = "device_id, date")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_daily_usage_device_date", columnNames = {"device_id", "date"})
    }
)
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyUsage {

    @Id
    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "total_usage", nullable = false)
    private double totalUsage;

    @Column(name = "reading_count", nullable = false)
    private int readingCount;
    
}
