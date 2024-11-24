package com.ssafy.enjoytrip.global.interceptor;

import com.ssafy.enjoytrip.tripmember.mapper.TripMemberMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TripMemberInterceptor implements HandlerInterceptor {
    private final TripMemberMapper tripMemberMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // Request에서 userId와 tripId 가져오기
        Long userId = (Long) request.getAttribute("userId");

        Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long tripId = Long.valueOf(String.valueOf(pathVariables.get("tripId")));

        if (tripId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Trip ID is missing or invalid");
            return false;
        }

        // Trip 멤버 여부 확인
        boolean exists = tripMemberMapper.existsByUserIdAndTripId(userId, tripId);

        if (!exists) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a member of this trip");
            return false;
        }

        return true;
    }

}
