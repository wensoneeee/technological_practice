package com.technokratos.apigateway.filter;

import com.technokratos.apigateway.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtUtil jwtUtil;

    private static final List<String> OPEN_ENDPOINTS = List.of(
            "/auth/", "/api/v1/auth/", "/api/v1/auth/login", "/api/v1/auth/sign-up", "/sign-in", "sign-up",
            "/swagger-ui/", "/booking-service/v3/api-docs", "/v3/api-docs", "/webjars/", "/v3/api-docs");

    public AuthenticationFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            boolean isTokenRequired = OPEN_ENDPOINTS.stream()
                    .noneMatch(path::startsWith);

            if (!isTokenRequired) {
                return chain.filter(exchange);
            }
            String token = extractToken(request);

            if (token == null) {
                exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
                exchange.getResponse().getHeaders().setLocation(URI.create("/sign-in"));
                return exchange.getResponse().setComplete();
            }

            try {
                Claims claims = jwtUtil.extractAllClaims(token);
                String userEmail = claims.getSubject();
                String role = claims.get("role", String.class);

                request = exchange.getRequest()
                        .mutate()
                        .header("X-User-Email", userEmail)
                        .header("X-User-Role", role)
                        .build();

            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
                exchange.getResponse().getHeaders().setLocation(URI.create("/sign-in"));
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange.mutate().request(request).build());
        };
    }

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
