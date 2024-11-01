package com.ssafy.enjoytrip.service;

import com.ssafy.enjoytrip.domain.Member;

public interface MemberService {
    void signUp(Member member);
    boolean signIn(String username, String rawPassword);
}