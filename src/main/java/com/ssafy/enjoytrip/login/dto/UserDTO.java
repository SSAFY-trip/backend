package com.ssafy.enjoytrip.login.dto;

import com.ssafy.enjoytrip.login.domain.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class UserDTO {
    private Role role;
    private String name;
    private String username;
}