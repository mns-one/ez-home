package com.ezhome.authservice.grpc;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import user.UserServiceGrpc;
import user.pollRequest;
import user.pollResponse;
import user.CreateUserAccountRequest;
import user.CreateUserAccountResponse;
import user.DeleteUserAccountRequest;
import user.DeleteUserAccountResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class UserServiceGrpcClient {

    private final UserServiceGrpc.UserServiceBlockingStub blockingStub;

    public UserServiceGrpcClient(
        @Value("${user.service.address}") String serverAddress,
        @Value("${user.service.grpc.port}") int serverPort
    ) {

        log.info("Connecting to User-service gRPC {}:{}", serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
                .usePlaintext()
                .build();

        blockingStub = UserServiceGrpc.newBlockingStub(channel);

    }

    // Poll the User-service to check if it's available
    public pollResponse pollUserService(String message) {

        pollRequest request = pollRequest.newBuilder()
                .setMessage(message)
                .build();

        pollResponse response = blockingStub.pollUserService(request);
        log.info("Received gRPC pollResponse {}", response);
        return response;

    }

    // Create a new user account in the User-service
    public CreateUserAccountResponse createUserAccount(String userId, String email) {

        CreateUserAccountRequest request = CreateUserAccountRequest.newBuilder()
                .setUserId(userId)
                .setEmail(email)
                .build();

        CreateUserAccountResponse response = blockingStub.createUserAccount(request);
        log.info("Received gRPC CreateUserAccountResponse {}", response);
        return response;

    }

    // Delete a user account in the User-service
    public DeleteUserAccountResponse deleteUserAccount(String userId) {
        
        DeleteUserAccountRequest request = DeleteUserAccountRequest.newBuilder()
                .setUserId(userId)
                .build();

        DeleteUserAccountResponse response = blockingStub.deleteUserAccount(request);
        log.info("Recived gRPC DeleteUserAccountResponse", response);
        return response;

    }
    
}
