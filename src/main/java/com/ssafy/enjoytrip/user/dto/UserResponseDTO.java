package com.ssafy.enjoytrip.user.dto;

import com.ssafy.enjoytrip.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDTO {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String role;

    /**
     * User 엔터티를 DTO로 변환
     */
    public static UserResponseDTO of(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name()) // Role을 문자열로 변환
                .build();
    }

    /**
     * DTO를 User 엔터티로 변환
     */
    public User toEntity() {
        return User.localUserBuilder()
                .username(username)
                .name(name)
                .build();
    }
}
