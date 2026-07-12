package com.ezhome.deviceservice.grpc;

import com.ezhome.deviceservice.service.DeviceService;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import validate.ValidateUserDeviceRequest;
import validate.ValidateUserDeviceResponse;
import validate.ValidateUserDeviceServiceGrpc.ValidateUserDeviceServiceImplBase;

@GrpcService
@Slf4j
public class DeviceValidationGrpcService extends ValidateUserDeviceServiceImplBase {

    private final DeviceService deviceService;

    public DeviceValidationGrpcService (DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    // process user and device validation request from alert-service
    public void validateUserDevice(ValidateUserDeviceRequest request, StreamObserver<ValidateUserDeviceResponse> responseObserver) {

        log.info("Recieved gRPC ValidateUserDeviceRequest {}", request.toString());

        boolean status = deviceService.validateUserDevice(request.getDeviceId(), request.getUserId());

        ValidateUserDeviceResponse response = ValidateUserDeviceResponse.newBuilder()
                                .setIsValid(status)
                                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }


    
}
