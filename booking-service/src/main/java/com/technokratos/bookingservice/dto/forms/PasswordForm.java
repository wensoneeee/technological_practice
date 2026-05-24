package com.technokratos.bookingservice.dto.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordForm {
    String oldPassword;
    String newPassword;
    String confirmPassword;
}
