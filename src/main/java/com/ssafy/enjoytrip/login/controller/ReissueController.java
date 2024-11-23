package com.ssafy.enjoytrip.login.controller;

import com.ssafy.enjoytrip.login.exception.RefreshTokenExpiredException;
import com.ssafy.enjoytrip.login.exception.RefreshTokenInvalidException;
import com.ssafy.enjoytrip.login.exception.RefreshTokenNotExistException;
import com.ssafy.enjoytrip.login.service.ReissueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {
    private final ReissueService reissueService;
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        try {
            reissueService.reissueAccessToken(request, response);
            return ResponseEntity.ok().build();
        } catch (RefreshTokenNotExistException e) {
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Refresh token not provided");
        } catch (RefreshTokenExpiredException e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Refresh token expired");
        } catch (RefreshTokenInvalidException e) {
            return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).body("Invalid refresh token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body("An unknown error occurred");
        }
    }

}
