package com.ezhome.deviceservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezhome.deviceservice.dto.CreateDeviceRequestDTO;
import com.ezhome.deviceservice.dto.DeviceDTO;
import com.ezhome.deviceservice.dto.EditDeviceRequestDTO;
import com.ezhome.deviceservice.service.DeviceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/device/v1")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping("/add")
    public DeviceDTO addDevice(@RequestHeader("X-User-Id") String userId, @Valid @RequestBody CreateDeviceRequestDTO req) {

        UUID id = UUID.fromString(userId);
        return deviceService.createUserDevice(id, req);

    }

    @GetMapping("/find/{deviceId}")
    public DeviceDTO findDevice(@RequestHeader("X-User-Id") String userId, @Valid @PathVariable String deviceId) {

        UUID id = UUID.fromString(userId);
        return deviceService.findUserDevice(id, deviceId);

    }

    @GetMapping("/list")
    public List<DeviceDTO> listDevices(@RequestHeader("X-User-Id") String userId) {

        UUID id = UUID.fromString(userId);
        return deviceService.fetchAllUserDevices(id);

    }

    @PutMapping("/edit")
    public DeviceDTO deleteDevice(@RequestHeader("X-User-Id") String userId, @Valid @RequestBody EditDeviceRequestDTO req) {

        UUID id = UUID.fromString(userId);
        return deviceService.editUserDevice(id, req);
        
    }

    @DeleteMapping("/delete/{deviceId}")
    public void deleteDevice(@RequestHeader("X-User-Id") String userId, @Valid @PathVariable String deviceId) {

        UUID id = UUID.fromString(userId);
        deviceService.deleteUserDevice(id, deviceId);
        
    }

}
