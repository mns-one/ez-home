package com.ezhome.deviceservice.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ezhome.deviceservice.dto.CreateDeviceRequestDTO;
import com.ezhome.deviceservice.dto.DeviceDTO;
import com.ezhome.deviceservice.dto.EditDeviceRequestDTO;
import com.ezhome.deviceservice.entity.Device;
import com.ezhome.deviceservice.exception.CustomException;
import com.ezhome.deviceservice.grpc.DeviceRegistryServiceGrpcClient;
import com.ezhome.deviceservice.repository.DeviceRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceRegistryServiceGrpcClient deviceRegistryServiceGrpcClient;

    public DeviceService(DeviceRepository deviceRepository, DeviceRegistryServiceGrpcClient deviceRegistryServiceGrpcClient) {
        this.deviceRepository = deviceRepository;
        this.deviceRegistryServiceGrpcClient = deviceRegistryServiceGrpcClient;
    }

    public DeviceDTO createUserDevice(UUID userId, CreateDeviceRequestDTO req) {

        Optional<Device> device = deviceRepository.findBySerialNo(req.getSerialNo());
        if(device.isPresent()) {
            throw new CustomException("Device already linked to an user");
        }    
        
        Device newDevice = Device.builder()
                            .serialNo(req.getSerialNo())
                            .userId(userId)
                            .modelName(req.getModelName())
                            .build();
        
        try{
            if(!deviceRegistryServiceGrpcClient.validateDevice(newDevice.getSerialNo())) {
                throw new Exception("Device validation failed");
            }
        }
        catch (Exception e) {
            log.error("Error while registering client: {}", e.getMessage());
            throw new CustomException("Failed to add device");
        }

        Device creatDevice = deviceRepository.save(newDevice);

        return mapToDeviceDTO(creatDevice);

    }

    public DeviceDTO findUserDevice(UUID userId, String serialNo) {

        Device device = deviceRepository.findByUserIdAndSerialNo(userId, serialNo)
        .orElseThrow(() -> new CustomException("Device not found"));

        return mapToDeviceDTO(device);

    }

    public List<DeviceDTO> fetchAllUserDevices(UUID userId) {

        List<DeviceDTO> allDevices = deviceRepository.findByUserId(userId)
                                        .stream()
                                        .map(this::mapToDeviceDTO)
                                        .toList();

        return allDevices;
    }

    public void deleteUserDevice(UUID userId, String serialNo) {

        Device device = deviceRepository.findByUserIdAndSerialNo(userId, serialNo)
            .orElseThrow(() -> new CustomException("No device found!"));

        deviceRepository.delete(device);

    }

    public DeviceDTO editUserDevice(UUID userId, EditDeviceRequestDTO req) {

        Device device = deviceRepository.findByUserIdAndSerialNo(userId, req.getSerialNo())
        .orElseThrow(() -> new CustomException("Device not found"));

        device.setCustomName(req.getCustomName());
        Device updatedDevice = deviceRepository.save(device);

        return mapToDeviceDTO(updatedDevice);

    }

    private DeviceDTO mapToDeviceDTO(Device data) {
        return DeviceDTO.builder()
                .serialNo(data.getSerialNo())
                .userId(data.getUserId().toString())
                .modelName(data.getModelName())
                .customName(data.getCustomName())
                .alertsEnabled(data.getAlertsEnabled())
                .createdAt(data.getCreatedAt())
                .build();

    }


    
}
