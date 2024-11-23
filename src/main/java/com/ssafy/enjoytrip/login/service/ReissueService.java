package com.ssafy.enjoytrip.login.service;

import com.ssafy.enjoytrip.login.exception.RefreshTokenExpiredException;
import com.ssafy.enjoytrip.login.exception.RefreshTokenInvalidException;
import com.ssafy.enjoytrip.login.exception.RefreshTokenNotExistException;
import com.ssafy.enjoytrip.user.domain.Role;
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
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ReissueService {
    private final JWTUtil jwtUtil;
    private final UtilFunction utilFunction;
    private final RefreshMapper refreshMapper;
    public void reissueAccessToken(HttpServletRequest request, HttpServletResponse response){
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null)
            throw new RefreshTokenNotExistException();
        for (Cookie cookie : cookies)
            if ("refresh".equals(cookie.getName()))
                refresh = cookie.getValue();

        if (refresh == null)
            throw new RefreshTokenNotExistException();
        if(jwtUtil.isExpired(refresh))
            throw new RefreshTokenExpiredException();
        if (!"refresh".equals(jwtUtil.getCategory(refresh)) || !refreshMapper.existsByRefresh(refresh))
            throw new RefreshTokenInvalidException();

        String username = jwtUtil.getUsername(refresh);
        Role role = jwtUtil.getRole(refresh);
        Long userId = jwtUtil.getUserId(refresh);

        String newAccess = jwtUtil.createJwt("access", username, role, 600000L, userId);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L, userId);

        refreshMapper.deleteByRefresh(refresh);
        utilFunction.addRefreshEntity(username, newRefresh);

        response.setHeader("access", newAccess);
        Cookie refreshCookie = utilFunction.createCookie("refresh", newRefresh);
        response.addCookie(refreshCookie);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}