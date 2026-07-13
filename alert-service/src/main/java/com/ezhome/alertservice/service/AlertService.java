package com.ezhome.alertservice.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ezhome.alertservice.dto.AlertDTO;
import com.ezhome.alertservice.dto.CreateAlertRequestDTO;
import com.ezhome.alertservice.dto.DeleteAlertRequestDTO;
import com.ezhome.alertservice.dto.EditAlertRequestDTO;
import com.ezhome.alertservice.dto.FindDeviceAlertsRequestDTO;
import com.ezhome.alertservice.entity.Alert;
import com.ezhome.alertservice.entity.AlertWindow;
import com.ezhome.alertservice.exception.CustomException;
import com.ezhome.alertservice.grpc.DeviceServiceGrpcClient;
import com.ezhome.alertservice.redis.ActiveAlertsCache;
import com.ezhome.alertservice.repository.AlertRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class AlertService {

    private final AlertRepository alertRepository;
    private final DeviceServiceGrpcClient deviceServiceGrpcClient;
    private final ActiveAlertsCache activeAlertsCache;

    public AlertService(AlertRepository alertRepository,
        DeviceServiceGrpcClient deviceServiceGrpcClient,
        ActiveAlertsCache activeAlertsCache
    ) {
        this.alertRepository = alertRepository;
        this.deviceServiceGrpcClient = deviceServiceGrpcClient;
        this.activeAlertsCache= activeAlertsCache;
    }

    // create new alert in database
    public AlertDTO createAlert(UUID userId, CreateAlertRequestDTO req) {

        Alert alert = Alert.builder()
                    .deviceId(req.getDeviceId())
                    .userId(userId)
                    .alertName(req.getAlertName())
                    .usageTarget(req.getUsageTarget())
                    .alertWindow(req.getAlertWindow())
                    .build();
        
        // verfiy user-device ownership with device-service before inserting in db
        try{
            if(!deviceServiceGrpcClient.validateUserDevice(alert.getDeviceId(), alert.getUserId().toString())) {
                throw new Exception("Device validation failed");
            }
        }
        catch (Exception e) {
            log.error("Error creating alert: {}", e.getMessage());
            throw new CustomException("Failed to create alert");
        }
        
        // new alert is default active so also add to acitve alerts cache
        Alert createdAlert = alertRepository.save(alert);
        String alertKey = createCacheKey(createdAlert.getDeviceId(), createdAlert.getUsageTarget(), createdAlert.getAlertWindow());

        activeAlertsCache.add(alertKey);

        return mapToAlertDTO(createdAlert);

    }

    // edit existing alert entry in database
    public AlertDTO editAlert(UUID userId, EditAlertRequestDTO req) {

        UUID alertId = UUID.fromString(req.getAlertId());

        Alert alert = alertRepository.findById(alertId)
            .orElseThrow(() -> new CustomException("No Alert found"));

        if(!alert.getUserId().equals(userId)){
            throw new CustomException("No Alert found");
        }    

        alert.setAlertName(req.getAlertName());
        alert.setUsageTarget(req.getUsageTarget());
        alert.setAlertWindow(req.getAlertWindow());
        alert.setIsActive(req.getIsActive());

        Alert editedAlert = alertRepository.save(alert);

        // based on isActive status, add or remove alert from acitve alerts cache
        String alertKey = createCacheKey(editedAlert.getDeviceId(), editedAlert.getUsageTarget(), editedAlert.getAlertWindow());

        if(editedAlert.getIsActive()) {
            activeAlertsCache.add(alertKey);
        }
        else {
            activeAlertsCache.remove(alertKey);
        }

        return mapToAlertDTO(editedAlert);
        
    }

    // delete alert entry from database
    public void deleteAlert(UUID userId, DeleteAlertRequestDTO req) {

        UUID alertId = UUID.fromString(req.getAlertId());

        Alert alert = alertRepository.findById(alertId)
            .orElseThrow(() -> new CustomException("No Alert found"));

        if(!alert.getUserId().equals(userId)){
            throw new CustomException("No Alert found");
        } 

        // remove it from both database and cache
        String alertKey = createCacheKey(alert.getDeviceId(), alert.getUsageTarget(), alert.getAlertWindow());
        activeAlertsCache.remove(alertKey);

        alertRepository.delete(alert);

    }

    // find all alerts created by user
    public List<AlertDTO> findAllAlertsByUser(UUID userId) {

        List<AlertDTO> allAlerts = alertRepository.findByUserId(userId)
                                        .stream()
                                        .map(this::mapToAlertDTO)
                                        .toList();
        
        return allAlerts;

    }

    // find all alerts linked to a device
    public List<AlertDTO> findAllAlertsByDevice(UUID userId, FindDeviceAlertsRequestDTO req) {

        List<AlertDTO> allAlerts = alertRepository.findByUserId(userId)
                                        .stream()
                                        .filter(alert -> Objects.equals(alert.getDeviceId(), req.getDeviceId()))
                                        .map(this::mapToAlertDTO)
                                        .toList();
        
        return allAlerts;

    }

    // delete all existing alert records for a device
    public void deleteAllDeviceAlerts(String deviceId) {

        List<Alert> alerts = alertRepository.findByDeviceId(deviceId);

        if (alerts.isEmpty()) {
            return;
        }

        // remove all alerts from cache and database
        for (Alert alert : alerts) {
            String alertKey = createCacheKey(
                alert.getDeviceId(),
                alert.getUsageTarget(),
                alert.getAlertWindow()
            );
            activeAlertsCache.remove(alertKey);
        }

        alertRepository.deleteAll(alerts);

    }

    // helper to create a cache key for each alert database record
    private String createCacheKey(String deviceId, double usageTarget, AlertWindow alertWindow) {

        return String.format("%s:%s:%s", deviceId, usageTarget, alertWindow.name());

    }

    // map alert entity to public dto
    private AlertDTO mapToAlertDTO(Alert data) {
        return AlertDTO.builder()
                    .alertId(data.getId().toString())
                    .deviceId(data.getDeviceId())
                    .userId(data.getUserId().toString())
                    .alertName(data.getAlertName())
                    .isActive(data.getIsActive())
                    .usageTarget(data.getUsageTarget())
                    .alertWindow(data.getAlertWindow())
                    .createdAt(data.getCreatedAt())
                    .build();
    }

    
    
}
