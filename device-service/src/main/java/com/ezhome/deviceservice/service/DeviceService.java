package com.ezhome.deviceservice.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ezhome.deviceservice.dto.CreateDeviceRequestDTO;
import com.ezhome.deviceservice.dto.DeviceDTO;
import com.ezhome.deviceservice.dto.EditDeviceRequestDTO;
import com.ezhome.deviceservice.entity.Device;
import com.ezhome.deviceservice.exception.CustomException;
import com.ezhome.deviceservice.grpc.DeviceRegistryServiceGrpcClient;
import com.ezhome.deviceservice.grpc.IngestionServiceGrpcClient;
import com.ezhome.deviceservice.repository.DeviceRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceRegistryServiceGrpcClient deviceRegistryServiceGrpcClient;
    private final IngestionServiceGrpcClient ingestionServiceGrpcClient;

    public DeviceService(DeviceRepository deviceRepository, DeviceRegistryServiceGrpcClient deviceRegistryServiceGrpcClient, IngestionServiceGrpcClient ingestionServiceGrpcClient) {
        this.deviceRepository = deviceRepository;
        this.deviceRegistryServiceGrpcClient = deviceRegistryServiceGrpcClient;
        this.ingestionServiceGrpcClient = ingestionServiceGrpcClient;
    }

    // create a new entry for user device
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

        // validate device with device-registry before inserting
        // and send deviceId to ingestion-service
        try{
            if(!deviceRegistryServiceGrpcClient.validateDevice(newDevice.getSerialNo())) {
                throw new Exception("Device validation failed");
            }
            ingestionServiceGrpcClient.addDeviceToIngestionService(newDevice.getSerialNo());
        }
        catch (Exception e) {
            log.error("Error while registering device: {}", e.getMessage());
            throw new CustomException("Failed to add device");
        }

        Device creatDevice = deviceRepository.save(newDevice);

        return mapToDeviceDTO(creatDevice);

    }

    // fetch record for a device registered by user
    public DeviceDTO findUserDevice(UUID userId, String serialNo) {

        Device device = deviceRepository.findByUserIdAndSerialNo(userId, serialNo)
        .orElseThrow(() -> new CustomException("Device not found"));

        return mapToDeviceDTO(device);

    }

    // fetch record for all devices registered by user
    public List<DeviceDTO> fetchAllUserDevices(UUID userId) {

        List<DeviceDTO> allDevices = deviceRepository.findByUserId(userId)
                                        .stream()
                                        .map(this::mapToDeviceDTO)
                                        .toList();

        return allDevices;
    }

    // delete record for a user device
    public void deleteUserDevice(UUID userId, String serialNo) {

        Device device = deviceRepository.findByUserIdAndSerialNo(userId, serialNo)
            .orElseThrow(() -> new CustomException("No device found!"));

        try{
            ingestionServiceGrpcClient.deleteDeviceFromIngestionService(device.getSerialNo());
            deviceRepository.delete(device);
        }
        catch (Exception e) {
            log.error("Error while deleting device: {}", e.getMessage());
            throw new CustomException("Failed to delete device");
        }

    }

    // edit user device record details in db
    public DeviceDTO editUserDevice(UUID userId, EditDeviceRequestDTO req) {

        Device device = deviceRepository.findByUserIdAndSerialNo(userId, req.getSerialNo())
        .orElseThrow(() -> new CustomException("Device not found"));

        device.setCustomName(req.getCustomName());
        Device updatedDevice = deviceRepository.save(device);

        return mapToDeviceDTO(updatedDevice);

    }

    // fetch serialNo of all devices in db using pagination
    public Page<String> fetchDeviceIdPage(int pageNumber, int pageSize) {

        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        return deviceRepository.findAllSerialNos(pageable);
        
    }

    public Boolean validateUserDevice(String deviceId, String userId) {

        try{
            UUID id = UUID.fromString(userId);
            Optional<Device> device = deviceRepository.findByUserIdAndSerialNo(id, deviceId);
            if(!device.isPresent()){
                throw new Exception("No record found in database");
            }
        }
        catch (Exception e) {
            log.error("User and Device cannot be verifided", e);
            return false;
        }

        return true;
    }

    // helper function for mapping
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
