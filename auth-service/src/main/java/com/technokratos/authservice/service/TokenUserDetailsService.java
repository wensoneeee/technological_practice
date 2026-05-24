package com.technokratos.authservice.service;

import com.technokratos.authservice.dto.AccountResponse;
import com.technokratos.authservice.dto.TokenUserAccount;
import com.technokratos.authservice.entity.User;
import com.technokratos.authservice.jwt.JwtAccessTokenProvider;
import com.technokratos.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        AccountResponse accountResponse = new AccountResponse(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
        return new TokenUserAccount(accountResponse);
    }
}
