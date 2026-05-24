package com.technokratos.authservice.service;

import com.technokratos.authservice.client.BookingServiceClient;
import com.technokratos.authservice.dto.AccountResponse;
import com.technokratos.authservice.dto.AuthResponse;
import com.technokratos.authservice.entity.RefreshToken;
import com.technokratos.authservice.entity.Role;
import com.technokratos.authservice.entity.User;
import com.technokratos.authservice.jwt.JwtAccessTokenProvider;
import com.technokratos.authservice.jwt.JwtRefreshTokenProvider;
import com.technokratos.authservice.repository.RefreshTokenRepository;
import com.technokratos.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtRefreshTokenProvider jwtRefreshTokenProvider;
    private final JwtAccessTokenProvider jwtAccessTokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final BookingServiceClient bookingServiceClient;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    @Transactional
    public AuthResponse signUp(String email, String password, String name) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Юзер с таким логином уже существует");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setRole(Role.USER);

        user = userRepository.save(user);

        try {
            bookingServiceClient.createProfile(user.getEmail(), user.getName());
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать профиль в booking-service", e);
        }



        return generateTokens(user);
    }

    @Transactional
    public AuthResponse login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        refreshTokenRepository.deleteByUser(user);
        return generateTokens(user);
    }

    public AccountResponse validateAndGetAccount(String token) {
        if (jwtAccessTokenProvider.validateToken(token)) {
            return jwtAccessTokenProvider.getUserInfoByToken(token);
        }
        throw new RuntimeException("Некорректный токен");
    }

    public AuthResponse refresh(String oldRefreshToken) {
        RefreshToken refreshToken= refreshTokenRepository.findByToken(oldRefreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh-token не найден"));

        if (refreshToken.isRevoked()) {
            throw new RuntimeException("токен уже был отозван");
        }

        if (refreshToken.getExpDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("срок действия refresh-токена истек");
        }

        User user = refreshToken.getUser();

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        return generateTokens(user);
    }

    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    private AuthResponse generateTokens(User user) {
        AccountResponse account = new AccountResponse();
        account.setId(user.getId());
        account.setEmail(user.getEmail());
        account.setRole(user.getRole());

        String accessToken = jwtAccessTokenProvider.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
        String newRefreshToken = jwtRefreshTokenProvider.generateRefreshToken(user.getEmail());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(newRefreshToken);
        refreshToken.setUser(user);
        refreshToken.setExpDate(Instant.now().plusMillis(refreshExpiration));
        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(accessToken, newRefreshToken);
    }
}

