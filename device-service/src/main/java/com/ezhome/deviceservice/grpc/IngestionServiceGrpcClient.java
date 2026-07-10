package com.ezhome.deviceservice.grpc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

import ingestion.AddDeviceRequest;
import ingestion.DeleteDeviceRequest;
import ingestion.IngestionServiceGrpc;


@Service
@Slf4j
public class IngestionServiceGrpcClient {

    private final IngestionServiceGrpc.IngestionServiceBlockingStub blockingStub;

    public IngestionServiceGrpcClient(
        @Value("${ingestion.service.address}") String serverAddress,
        @Value("${ingestion.service.grpc.port}") int serverPort
    ) {

        log.info("Connecting to ingestion-service gRPC {}:{}", serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
                .usePlaintext()
                .build();

        blockingStub = IngestionServiceGrpc.newBlockingStub(channel);

    }

    // send device id to add in ingestion-service memory for telemetry validation
    public void addDeviceToIngestionService(String serialNo) {

        AddDeviceRequest request = AddDeviceRequest.newBuilder()
                            .setSerialNo(serialNo)
                            .build();
        
        blockingStub.addDeviceToIngestionService(request);
        log.info("Send gRPC addDeviceToIngestionService request {}", request.toString());

    }

    // send device id to remove it from ingestion-service memory
    public void deleteDeviceFromIngestionService(String serialNo) {

        DeleteDeviceRequest request = DeleteDeviceRequest.newBuilder()
                            .setSerialNo(serialNo)
                            .build();
        
        blockingStub.deleteDeviceFromIngestionService(request);
        log.info("Send gRPC deleteDeviceFromIngestionService request {}", request.toString());

    }
    
}
