package com.ezhome.userservice.grpc;

import com.ezhome.userservice.dto.CreateUserAccountDTO;
import com.ezhome.userservice.dto.UserAccountDTO;
import com.ezhome.userservice.service.UserService;

import user.UserServiceGrpc.UserServiceImplBase;
import com.google.protobuf.Empty;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import user.CreateUserAccountRequest;
import user.CreateUserAccountResponse;
import user.DeleteUserAccountRequest;
import user.pollRequest;
import user.pollResponse;

import io.grpc.stub.StreamObserver;


@GrpcService
@Slf4j
public class UserGrpcService extends UserServiceImplBase {

    private final UserService userService;

    public UserGrpcService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void pollUserService(pollRequest request, StreamObserver<pollResponse> responseObserver) {

        // poll functionality to check if the User-service is available
        log.info("Recieved gRPC pollRequest {}", request.toString());

        pollResponse response = pollResponse.newBuilder()
                .setMessage("pong")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createUserAccount(CreateUserAccountRequest createUserAccountRequest, StreamObserver<CreateUserAccountResponse> responseObserver) {

        // create a new user account in the User-service
        log.info("Recieved gRPC createUserAccountRequest {}", createUserAccountRequest.toString());

        CreateUserAccountDTO payload = CreateUserAccountDTO.builder()
            .username(createUserAccountRequest.getUsername())
            .email(createUserAccountRequest.getEmail())
            .build();

        UserAccountDTO createdUser = userService.createUserAccount(payload);

        CreateUserAccountResponse response = CreateUserAccountResponse.newBuilder()
                .setId(String.valueOf(createdUser.getUserId()))
                .setUsername(createdUser.getUsername())
                .setEmail(createdUser.getEmail())
                .setCreatedAt(String.valueOf(createdUser.getCreatedAt()))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteUserAccount(DeleteUserAccountRequest deleteUserAccountRequest, StreamObserver<Empty> responseObserver) {

        // delete a user account in the User-service
        log.info("Recieved gRPC deleteUserAccountRequest {}", deleteUserAccountRequest.toString());

        userService.deleteUserAccount(deleteUserAccountRequest.getId());

        responseObserver.onNext(
            Empty.newBuilder().build()
        );
        responseObserver.onCompleted();
    }
    
}
