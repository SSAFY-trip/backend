package com.ssafy.enjoytrip.user.controller;

import com.ssafy.enjoytrip.user.dto.JoinDTO;
import com.ssafy.enjoytrip.user.service.JoinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
@RequiredArgsConstructor
public class JoinController {
    private final JoinService joinService;
    @PostMapping("/join")
    public boolean joinProcess(@RequestBody @Valid JoinDTO joinDTO){
        return joinService.joinProcess(joinDTO);
    }
}
