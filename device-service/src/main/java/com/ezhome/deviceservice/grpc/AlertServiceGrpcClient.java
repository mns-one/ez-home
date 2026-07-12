package com.ezhome.deviceservice.grpc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import alert.AlertServiceGrpc;
import alert.DeleteAlertServiceRequest;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class AlertServiceGrpcClient {

    private final AlertServiceGrpc.AlertServiceBlockingStub blockingStub;

    public AlertServiceGrpcClient(
        @Value("${alert.service.address}") String serverAddress,
        @Value("${alert.service.grpc.port}") int serverPort
    ) {

        log.info("Connecting to alert-service gRPC {}:{}", serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
                .usePlaintext()
                .build();

        blockingStub = AlertServiceGrpc.newBlockingStub(channel);

    }

    // send deviceId to delete all existing alerts for device
    public void deleteAlertService(String deviceId) {

        DeleteAlertServiceRequest request = DeleteAlertServiceRequest.newBuilder()
                            .setDeviceId(deviceId)
                            .build();
        
        blockingStub.deleteAlertService(request);
        log.info("Send gRPC deleteAlertService request {}", request.toString());

    }
    
}
