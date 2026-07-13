package com.ezhome.alertservice.redis;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ActiveAlertsCache {

    private final RedisTemplate<String, String> redisTemplate;

    public ActiveAlertsCache(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // maintaining a set of active alerts for quick lookup without hitting main database
    
   public void add(String alertKey) {

        log.info("Adding key to cache -> {}", alertKey);

        if (alertKey == null || alertKey.isEmpty()) {
            return;
        }

        redisTemplate
                .opsForSet()
                .add("active_alerts_set", alertKey);
    }


    public boolean exists(String alertKey) {

        log.info("Finding key in cache -> {}", alertKey);

        Boolean exists =
                redisTemplate
                    .opsForSet()
                    .isMember(
                        "active_alerts_set",
                        alertKey
                    );

        log.info("Result -> {}", exists);

        return Boolean.TRUE.equals(exists);
    }


    public void remove(String alertKey) {

        log.info("Removing key from cache -> {}", alertKey);

        redisTemplate
                .opsForSet()
                .remove("active_alerts_set", alertKey);
    }

    public void saveKeyBatch(List<String> keys) {

        if (keys == null || keys.isEmpty()) {
            return;
        }

        redisTemplate
            .opsForSet()
            .add("active_alerts_set", keys.toArray(new String[0]));
    }

}
