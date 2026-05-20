package com.technokratos.bookingservice.dto.forms;

public record PasswordForm(String oldPassword, String newPassword, String confirmPassword) {}
