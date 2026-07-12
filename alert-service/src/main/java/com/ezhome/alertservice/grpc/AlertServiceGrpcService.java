package com.ezhome.alertservice.grpc;

import com.ezhome.alertservice.service.AlertService;

import alert.DeleteAlertServiceRequest;
import alert.DeleteAlertServiceResponse;
import alert.AlertServiceGrpc.AlertServiceImplBase;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;


@GrpcService
@Slf4j
public class AlertServiceGrpcService extends AlertServiceImplBase {

    private final AlertService alertService;

    public AlertServiceGrpcService(AlertService alertService) {
        this.alertService = alertService;
    }

    // delete all existing alerts for requested device
    public void deleteAlertService(DeleteAlertServiceRequest request, StreamObserver<DeleteAlertServiceResponse> responseObserver) {

        log.info("Recieved gRPC DeleteAlertServiceRequest {}", request.toString());

        alertService.deleteAllDeviceAlerts(request.getDeviceId());

        DeleteAlertServiceResponse response = DeleteAlertServiceResponse.newBuilder().build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
    
}
