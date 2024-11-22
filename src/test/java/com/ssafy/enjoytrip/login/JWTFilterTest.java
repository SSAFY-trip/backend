package com.ssafy.enjoytrip.login;

import com.ssafy.enjoytrip.login.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JWTFilterTest {
    private JWTUtil jwtUtil;
    private TestableJWTFilter jwtFilter;

    @BeforeEach
    public void setUp() {
        jwtUtil = Mockito.mock(JWTUtil.class);
        jwtFilter = new TestableJWTFilter(jwtUtil);
    }

    @Test
    @DisplayName("Access Token이 없을 때 다음 필터로 넘어가야 한다.")
    public void testAccessTokenIsNull() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        jwtFilter.doFilterInternal(request, response, chain);

        // 상태 코드는 기본값 200이어야 함
        assertEquals(200, response.getStatus());
        assertNull(request.getAttribute("userId"));
    }

    @Test
    @DisplayName("만료된 Access Token이 전달되었을 때 401 상태 코드와 메시지를 반환해야 한다.")
    public void testExpiredAccessToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("access", "expiredAccessToken");

        MockHttpServletResponse response = new MockHttpServletResponse();

        Mockito.when(jwtUtil.isExpired("expiredAccessToken")).thenReturn(true);

        jwtFilter.doFilterInternal(request, response, new MockFilterChain());

        // 응답 상태 코드와 메시지 확인
        assertEquals(401, response.getStatus());
        assertEquals("access token expired", response.getContentAsString().trim());
    }

    @Test
    @DisplayName("잘못된 Access Token 카테고리가 전달되었을 때 401 상태 코드와 메시지를 반환해야 한다.")
    public void testInvalidAccessTokenCategory() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("access", "invalidCategoryToken");

        MockHttpServletResponse response = new MockHttpServletResponse();

        Mockito.when(jwtUtil.isExpired("invalidCategoryToken")).thenReturn(false);
        Mockito.when(jwtUtil.getCategory("invalidCategoryToken")).thenReturn("refresh");

        jwtFilter.doFilterInternal(request, response, new MockFilterChain());

        // 응답 상태 코드와 메시지 확인
        assertEquals(401, response.getStatus());
        assertEquals("invalid access token", response.getContentAsString().trim());
    }
}
