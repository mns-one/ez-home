package com.ezhome.ingestionservice.grpc;

import ingestion.IngestionServiceGrpc.IngestionServiceImplBase;

import com.ezhome.ingestionservice.redis.DeviceIdCache;

import ingestion.AddDeviceRequest;
import ingestion.AddDeviceResponse;
import ingestion.DeleteDeviceRequest;
import ingestion.DeleteDeviceResponse;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;


@GrpcService
@Slf4j
public class IngestionServiceGrpcService extends IngestionServiceImplBase {

    private final DeviceIdCache deviceIdCache;

    public IngestionServiceGrpcService(DeviceIdCache deviceIdCache) {
        this.deviceIdCache = deviceIdCache;
    }

    // add recieved device Id to redis for telemetry validation
    public void addDeviceToIngestionService(AddDeviceRequest request, StreamObserver<AddDeviceResponse> responseObserver) {

        log.info("Recieved gRPC addDeviceToIngestionService {}", request.toString());

        deviceIdCache.addDeviceId(request.getSerialNo());

        AddDeviceResponse response = AddDeviceResponse.newBuilder().build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    // delete recieved device Id from redis
    public void deleteDeviceFromIngestionService(DeleteDeviceRequest request, StreamObserver<DeleteDeviceResponse> responseObserver) {

        log.info("Recieved gRPC deleteDeviceToIngestionService {}", request.toString());

        deviceIdCache.removeDeviceId(request.getSerialNo());

        DeleteDeviceResponse response = DeleteDeviceResponse.newBuilder().build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
    
}
