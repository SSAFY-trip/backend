package com.ssafy.enjoytrip.login.controller;

import com.ssafy.enjoytrip.login.dto.JoinDTO;
import com.ssafy.enjoytrip.login.service.JoinService;
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
    public boolean joinProcess(@RequestBody JoinDTO joinDTO){
        return joinService.joinProcess(joinDTO);
    }
}
