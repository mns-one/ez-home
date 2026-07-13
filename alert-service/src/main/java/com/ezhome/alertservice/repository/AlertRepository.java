package com.ezhome.alertservice.repository;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezhome.alertservice.entity.Alert;
import java.util.List;


public interface AlertRepository extends JpaRepository<Alert, UUID> {

    Optional<Alert> findByIdAndUserId(UUID id, UUID userId);

    List<Alert> findByUserId(UUID userId);

    List<Alert> findByDeviceId(String deviceId);

    Page<Alert> findByIsActiveTrue(Pageable pageable);
    
}
