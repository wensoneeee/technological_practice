package com.technokratos.bookingservice.dto.forms;

import com.technokratos.bookingservice.models.Role;

public record UserForm(String email, String password, String name, Role role) {}
