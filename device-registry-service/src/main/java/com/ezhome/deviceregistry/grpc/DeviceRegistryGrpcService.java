package com.ezhome.deviceregistry.grpc;

import com.ezhome.deviceregistry.service.DeviceService;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import registry.ValidateDeviceRequest;
import registry.ValidateDeviceResponse;
import registry.DeviceRegistryServiceGrpc.DeviceRegistryServiceImplBase;

@GrpcService
@Slf4j
public class DeviceRegistryGrpcService extends DeviceRegistryServiceImplBase {

    private final DeviceService deviceService;

    public DeviceRegistryGrpcService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    // process device validation request from device-service
    public void validateDevice(ValidateDeviceRequest request, StreamObserver<ValidateDeviceResponse> responseObserver) {

        log.info("Recieved gRPC ValidateDeviceRequest {}", request.toString());

        boolean status = deviceService.validateDevice(request.getSerialNo());

        ValidateDeviceResponse response = ValidateDeviceResponse.newBuilder()
                                .setIsValid(status)
                                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
    
}
