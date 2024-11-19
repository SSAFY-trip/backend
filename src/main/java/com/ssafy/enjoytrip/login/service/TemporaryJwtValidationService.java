package com.ssafy.enjoytrip.login.service;

import com.ssafy.enjoytrip.login.domain.Role;
import com.ssafy.enjoytrip.login.util.JWTUtil;
import com.ssafy.enjoytrip.login.util.UtilFunction;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemporaryJwtValidationService {
    private final JWTUtil jwtUtil;
    private final UtilFunction utilFunction;

    public void verifyTemporaryTokenAndIssueTokens(String temporaryToken, HttpServletResponse response) {
        if (jwtUtil.isExpired(temporaryToken)) {
            throw new IllegalArgumentException("Temporary token expired");
        }

        String username = jwtUtil.getUsername(temporaryToken);
        Role role = jwtUtil.getRole(temporaryToken);

        if (username == null || role == null) {
            throw new IllegalArgumentException("Invalid token");
        }

        String access = jwtUtil.createJwt("access", username, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        utilFunction.addRefreshEntity(username, refresh);

        response.setHeader("access", access);
        response.addCookie(utilFunction.createCookie("refresh", refresh));
    }
}
