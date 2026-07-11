package com.ezhome.alertservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezhome.alertservice.dto.AlertDTO;
import com.ezhome.alertservice.dto.CreateAlertRequestDTO;
import com.ezhome.alertservice.dto.DeleteAlertRequestDTO;
import com.ezhome.alertservice.dto.EditAlertRequestDTO;
import com.ezhome.alertservice.dto.FindDeviceAlertsRequestDTO;
import com.ezhome.alertservice.service.AlertService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/alert/v1")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping("/add")
    public AlertDTO addAlert(@RequestHeader("X-User-Id") String userId, @Valid @RequestBody CreateAlertRequestDTO req) {

        UUID id = UUID.fromString(userId);
        return alertService.createAlert(id, req);

    }

    @PutMapping("/edit")
    public AlertDTO editAlert(@RequestHeader("X-User-Id") String userId, @Valid @RequestBody EditAlertRequestDTO req) {

        UUID id = UUID.fromString(userId);
        return alertService.editAlert(id, req);
        
    }

    @DeleteMapping("/delete")
    public void deleteDevice(@RequestHeader("X-User-Id") String userId, @Valid @RequestBody DeleteAlertRequestDTO req) {

        UUID id = UUID.fromString(userId);
        alertService.deleteAlert(id, req);
        
    }

    @GetMapping("/find/all")
    public List<AlertDTO> findDevice(@RequestHeader("X-User-Id") String userId) {

        UUID id = UUID.fromString(userId);
        return alertService.findAllAlertsByUser(id);

    }

    @GetMapping("/find/device")
    public List<AlertDTO> listDevices(@RequestHeader("X-User-Id") String userId, @Valid @RequestBody FindDeviceAlertsRequestDTO req) {

        UUID id = UUID.fromString(userId);
        return alertService.findAllAlertsByDevice(id, req);

    }

    
    
}
