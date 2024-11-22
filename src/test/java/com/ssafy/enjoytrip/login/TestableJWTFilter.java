package com.ssafy.enjoytrip.login;

import com.ssafy.enjoytrip.login.filter.JWTFilter;
import com.ssafy.enjoytrip.login.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class TestableJWTFilter extends JWTFilter {
    public TestableJWTFilter(JWTUtil jwtUtil) {
        super(jwtUtil);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        super.doFilterInternal(request, response, filterChain);
    }
}
