package com.ezhome.alertservice.startup;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ezhome.alertservice.entity.Alert;
import com.ezhome.alertservice.entity.AlertWindow;
import com.ezhome.alertservice.redis.ActiveAlertsCache;
import com.ezhome.alertservice.repository.AlertRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RedisStartupLoader implements ApplicationRunner {

    private final ActiveAlertsCache activeAlertsCache;
    private final AlertRepository alertRepository;

    public RedisStartupLoader(ActiveAlertsCache activeAlertsCache, AlertRepository alertRepository) {
        this.activeAlertsCache = activeAlertsCache;
        this.alertRepository = alertRepository;
    }
    
    @Override
    public void run(ApplicationArguments args) {

        log.info("Loading active alerts from database to memory....");

        // on service startup, load all active alerts from database to cache in batches
        // used for quick lookup of incoming kafka usage-events and trigger alert notification process

        int page = 0;
        int size = 100;

        while (true) {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<Alert> alertPage = alertRepository.findByIsActiveTrue(pageRequest);

            if (alertPage.isEmpty()) {
                break;
            }

            List<String> alertKeys = new ArrayList<>();

            for (Alert alert : alertPage.getContent()) {
                String cacheKey = createCacheKey(
                    alert.getDeviceId(),
                    alert.getUsageTarget(),
                    alert.getAlertWindow()
                );
                alertKeys.add(cacheKey);
            }

            if (!alertKeys.isEmpty()) {
                activeAlertsCache.saveKeyBatch(alertKeys);
            }

            if (!alertPage.hasNext()) {
                break;
            }

            page++;
        }

        log.info("Completed loading all active alerts into memory!");

    }

    private String createCacheKey(String deviceId, double usageTarget, AlertWindow alertWindow) {

        return String.format("%s:%s:%s", deviceId, usageTarget, alertWindow.name());

    }
    
}
