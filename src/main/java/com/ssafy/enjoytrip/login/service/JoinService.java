package com.ssafy.enjoytrip.login.service;

import com.ssafy.enjoytrip.login.domain.Role;
import com.ssafy.enjoytrip.login.domain.User;
import com.ssafy.enjoytrip.login.dto.JoinDTO;
import com.ssafy.enjoytrip.login.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final UserMapper userMapper;
    public boolean joinProcess(JoinDTO joinDTO){
        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();

        boolean isExist = userMapper.existsByUsername(username);
        if(isExist) return false;
        if (joinDTO.getRole() == null || !Role.isValidRole(joinDTO.getRole().name())) {
            return false;
        }
        User user = User.localUserBuilder()
                .username(username)
                .password(password)
                .role(joinDTO.getRole())
                .build();
        userMapper.save(user);

        return true;
    }
}
