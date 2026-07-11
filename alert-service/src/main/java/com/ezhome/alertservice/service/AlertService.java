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
import com.ezhome.alertservice.exception.CustomException;
import com.ezhome.alertservice.repository.AlertRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public AlertDTO createAlert(UUID userId, CreateAlertRequestDTO req) {

        Alert alert = Alert.builder()
                    .deviceId(req.getDeviceId())
                    .userId(userId)
                    .alertName(req.getAlertName())
                    .usageTarget(req.getUsageTarget())
                    .alertWindow(req.getAlertWindow())
                    .build();
        
        Alert createdAlert = alertRepository.save(alert);

        return mapToAlertDTO(createdAlert);

    }

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

        Alert editedAlert = alertRepository.save(alert);

        return mapToAlertDTO(editedAlert);
        
    }

    public void deleteAlert(UUID userId, DeleteAlertRequestDTO req) {

        UUID alertId = UUID.fromString(req.getAlertId());

        Alert alert = alertRepository.findById(alertId)
            .orElseThrow(() -> new CustomException("No Alert found"));

        if(!alert.getUserId().equals(userId)){
            throw new CustomException("No Alert found");
        } 

        alertRepository.delete(alert);

    }

    public List<AlertDTO> findAllAlertsByUser(UUID userId) {

        List<AlertDTO> allAlerts = alertRepository.findByUserId(userId)
                                        .stream()
                                        .map(this::mapToAlertDTO)
                                        .toList();
        
        return allAlerts;

    }

    public List<AlertDTO> findAllAlertsByDevice(UUID userId, FindDeviceAlertsRequestDTO req) {

        List<AlertDTO> allAlerts = alertRepository.findByUserId(userId)
                                        .stream()
                                        .filter(alert -> Objects.equals(alert.getDeviceId(), req.getDeviceId()))
                                        .map(this::mapToAlertDTO)
                                        .toList();
        
        return allAlerts;

    }


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
