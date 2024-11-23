package com.ssafy.enjoytrip.login.interceptor;

import com.ssafy.enjoytrip.user.domain.User;
import com.ssafy.enjoytrip.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class TripMemberInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 인증된 사용자 정보 가져오기
        User authenticatedUser = userService.getAuthenticatedUser();

        // 예제: 사용자의 팀 정보를 확인하는 로직
        if (authenticatedUser == null || !isUserInTeam(authenticatedUser)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("You are not authorized to access this team.");
            return false; // 요청 차단
        }

        // 사용자 팀 정보를 request attribute로 저장 (필요하면)
        request.setAttribute("userTeam", getUserTeam(authenticatedUser));

        return true; // 요청 진행
    }

    private boolean isUserInTeam(User user) {
        // 팀 속성 확인 로직 (예: 데이터베이스 또는 캐시 조회)
        // 예를 들어, userService.isUserInTeam(userId, teamId) 같은 메서드 호출
        return true; // 사용자 팀 확인 로직
    }

    private String getUserTeam(User user) {
        // 사용자가 속한 팀을 반환 (필요시 DB 또는 캐시에서 조회)
        return "ExampleTeam"; // 예제
    }
}
