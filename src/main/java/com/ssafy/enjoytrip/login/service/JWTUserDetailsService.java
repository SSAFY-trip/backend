package com.ssafy.enjoytrip.login.service;

import com.ssafy.enjoytrip.login.dto.JWTUserDetails;
import com.ssafy.enjoytrip.user.domain.User;
import com.ssafy.enjoytrip.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JWTUserDetailsService implements UserDetailsService {
    private final UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userMapper.findByUsername(username);
        if(user.isPresent()){
            return new JWTUserDetails(user.get());
        }
        throw new UsernameNotFoundException("User not found");
    }
}