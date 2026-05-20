package com.technokratos.authservice.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    private String password;
    private String fullName;
}
