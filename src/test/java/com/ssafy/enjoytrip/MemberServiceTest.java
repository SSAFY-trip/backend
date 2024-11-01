package com.ssafy.enjoytrip;

import static org.junit.jupiter.api.Assertions.*;

import com.ssafy.enjoytrip.domain.Member;
import com.ssafy.enjoytrip.repository.MemberMapper;
import com.ssafy.enjoytrip.service.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.when;

class MemberServiceTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberMapper memberRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 회원가입_성공_테스트() {
        // given
        Member member = new Member();
        member.addUsername("testuser");
        member.addPassword("testpassword");

        // when
        memberService.signUp(member);

        // then
        assertTrue(passwordEncoder.matches("testpassword", member.getPassword()));
    }

    @Test
    void 로그인_성공_테스트() {
        // given
        Member member = new Member();
        member.addUsername("testuser");
        member.addPassword(passwordEncoder.encode("testpassword"));

        // mock repository
        when(memberRepository.findByUsername("testuser")).thenReturn(member);

        // when
        boolean result = memberService.signIn("testuser", "testpassword");

        // then
        assertTrue(result);
    }

    @Test
    void 로그인_실패_테스트() {
        // given
        Member member = new Member();
        member.addUsername("testuser");
        member.addPassword(passwordEncoder.encode("wrongpassword"));

        // mock repository
        when(memberRepository.findByUsername("testuser")).thenReturn(member);

        // when
        boolean result = memberService.signIn("testuser", "testpassword");

        // then
        assertFalse(result);
    }
}