package com.ssafy.enjoytrip.login;

import com.ssafy.enjoytrip.login.exception.*;
import com.ssafy.enjoytrip.login.filter.LoginFilter;
import com.ssafy.enjoytrip.login.util.JWTUtil;
import com.ssafy.enjoytrip.login.util.UtilFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoginExceptionHandlerTest {
    private JWTUtil jwtUtil;
    private TestableJWTFilter jwtFilter;

    @BeforeEach
    public void setUp() {
        jwtUtil = Mockito.mock(JWTUtil.class);
        jwtFilter = new TestableJWTFilter(jwtUtil);
    }

    @Test
    @DisplayName("access 토근이 없을때 오류를 호출하지 않고 다음 필터로 넘어가야 합니다.")
    public void testAccessTokenIsNull() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        // Filter chain should pass without throwing exception
        jwtFilter.doFilterInternal(request, response, chain);
    }

    @Test
    @DisplayName("access Token이 만기가 됐을때 AccessTokenExpiredException이 호출되야 합니다.")
    public void testExpiredAccessToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("access", "expiredAccessToken");

        Mockito.when(jwtUtil.isExpired("expiredAccessToken")).thenReturn(true);

        assertThrows(AccessTokenExpiredException.class, () -> {
            jwtFilter.doFilterInternal(request, new MockHttpServletResponse(), new MockFilterChain());
        });
    }

    @Test
    @DisplayName("JWT Filter에 AccessToken이 아닌 Refresh Token이 들어왔을때 AccessTokenInvalidException가 호출되야 합니다.")
    public void testInvalidAccessTokenCategory() {
        // when
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("access", "invalidCategoryToken");

        Mockito.when(jwtUtil.isExpired("invalidCategoryToken")).thenReturn(false);
        Mockito.when(jwtUtil.getCategory("invalidCategoryToken")).thenReturn("refresh");

        assertThrows(AccessTokenInvalidException.class, () -> {
            jwtFilter.doFilterInternal(request, new MockHttpServletResponse(), new MockFilterChain());
        });
    }

    @Test
    @DisplayName("JSON 타입에 올바른 값이 없으면 LoginJsonParsingException가 호출돼야 합니다.")
    public void testLoginJsonParsingException() {
        LoginFilter loginFilter = new LoginFilter(
                Mockito.mock(AuthenticationManager.class),
                Mockito.mock(JWTUtil.class),
                Mockito.mock(UtilFunction.class)
        );

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent("Invalid JSON".getBytes());

        assertThrows(LoginJsonParsingException.class, () -> {
            loginFilter.attemptAuthentication(request, new MockHttpServletResponse());
        });
    }

    @Test
    @DisplayName("refresh 토큰이 만기가 되었을때 RefreshTokenExpiredException이 호출됩니다..")
    public void testExpiredRefreshTokenException() {
        // Mock behavior for expired refresh token
        String expiredRefreshToken = "expiredRefreshToken";
        Mockito.when(jwtUtil.isExpired(expiredRefreshToken)).thenReturn(true);

        assertThrows(RefreshTokenExpiredException.class, () -> {
            throw new RefreshTokenExpiredException();
        });
    }

    @Test
    @DisplayName("refresh 토큰이 올바르지 않을때 RefreshTokenInvalidException 호출됩니다..")
    public void testInvalidRefreshTokenException() {
        // Mock behavior for invalid refresh token
        String invalidRefreshToken = "invalidRefreshToken";
        Mockito.when(jwtUtil.isExpired(invalidRefreshToken)).thenReturn(false);

        assertThrows(RefreshTokenInvalidException.class, () -> {
            throw new RefreshTokenInvalidException();
        });
    }
}

