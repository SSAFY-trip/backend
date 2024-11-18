package com.ssafy.enjoytrip.user.controller;

import com.ssafy.enjoytrip.user.domain.User;
import com.ssafy.enjoytrip.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * http://localhost:8080/api/signup
     * {
     * "username": "testuser",
     * "password": "testpassword"
     * }
     */
    @PostMapping("/api/signup")
    public boolean addMember(@RequestBody User user) {
        userService.signUp(user);
        return true;
    }

    /**
     * POST http://localhost:8080/api/signin?username=testuser&password=testpassword
     * 아직은 반환값을 true, false로 존재 여부만 확인합니다.
     */
    @PostMapping("/api/signin")
    public boolean signIn(@RequestParam String username, @RequestParam String password) {
        return userService.signIn(username, password);
    }
}
