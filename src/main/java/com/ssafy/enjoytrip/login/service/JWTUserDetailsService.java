package com.ssafy.enjoytrip.login.service;

import com.ssafy.enjoytrip.login.details.JWTUserDetails;
import com.ssafy.enjoytrip.login.domain.User;
import com.ssafy.enjoytrip.login.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JWTUserDetailsService implements UserDetailsService {
    private final UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if(user != null){
            return new JWTUserDetails(user);
        }
        throw new UsernameNotFoundException("User not found");
    }
}