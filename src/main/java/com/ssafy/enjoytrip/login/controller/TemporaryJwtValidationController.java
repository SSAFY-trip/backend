package com.ssafy.enjoytrip.login.controller;

import com.ssafy.enjoytrip.login.service.TemporaryJwtValidationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class TemporaryJwtValidationController {
    private final TemporaryJwtValidationService temporaryJwtValidationService;

    @PostMapping("/verify-temporary")
    public ResponseEntity<?> verifyTemporaryToken(@RequestBody Map<String, String> payload, HttpServletResponse response) {
        String temporaryToken = payload.get("temporaryToken");
        if (temporaryToken == null || temporaryToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Temporary token is missing");
        }

        try {
            temporaryJwtValidationService.verifyTemporaryTokenAndIssueTokens(temporaryToken, response);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
