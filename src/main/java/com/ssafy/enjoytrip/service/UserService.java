package com.ssafy.enjoytrip.service;

import com.ssafy.enjoytrip.domain.User;

public interface UserService {
    void signUp(User user);
    boolean signIn(String username, String rawPassword);
}