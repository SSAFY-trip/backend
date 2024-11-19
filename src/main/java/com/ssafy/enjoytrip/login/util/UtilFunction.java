package com.ssafy.enjoytrip.login.util;

import com.ssafy.enjoytrip.login.domain.Refresh;
import com.ssafy.enjoytrip.login.mapper.RefreshMapper;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
public class UtilFunction {
    private final RefreshMapper refreshMapper;
    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }
    public void addRefreshEntity(String username, String refresh) {
        Timestamp expiration = new Timestamp(System.currentTimeMillis() + 86400000L);

        Refresh refreshEntity = new Refresh();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(expiration);

        refreshMapper.save(refreshEntity);
    }
}
