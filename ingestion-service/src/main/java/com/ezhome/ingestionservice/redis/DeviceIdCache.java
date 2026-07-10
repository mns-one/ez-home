package com.ezhome.ingestionservice.redis;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class DeviceIdCache {

    // maintain a set of all registered device ids for quick validation
    private final RedisTemplate<String, String> redisTemplate;

    public DeviceIdCache(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addDeviceId(String deviceId) {

        redisTemplate
                .opsForSet()
                .add("device_id_set", deviceId);
    }


    public boolean exists(String deviceId) {

        Boolean exists =
                redisTemplate
                    .opsForSet()
                    .isMember(
                        "device_id_set",
                        deviceId
                    );

        return Boolean.TRUE.equals(exists);
    }


    public void removeDeviceId(String deviceId) {

        redisTemplate
                .opsForSet()
                .remove("device_id_set", deviceId);
    }

    public void loadDevices(List<String> ids) {

        if (ids == null || ids.isEmpty()) {
            return;
        }

        redisTemplate
            .opsForSet()
            .add("device_id_set", ids.toArray(new String[0]));
    }
    
}
