package com.ezhome.deviceregistry.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ezhome.deviceregistry.dto.AddDeviceRequestDTO;
import com.ezhome.deviceregistry.dto.DeviceDTO;
import com.ezhome.deviceregistry.entity.Device;
import com.ezhome.deviceregistry.exception.CustomException;
import com.ezhome.deviceregistry.repository.DeviceRepository;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    // create device entry in database
    public DeviceDTO addToRegistry(AddDeviceRequestDTO req) {

        Optional<Device> device = deviceRepository.findBySerialNo(req.getSerialNo());

        if(device.isPresent()) {
            throw new CustomException("Serial no already registered!");
        }

        Device newDevice = Device.builder()
                    .serialNo(req.getSerialNo())
                    .batchNo(req.getBatchNo())
                    .modelName(req.getModelName())
                    .factoryLocation(req.getFactoryLocation())
                    .build();
        
        Device createdDevice = deviceRepository.save(newDevice);

        return mapToDeviceDTO(createdDevice);

    }

    // fetch device record from database
    public DeviceDTO findinRegistry(String serialNo) {

        Device device = deviceRepository.findBySerialNo(serialNo)
            .orElseThrow(() -> new CustomException("No device record found!"));

        return mapToDeviceDTO(device);

    }

    // check if a device exists in database
    public boolean validateDevice(String serialNo) {

        Optional<Device> device = deviceRepository.findBySerialNo(serialNo);

        if(device.isPresent()) return true;

        return false;

    }

    // delete device record from database
    public void deleteFromRegistry(String serialNo) {

        Device device = deviceRepository.findBySerialNo(serialNo)
            .orElseThrow(() -> new CustomException("No device record found!"));

        deviceRepository.delete(device);

    }

    // helper function for mapping
    public DeviceDTO mapToDeviceDTO(Device device) {
        return DeviceDTO.builder()
                .serialNo(device.getSerialNo())
                .batchNo(device.getBatchNo())
                .modelName(device.getModelName())
                .factoryLocation(device.getFactoryLocation())
                .createdAt(device.getCreatedAt().toString())
                .build();
    }
    
}
