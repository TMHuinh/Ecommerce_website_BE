package com.example.apigateway.cofiguration;

import com.example.apigateway.dto.response.ApiResponse;
import com.example.apigateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {
    @Autowired
    IdentityService identityService;
    @Autowired
    ObjectMapper objectMapper;
    final String[] publicEndPoint = {
            "/identity-service/auth/login",
            "/identity-service/auth/introspect",
            "/identity-service/account",
            "/identity-service/user",
            "/identity-service/user/.*",
            "/product-service/product",
            "/product-service/product/.*",
            "/product-service/product/page",
            "/product-service/product/fbn/.*",
            "/product-service/product-type",
            "/product-service/producttype",
            "/review-service/review",
            "/review-service/review/.*",
            "/order-service/address",
            "/order-service/address/.*",
            "/order-service/websocket/.*"
    };
    @Value("${app.api-prefix}")
    String apiPrefix;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().toString();
        
        log.info("━━━ REQUEST: {} {} ━━━", method, path);
        
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        
        if (ispublicEndPoint(exchange.getRequest())) {
            log.info("✓ Public endpoint - allowing access without token");
            return chain.filter(exchange)
                    .onErrorResume(throwable -> serviceUnavailable(exchange.getResponse()));
        }
        
        log.info("🔐 Private endpoint - checking for token");
        if (CollectionUtils.isEmpty(authHeader)) {
            log.warn("❌ No Authorization header found");
            return unauthenticated(exchange.getResponse());
        }
        
        String token = authHeader.get(0).replace("Bearer", "").trim();
        log.info("Token: {}...", token.substring(0, Math.min(20, token.length())));
        
        return identityService.introspect(token).flatMap(introspectResponseApiResponse -> {
            if (introspectResponseApiResponse.getResult().isValid()) {
                log.info("✓ Token valid - allowing request");
                return chain.filter(exchange)
                        .onErrorResume(throwable -> serviceUnavailable(exchange.getResponse()));
            } else {
                log.warn("❌ Token invalid");
                return unauthenticated(exchange.getResponse());
            }
        }).onErrorResume(throwable -> {
            log.error("❌ Token introspection error: {}", throwable.getMessage());
            return unauthenticated(exchange.getResponse());
        });
    }

    @Override
    public int getOrder() {
        return -1;
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(1401)
                .message("Unauthenticated")
                .build();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
    Mono<Void> serviceUnavailable(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(1503)
                .message("Service Unavailable")
                .build();
        response.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
        String body;
        try {
            // Chuyển đổi ApiResponse thành chuỗi JSON
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing ApiResponse", e);
        }
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
     boolean ispublicEndPoint(ServerHttpRequest request) {
         String path = request.getURI().getPath();
         log.info("📍 Checking public endpoint for path: {}", path);
         
         boolean isPublic = Arrays.stream(publicEndPoint).anyMatch(pattern -> {
             String fullPattern = apiPrefix + pattern;
             boolean matches = false;
             
             // Nếu pattern có /*, bỏ regex và chỉ dùng startsWith
             if (pattern.contains(".*")) {
                 String basePattern = apiPrefix + pattern.replace("/.*", "");
                 matches = path.startsWith(basePattern);
                 if (matches) log.info("  ✓ Regex pattern matched: {} starts with {}", path, basePattern);
             } else {
                 // Literal pattern - check exact match hoặc startsWith
                 matches = path.equals(fullPattern) || path.startsWith(fullPattern + "/");
                 if (matches) log.info("  ✓ Literal pattern matched: {}", fullPattern);
             }
             
             return matches;
         });
         
         if (isPublic) {
             log.info("✅ PUBLIC endpoint - allowing access WITHOUT token");
         } else {
             log.warn("🔐 PRIVATE endpoint - requires authentication");
         }
         
         return isPublic;
     }
}
