package com.v6.yeogaekgi.member.service;

import com.v6.yeogaekgi.jwt.TokenProvider;
import com.v6.yeogaekgi.member.dto.LoginRequestDTO;
import com.v6.yeogaekgi.member.dto.MemberRequestDTO;
import com.v6.yeogaekgi.member.dto.MemberResponseDTO;
import com.v6.yeogaekgi.member.entity.Country;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.member.repository.CountryRepository;
import com.v6.yeogaekgi.member.repository.MemberRepository;
import com.v6.yeogaekgi.member.repository.RefreshTokenRepository;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class MemberService {
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;
    private final CountryRepository countryRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public void signUp(MemberRequestDTO memberRequestDto){
        String email = memberRequestDto.getEmail();
        String password = memberRequestDto.getPassword();
        String confirm = memberRequestDto.getConfirmPassword();
        String nickname = memberRequestDto.getNickname();

        if(!password.equals(confirm)){
            throw new Error("비밀번호가 다릅니다");
        }

        if (memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 계정입니다.");
        }

        if (memberRepository.findByNickname(nickname).isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임입니다.");
        }
        
        Country country = countryRepository.findByCode(memberRequestDto.getCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 국가코드입니다"));

        Member member = new Member(
                email,
                passwordEncoder.encode(password),
                memberRequestDto.getName(),
                nickname,
                memberRequestDto.getGender(),
                memberRequestDto.getPhoneNumber(),
                memberRequestDto.getPassport(),
                country
        );
        memberRepository.save(member);
    }

    @Transactional
    public MemberResponseDTO login(LoginRequestDTO requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("로그인 정보를 확인해주세요"));
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("로그인 정보를 확인해주세요");
        }
        return new MemberResponseDTO(member.getEmail(), member.getName(), member.getNickname(),
                member.getGender(), member.getPhoneNumber(), member.getPassport(),
                member.getAccountNumber(), member.getBank(), member.getCountry());
    }

    @Transactional
    public boolean logout(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
            int deletedCount = refreshTokenRepository.deleteByEmail(memberDetails.getMember().getEmail());
            if(deletedCount > 0) {
                log.info("Logout success");
                return true;
            } else {
                log.info("Logout delete fail");
                return false;
            }
    }
}