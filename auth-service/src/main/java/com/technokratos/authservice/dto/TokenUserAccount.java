package com.technokratos.authservice.dto;

import com.technokratos.authservice.entity.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TokenUserAccount implements UserDetails {

    private final AccountResponse accountResponse;

    public TokenUserAccount(AccountResponse accountResponse) {
        this.accountResponse = accountResponse;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Role role = accountResponse.getRole();
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_%s".formatted(role.name())));

        List<GrantedAuthority> privileges = role.getAuthorities().stream()
                .map(auth -> new SimpleGrantedAuthority(auth))
                .collect(Collectors.toList());

        authorities.addAll(privileges);

        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return accountResponse.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
