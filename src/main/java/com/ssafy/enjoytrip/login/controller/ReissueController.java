package com.ssafy.enjoytrip.login.controller;

import com.ssafy.enjoytrip.login.service.ReissueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class ReissueController {
    private final ReissueService reissueService;
    @PostMapping("/reissue")
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        try {
            reissueService.reissueAccessToken(request, response);
        } catch (Exception e) {
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Internal Server Error");
            } catch (Exception ignored) {
            }
        }
    }
}
