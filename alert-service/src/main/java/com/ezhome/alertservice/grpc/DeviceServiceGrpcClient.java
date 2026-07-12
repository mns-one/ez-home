package com.ezhome.alertservice.grpc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import validate.ValidateUserDeviceServiceGrpc;
import validate.ValidateUserDeviceRequest;
import validate.ValidateUserDeviceResponse;


@Service
@Slf4j
public class DeviceServiceGrpcClient {

    private final ValidateUserDeviceServiceGrpc.ValidateUserDeviceServiceBlockingStub blockingStub;

    public DeviceServiceGrpcClient(
        @Value("${device.service.address}") String serverAddress,
        @Value("${device.service.grpc.port}") int serverPort
    ) {

        log.info("Connecting to device-registry-service gRPC {}:{}", serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
                .usePlaintext()
                .build();

        blockingStub = ValidateUserDeviceServiceGrpc.newBlockingStub(channel);

    }

    public boolean validateUserDevice(String deviceId, String userId) {

        ValidateUserDeviceRequest request = ValidateUserDeviceRequest.newBuilder()
                            .setDeviceId(deviceId)
                            .setUserId(userId)
                            .build();
        
        ValidateUserDeviceResponse response = blockingStub.validateUserDevice(request);
        log.info("Received gRPC ValidateUserDeviceResponse {}", response.toString());

        return response.getIsValid();

    }
    
    
}
