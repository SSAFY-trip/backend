package com.ssafy.enjoytrip.login.dto;

import com.ssafy.enjoytrip.global.EnumValue;
import com.ssafy.enjoytrip.login.domain.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class UserDTO {
    @EnumValue(enumClass = Role.class, message = "Role must be one of USER, ADMIN, MODERATOR")
    private Role role;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "username is required")
    private String username;
}