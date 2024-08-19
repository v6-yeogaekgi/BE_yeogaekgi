package com.v6.yeogaekgi.security;

import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername (String nickname)throws UsernameNotFoundException {
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(()->new UsernameNotFoundException("유저를 찾을 수 없습니다." + nickname));

        return new MemberDetailsImpl(member);
    }

}
