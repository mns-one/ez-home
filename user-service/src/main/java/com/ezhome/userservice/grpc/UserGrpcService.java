package com.ezhome.userservice.grpc;

import java.util.UUID;

import com.ezhome.userservice.dto.CreateUserDTO;
import com.ezhome.userservice.entity.User;
import com.ezhome.userservice.service.UserService;

import user.UserServiceGrpc.UserServiceImplBase;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import io.grpc.stub.StreamObserver;

import user.CreateUserAccountRequest;
import user.CreateUserAccountResponse;
import user.DeleteUserAccountRequest;
import user.DeleteUserAccountResponse;
import user.pollRequest;
import user.pollResponse;


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

        CreateUserDTO payload = CreateUserDTO.builder()
            .id(UUID.fromString(createUserAccountRequest.getUserId()))
            .email(createUserAccountRequest.getEmail())
            .build();

        User createdUser = userService.createUser(payload);

        CreateUserAccountResponse response = CreateUserAccountResponse.newBuilder()
                .setUserId(createdUser.getId().toString())
                .setUsername(createdUser.getUsername())
                .setEmail(createdUser.getEmail())
                .setCreatedAt(createdUser.getCreatedAt().toString())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteUserAccount(DeleteUserAccountRequest deleteUserAccountRequest, StreamObserver<DeleteUserAccountResponse> responseObserver) {

        // delete a user account in the User-service
        log.info("Recieved gRPC deleteUserAccountRequest {}", deleteUserAccountRequest.toString());

        userService.deleteUser(UUID.fromString(deleteUserAccountRequest.getUserId()));

        DeleteUserAccountResponse response = DeleteUserAccountResponse.newBuilder().setMessage("Account Deleted Sucessfully!").build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
}
