package com.ezhome.alertservice.notification;

import org.springframework.stereotype.Service;

import com.ezhome.alertservice.entity.AlertWindow;
import com.ezhome.alertservice.event.UsageEvent;
import com.ezhome.alertservice.redis.ActiveAlertsCache;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ProcessUsage {

    private final ActiveAlertsCache activeAlertsCache;
    private final PendingEvents pendingEvents;

    public ProcessUsage(ActiveAlertsCache activeAlertsCache, PendingEvents pendingEvents) {
        this.activeAlertsCache = activeAlertsCache;
        this.pendingEvents = pendingEvents;
    }

    public void validate(UsageEvent event) {

        log.info("Recieved usage-event {}", event);

        String dailyAlertKey = createCacheKey(
            event.getDeviceId(),
            event.getDailyUsage(),
            AlertWindow.DAILY
        );

        String lifetimeAlertKey = createCacheKey(
            event.getDeviceId(),
            event.getUsage(),
            AlertWindow.LIFETIME
        );

        if(activeAlertsCache.exists(lifetimeAlertKey)) {
            log.info("Queued Alert to send for event {}", event);
            pendingEvents.add(event);
        }

        if(activeAlertsCache.exists(dailyAlertKey)) {
            log.info("Queued Alert to send for event {}", event);
            pendingEvents.add(event);
        }

    }

    private String createCacheKey(String deviceId, double usageTarget, AlertWindow alertWindow) {

        return String.format("%s:%s:%s", deviceId, usageTarget, alertWindow.name());

    }
    
}
