package com.ssafy.enjoytrip.user.dto;

import com.ssafy.enjoytrip.global.EnumValue;
import com.ssafy.enjoytrip.user.domain.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class JoinDTO {
    @NotBlank(message = "username is required")
    private String username;
    @NotBlank(message = "password is required")
    private String password;
    @EnumValue(enumClass = Role.class, message = "Role must be one of USER, ADMIN, MODERATOR")
    private Role role;
    @NotBlank(message = "Name is required")
    private String name;
}
