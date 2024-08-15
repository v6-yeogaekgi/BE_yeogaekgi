package com.v6.yeogaekgi.member.service;

import com.v6.yeogaekgi.member.dto.MemberRequestDto;
import com.v6.yeogaekgi.member.entity.Country;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.member.repository.CountryRepository;
import com.v6.yeogaekgi.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CountryRepository countryRepository;

    @Transactional
    public Member signUp(MemberRequestDto memberRequestDto){
        if (!memberRequestDto.getPassword().equals(memberRequestDto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호를 다시 확인해주세요");
        }

        Country country = countryRepository.findByCode(memberRequestDto.getCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 국가코드입니다"));

        Member member = new Member(
                memberRequestDto.getEmail(),
                memberRequestDto.getPassword(),
                memberRequestDto.getName(),
                memberRequestDto.getNickname(),
                memberRequestDto.getGender(),
                memberRequestDto.getPhoneNumber(),
                memberRequestDto.getPassport(),
                country
        );

        return memberRepository.save(member);
    }
}
