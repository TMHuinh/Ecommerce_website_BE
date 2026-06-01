package com.example.apigateway.cofiguration;

import com.example.apigateway.dto.response.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class IpRateLimitFilter implements GlobalFilter, Ordered {
    final ObjectMapper objectMapper;
    final ConcurrentMap<String, RequestCounter> requestCounters = new ConcurrentHashMap<>();

    @Value("${app.rate-limit.enabled:true}")
    boolean enabled;

    @Value("${app.rate-limit.max-requests:100}")
    int maxRequests;

    @Value("${app.rate-limit.window-seconds:60}")
    long windowSeconds;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!enabled || isCorsPreflight(exchange.getRequest())) {
            return chain.filter(exchange);
        }

        String clientIp = getClientIp(exchange.getRequest());
        long now = System.currentTimeMillis();
        long windowMillis = Duration.ofSeconds(windowSeconds).toMillis();

        RequestCounter counter = requestCounters.compute(clientIp, (ip, currentCounter) -> {
            if (currentCounter == null || now >= currentCounter.windowStart + windowMillis) {
                return new RequestCounter(now);
            }

            currentCounter.count.incrementAndGet();
            return currentCounter;
        });

        int requestCount = counter.count.get();
        if (requestCount > maxRequests) {
            log.warn("Rate limit exceeded for IP {}: {}/{} requests", clientIp, requestCount, maxRequests);
            return tooManyRequests(exchange.getResponse());
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -2;
    }

    private boolean isCorsPreflight(ServerHttpRequest request) {
        return "OPTIONS".equalsIgnoreCase(request.getMethod().toString());
    }

    private String getClientIp(ServerHttpRequest request) {
        String forwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        String realIp = request.getHeaders().getFirst("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }

        InetSocketAddress remoteAddress = request.getRemoteAddress();
        if (remoteAddress != null && remoteAddress.getAddress() != null) {
            return remoteAddress.getAddress().getHostAddress();
        }

        return "unknown";
    }

    private Mono<Void> tooManyRequests(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(1429)
                .message("Too many requests")
                .build();
        response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");

        String body;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing ApiResponse", e);
        }

        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8))));
    }

    static class RequestCounter {
        final long windowStart;
        final AtomicInteger count = new AtomicInteger(1);

        RequestCounter(long windowStart) {
            this.windowStart = windowStart;
        }
    }
}
