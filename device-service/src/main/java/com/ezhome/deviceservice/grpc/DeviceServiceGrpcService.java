package com.ezhome.deviceservice.grpc;

import org.springframework.data.domain.Page;

import com.ezhome.deviceservice.service.DeviceService;

import device.StreamDeviceIdsRequest;
import device.StreamDeviceIdsResponse;
import device.DeviceServiceGrpc.DeviceServiceImplBase;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;


@GrpcService
@Slf4j
public class DeviceServiceGrpcService extends DeviceServiceImplBase {

    private final DeviceService deviceService;

    public DeviceServiceGrpcService (DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    // fetch and stream all device ids from db to ingestion-service in batches
    public void streamAllDeviceIds(StreamDeviceIdsRequest request, StreamObserver<StreamDeviceIdsResponse> responseObserver) {

        int pageSize = request.getPageSize() > 0 ? request.getPageSize() : 100;
        int pageNumber = 0;

        while (true) {
            Page<String> page = deviceService.fetchDeviceIdPage(pageNumber, pageSize);
            if (page.isEmpty()) {
                break;
            }

            responseObserver.onNext(
                StreamDeviceIdsResponse.newBuilder()
                    .addAllSerialNos(page.getContent())
                    .build());

            if (!page.hasNext()) {
                break;
            }

            pageNumber++;
        }

        responseObserver.onCompleted();                  
    }
    
}
