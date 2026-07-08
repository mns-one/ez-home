package com.ezhome.deviceregistry.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezhome.deviceregistry.entity.Device;
import java.util.Optional;


public interface DeviceRepository extends JpaRepository<Device, UUID> {

    Optional<Device> findBySerialNo(String serialNo);
    
}
