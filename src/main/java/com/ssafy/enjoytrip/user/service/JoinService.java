package com.ssafy.enjoytrip.user.service;

import com.ssafy.enjoytrip.user.domain.Role;
import com.ssafy.enjoytrip.user.domain.User;
import com.ssafy.enjoytrip.user.dto.JoinDTO;
import com.ssafy.enjoytrip.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final UserMapper userMapper;
    public boolean joinProcess(JoinDTO joinDTO) {
        if (isUsernameOrNameDuplicate(joinDTO) || isInvalidRole(joinDTO.getRole())) {
            return false;
        }

        saveLocalUser(joinDTO);
        return true;
    }

    private boolean isUsernameOrNameDuplicate(JoinDTO joinDTO) {
        boolean isUsernameExists = userMapper.existsByUsername(joinDTO.getUsername());
        boolean isNameExists = userMapper.existsByName(joinDTO.getName());
        return isUsernameExists || isNameExists;
    }

    private boolean isInvalidRole(Role role) {
        return role == null || !Role.isValidRole(role.name());
    }

    private void saveLocalUser(JoinDTO joinDTO) {
        User user = User.localUserBuilder()
                .username(joinDTO.getUsername())
                .password(joinDTO.getPassword())
                .role(joinDTO.getRole())
                .name(joinDTO.getName())
                .build();
        userMapper.save(user);
    }
}
