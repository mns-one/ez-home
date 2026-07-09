package com.ezhome.ingestionservice.startup;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.ezhome.ingestionservice.grpc.DeviceServiceGrpcClient;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class RedisStartupLoader implements ApplicationRunner {

    private final DeviceServiceGrpcClient deviceServiceGrpcClient;

    public RedisStartupLoader(DeviceServiceGrpcClient deviceServiceGrpcClient) {
        this.deviceServiceGrpcClient = deviceServiceGrpcClient;
    }

    @Override
    public void run(ApplicationArguments args) {

        log.info("Establishing connection load memory....");

        try{
            deviceServiceGrpcClient.streamAllDeviceIds(1);
        }
        catch (Exception e) {
            log.error("Error loading memory -> " + e.getMessage());
            throw new RuntimeException();
        }

        log.info("Memory loading completed!");

    }

}
