package com.ssafy.enjoytrip.service;

import com.ssafy.enjoytrip.domain.Member;

public interface MemberService {
    void signUp(Member member);  // 회원 가입 메서드
    boolean signIn(String username, String rawPassword);  // 로그인 메서드
}