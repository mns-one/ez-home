package com.ezhome.deviceservice.grpc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import registry.DeviceRegistryServiceGrpc;
import registry.ValidateDeviceRequest;
import registry.ValidateDeviceResponse;


@Service
@Slf4j
public class DeviceRegistryServiceGrpcClient {

    private final DeviceRegistryServiceGrpc.DeviceRegistryServiceBlockingStub blockingStub;

    public DeviceRegistryServiceGrpcClient(
        @Value("${device.registry.service.address}") String serverAddress,
        @Value("${device.registry.service.grpc.port}") int serverPort
    ) {

        log.info("Connecting to device-registry-service gRPC {}:{}", serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
                .usePlaintext()
                .build();

        blockingStub = DeviceRegistryServiceGrpc.newBlockingStub(channel);

    }

    public boolean validateDevice(String serialNo) {

        ValidateDeviceRequest request = ValidateDeviceRequest.newBuilder()
                            .setSerialNo(serialNo)
                            .build();
        
        ValidateDeviceResponse response = blockingStub.validateDevice(request);
        log.info("Received gRPC ValidateDeviceResponse {}", response);

        return response.getIsValid();

    }
    
}
