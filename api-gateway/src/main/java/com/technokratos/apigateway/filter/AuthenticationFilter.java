package com.technokratos.apigateway.filter;

import com.technokratos.apigateway.jwt.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtUtil jwtUtil;

    private static final List<String> OPEN_ENDPOINTS = List.of(
            "/auth/", "/api/v1/auth/", "/api/v1/auth/login", "/api/v1/auth/sign-up",
            "/swagger-ui/", "/booking-service/v3/api-docs", "/v3/api-docs", "/webjars/", "/v3/api-docs");

    public AuthenticationFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    // apply возвращает GatewayFilter, который Spring Gateway вызывает для каждого запроса
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            boolean isTokenRequired = OPEN_ENDPOINTS.stream()
                    .noneMatch(path::startsWith);
            // пропускаем публ пути, где не нужен токен
            if (!isTokenRequired) {
                return chain.filter(exchange);
            }
            String token = extractToken(request);

            if (token == null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            try {
                //парсим JWT и проверяем его подпись через JwtUtil
                jwtUtil.validateToken(token);

                String userEmail = jwtUtil.extractEmail(token);
                String role = jwtUtil.extractRole(token);

                // "подменяем" запрос, добавляя заголовки, которые увидят наши микросервисы
                request = exchange.getRequest()
                        .mutate()
                        .header("X-User-Email", userEmail)
                        .header("X-User-Role", role)
                        .build();
            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    // пытаемся найти токен в куках или заголовках
    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        HttpCookie cookie = request.getCookies().getFirst("JWT");
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    public static class Config {
    }
}