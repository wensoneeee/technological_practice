package com.technokratos.bookingservice.dto.forms;

import com.technokratos.bookingservice.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserForm{
    String email;
    String password;
    String name;
    Role role;
}
