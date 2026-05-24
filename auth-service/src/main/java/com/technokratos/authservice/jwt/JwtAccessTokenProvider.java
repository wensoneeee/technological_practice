package com.technokratos.authservice.jwt;

import com.technokratos.authservice.dto.AccountResponse;
import com.technokratos.authservice.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAccessTokenProvider {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration}")
    private long accessExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateAccessToken(Long id, String email, Role role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("role", role.name());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(Instant.now().plusMillis(accessExpiration)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public AccountResponse getUserInfoByToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String email = claims.getSubject();
        String role = claims.get("role", String.class);

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(claims.get("id", Long.class));
        accountResponse.setEmail(email);
        accountResponse.setRole(Role.valueOf(role));
        return accountResponse;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}