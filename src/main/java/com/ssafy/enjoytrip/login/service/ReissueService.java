package com.ssafy.enjoytrip.login.service;

import com.ssafy.enjoytrip.login.domain.Role;
import com.ssafy.enjoytrip.login.mapper.RefreshMapper;
import com.ssafy.enjoytrip.login.util.JWTUtil;
import com.ssafy.enjoytrip.login.util.UtilFunction;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ReissueService {
    private final JWTUtil jwtUtil;
    private final UtilFunction utilFunction;
    private final RefreshMapper refreshMapper;
    public void reissueAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 쿠키로 받은 refresh token을 확인
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("refresh token null");
            return;
        }

        // 2. 만료 여부 확인
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("refresh token expired");
            return;
        }

        // 3. 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!"refresh".equals(category)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("invalid refresh token");
            return;
        }
        // DB에 저장되어 있는지 확인
        Boolean isExist = refreshMapper.existsByRefresh(refresh);
        if (!isExist) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("invalid refresh token");
            return;
        }

        String username = jwtUtil.getUsername(refresh);
        Role role = jwtUtil.getRole(refresh);
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        refreshMapper.deleteByRefresh(refresh);
        utilFunction.addRefreshEntity(username, newRefresh);

        response.setHeader("access", newAccess);
        Cookie refreshCookie = utilFunction.createCookie("refresh", newRefresh);
        response.addCookie(refreshCookie);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}