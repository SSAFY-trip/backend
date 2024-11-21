package com.ssafy.enjoytrip.login.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.enjoytrip.login.dto.JWTUserDetails;
import com.ssafy.enjoytrip.user.domain.Role;
import com.ssafy.enjoytrip.login.util.JWTUtil;
import com.ssafy.enjoytrip.login.util.UtilFunction;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UtilFunction utilFunction;
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = null;
        String password = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
            username = requestBody.get("username");
            password = requestBody.get("password");
        } catch (IOException e) {
            throw new RuntimeException("JSON 파싱 오류", e);
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        JWTUserDetails userDetails = (JWTUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Role role = Role.valueOf(userDetails.getAuthorities().iterator().next().getAuthority());
        Long userId = userDetails.getUserId();

        String access = jwtUtil.createJwt("access", username, role, 600000L, userId);
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L, userId);

        utilFunction.addRefreshEntity(username, refresh);

        response.setHeader("access", access);
        response.addCookie(utilFunction.createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}