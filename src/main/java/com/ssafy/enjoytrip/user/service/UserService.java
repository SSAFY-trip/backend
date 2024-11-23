package com.ssafy.enjoytrip.user.service;

import com.ssafy.enjoytrip.login.dto.JWTUserDetails;
import com.ssafy.enjoytrip.user.domain.User;
import com.ssafy.enjoytrip.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    public User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User findMember = userMapper.findByUsername(username)
                .orElseThrow(RuntimeException::new);

        return findMember;
    }
    public Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof JWTUserDetails)) {
            throw new RuntimeException("User not authenticated");
        }
        JWTUserDetails userDetails = (JWTUserDetails) auth.getPrincipal();
        return userDetails.getUserId();
    }

}
