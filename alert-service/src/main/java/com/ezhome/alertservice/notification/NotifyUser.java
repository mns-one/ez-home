package com.ezhome.alertservice.notification;

import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ezhome.alertservice.entity.Alert;
import com.ezhome.alertservice.entity.AlertWindow;
import com.ezhome.alertservice.event.UsageEvent;
import com.ezhome.alertservice.redis.ActiveAlertsCache;
import com.ezhome.alertservice.repository.AlertRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class NotifyUser {

    private final PendingEvents pendingEvents;
    private final AlertRepository alertRepository;
    private final ActiveAlertsCache activeAlertsCache;

    public NotifyUser(PendingEvents pendingEvents,
        AlertRepository alertRepository,
        ActiveAlertsCache activeAlertsCache
    ) {
        this.pendingEvents = pendingEvents;
        this.alertRepository = alertRepository;
        this.activeAlertsCache = activeAlertsCache;
    }

    @Scheduled(fixedDelay = 2000)
    public void sendNotification() {

        // get pendingEvents from queue
        // fetch alert record from database and send notification
        // update redis if it succeeds
        // else requeue it back as pending

        UsageEvent event = pendingEvents.poll();

        if (event == null) {
            return;
        }

        try {

            Optional<Alert> matchedAlert = alertRepository.findByDeviceIdAndIsActiveTrue(event.getDeviceId())
                .stream()
                .filter(alert -> matchesAlert(alert, event))
                .findFirst();

            if (matchedAlert.isEmpty()) {
                log.info("No matching active alert found for event: {}", event);
                return;
            }

            Alert alert = matchedAlert.get();

            // WIP placeholder
            // send the info to notification-service that handles the implementation for email, sms, push etc
            log.info(
                "Sending notification to user {} for alert {} on device {} with event {}",
                alert.getUserId(),
                alert.getId(),
                alert.getDeviceId(),
                event
            );

            // remove LIFETIME alerts from redis only not DAILY ones
            if(alert.getAlertWindow().equals("LIFETIME")) {
                String alertKey = createCacheKey(alert.getDeviceId(), alert.getUsageTarget(), alert.getAlertWindow());
                activeAlertsCache.remove(alertKey);
            }

        } catch (Exception e) {
            log.error("Notification processing failed for event {}", event, e);
            // insert back as pending
            pendingEvents.requeue(event);
        }
    }

    private boolean matchesAlert(Alert alert, UsageEvent event) {

        if (alert.getAlertWindow() == AlertWindow.DAILY && alert.getUsageTarget() == event.getDailyUsage()) {
            return true;
        }

        if (alert.getAlertWindow() == AlertWindow.LIFETIME  && alert.getUsageTarget() == event.getUsage()) {
            return true;
        }

        return false;
    }

    private String createCacheKey(String deviceId, double usageTarget, AlertWindow alertWindow) {

        return String.format("%s:%s:%s", deviceId, usageTarget, alertWindow.name());

    }
    
}
