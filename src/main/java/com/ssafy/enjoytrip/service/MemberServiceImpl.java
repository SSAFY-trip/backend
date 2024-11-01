package com.ssafy.enjoytrip.service;

import com.ssafy.enjoytrip.domain.Member;
import com.ssafy.enjoytrip.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public void signUp(Member member) {
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.addPassword(encodedPassword);
        memberRepository.save(member);
    }

    @Override
    public boolean signIn(String username, String rawPassword) {
        Member member = memberRepository.findByUsername(username);

        if (member != null && passwordEncoder.matches(rawPassword, member.getPassword())) {
            return true;
        }
        return false;
    }
}