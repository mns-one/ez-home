package com.ezhome.ingestionservice.grpc;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ezhome.ingestionservice.redis.DeviceIdCache;

import device.DeviceServiceGrpc;
import device.StreamDeviceIdsRequest;
import device.StreamDeviceIdsResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class DeviceServiceGrpcClient {

    private final DeviceServiceGrpc.DeviceServiceBlockingStub blockingStub;
    private final DeviceIdCache deviceIdCache;

    public DeviceServiceGrpcClient(
        @Value("${device.service.address}") String serverAddress,
        @Value("${device.service.grpc.port}") int serverPort,
        DeviceIdCache deviceIdCache
    ) {

        log.info("Connecting to device-registry-service gRPC {}:{}", serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
                .usePlaintext()
                .build();

        blockingStub = DeviceServiceGrpc.newBlockingStub(channel);
        this.deviceIdCache = deviceIdCache;

    }

    // request all registered device ids from device-service to fill redis for validation
    public void streamAllDeviceIds(int pageSize) {

        StreamDeviceIdsRequest request = StreamDeviceIdsRequest.newBuilder()
            .setPageSize(pageSize)
            .build();

        Iterator<StreamDeviceIdsResponse> responses = blockingStub.streamAllDeviceIds(request);
        log.info("fetching ids....");
        while (responses.hasNext()) {
            deviceIdCache.loadDevices(responses.next().getSerialNosList());
        }

    }
    
}
