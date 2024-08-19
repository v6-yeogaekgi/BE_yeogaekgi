package com.v6.yeogaekgi.security;

import com.v6.yeogaekgi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsServiceImpl{
    private final MemberRepository memberRepository;

}
