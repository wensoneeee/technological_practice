package com.technokratos.bookingservice.dto.dtos;

import com.technokratos.bookingservice.models.Role;
import lombok.Builder;
import lombok.Data;
import com.technokratos.bookingservice.models.User;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Long imageId;
    private Role role;

    public static UserDto of(User user) {
        UserDto userDto = UserDto.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
        return userDto;
    }

    public static List<UserDto> from(List<User> users) {
        return users.stream().map(UserDto::of).collect(Collectors.toList());
    }
}
