package com.ezhome.usageservice.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezhome.usageservice.entity.DailyUsage;

import jakarta.transaction.Transactional;


public interface DailyUsageRepository extends JpaRepository<DailyUsage, String> {

    // Upsert query to quickly calculate device usage for each day by only updating one row
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO daily_usage (device_id, date, total_usage, reading_count)
        VALUES (:deviceId, :date, :totalUsage, :readingCount)
        ON CONFLICT (device_id, date)
        DO UPDATE SET
            total_usage = daily_usage.total_usage + EXCLUDED.total_usage,
            reading_count = daily_usage.reading_count + EXCLUDED.reading_count
        """, nativeQuery = true)
    void upsertDailyUsage(
        @Param("deviceId") String deviceId,
        @Param("date") LocalDate date,
        @Param("totalUsage") double totalUsage,
        @Param("readingCount") int readingCount
    );

    DailyUsage findByDeviceIdAndDate(String id, LocalDate date);
    
}
