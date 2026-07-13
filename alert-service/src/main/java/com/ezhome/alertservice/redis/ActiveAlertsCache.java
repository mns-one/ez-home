package com.ezhome.alertservice.redis;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class ActiveAlertsCache {

    private final RedisTemplate<String, String> redisTemplate;

    public ActiveAlertsCache(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // maintaining a set of active alerts for quick lookup without hitting main database
    
   public void add(String alertKey) {

        if (alertKey == null || alertKey.isEmpty()) {
            return;
        }

        redisTemplate
                .opsForSet()
                .add("active_alerts_set", alertKey);
    }


    public boolean exists(String alertKey) {

        Boolean exists =
                redisTemplate
                    .opsForSet()
                    .isMember(
                        "active_alerts_set",
                        alertKey
                    );

        return Boolean.TRUE.equals(exists);
    }


    public void remove(String alertKey) {

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
