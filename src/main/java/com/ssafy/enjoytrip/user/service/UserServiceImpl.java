package com.ssafy.enjoytrip.user.service;

import com.ssafy.enjoytrip.user.domain.User;
import com.ssafy.enjoytrip.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public void signUp(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.addPassword(encodedPassword);
        userMapper.save(user);
    }

    @Override
    public boolean signIn(String username, String rawPassword) {
        User user = userMapper.findByUsername(username);

        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
            return true;
        }
        return false;
    }
}