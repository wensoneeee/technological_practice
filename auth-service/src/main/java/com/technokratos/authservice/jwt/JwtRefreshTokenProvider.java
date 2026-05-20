package com.technokratos.authservice.jwt;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtRefreshTokenProvider {

    public String generateRefreshToken(String email) {
        return UUID.randomUUID().toString();
    }
}

