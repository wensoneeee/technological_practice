package com.technokratos.authservice.entity;

import lombok.Getter;
import java.util.List;

@Getter
public enum Role {
    USER(List.of("READ")),
    ADMIN(List.of("READ", "WRITE", "DELETE"));

    private final List<String> authorities;

    Role(List<String> authorities) {
        this.authorities = authorities;
    }
}
