package com.ssafy.enjoytrip.login.dto;

import com.ssafy.enjoytrip.login.domain.Role;
import lombok.Data;

@Data
public class JoinDTO {
    private String username;
    private String password;
    private Role role;
}
