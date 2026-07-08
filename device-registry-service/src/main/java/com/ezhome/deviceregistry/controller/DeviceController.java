package com.ezhome.deviceregistry.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezhome.deviceregistry.dto.AddDeviceRequestDTO;
import com.ezhome.deviceregistry.dto.DeviceDTO;
import com.ezhome.deviceregistry.exception.AccessException;
import com.ezhome.deviceregistry.service.DeviceService;

import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/registry/v1")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping("/add")
    public DeviceDTO addDevice(@RequestHeader("X-User-Id") String userId, @RequestHeader("X-User-Role") String role, @Valid @RequestBody AddDeviceRequestDTO req) {

        verifyAccess(userId, role);
        return deviceService.addToRegistry(req);
        
    }

    @GetMapping("/find/{id}")
    public DeviceDTO findDevice(@RequestHeader("X-User-Id") String userId, @RequestHeader("X-User-Role") String role, @Valid @PathVariable String id) {

        verifyAccess(userId, role);
        return deviceService.findinRegistry(id);
        
    }

    @DeleteMapping("/delete/{id}")
    public void deleteDevice(@RequestHeader("X-User-Id") String userId, @RequestHeader("X-User-Role") String role, @Valid @PathVariable String id) {

        verifyAccess(userId, role);
        deviceService.deleteFromRegistry(id);
        
    }

    private void verifyAccess(String userId, String role) {
        UUID.fromString(userId);

        if(!role.equalsIgnoreCase("ADMIN")){
            throw new AccessException("Account does not have permission");
        }
    }

}
