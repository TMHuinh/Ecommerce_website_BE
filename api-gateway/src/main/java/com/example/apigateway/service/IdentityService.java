package com.example.apigateway.service;

import com.example.apigateway.dto.request.IntrospectRequest;
import com.example.apigateway.dto.response.ApiResponse;
import com.example.apigateway.dto.response.IntrospectResponse;
import com.example.apigateway.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Service
@RequiredArgsConstructor
public class IdentityService {
    IdentityClient identityClient;
    public Mono<ApiResponse<IntrospectResponse>> introspect(String token){
        return identityClient.introspect(IntrospectRequest.builder()
                .token(token)
                .build());
    }
}
