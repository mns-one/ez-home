package com.ezhome.deviceservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezhome.deviceservice.entity.Device;
import java.util.Optional;
import java.util.List;



public interface DeviceRepository extends JpaRepository<Device, UUID> {

    List<Device> findByUserId(UUID userId);
    Optional<Device> findByUserIdAndSerialNo(UUID userId, String serialNo);
    Optional<Device> findBySerialNo(String serialNo);
    
}
