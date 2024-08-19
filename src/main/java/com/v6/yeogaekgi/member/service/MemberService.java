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
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CountryRepository countryRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseEntity<Void> signUp(MemberRequestDTO memberRequestDto){
        String email = memberRequestDto.getEmail();
        String password = memberRequestDto.getPassword();
        String confirm = memberRequestDto.getConfirmPassword();
        String nickname = memberRequestDto.getNickname();

        if(!password.equals(confirm)){
            throw new Error("비밀번호가 다릅니다");
        }else
            password = passwordEncoder.encode(memberRequestDto.getPassword());

        Country country = countryRepository.findByCode(memberRequestDto.getCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 국가코드입니다"));

        Optional<Member> checkEmail = memberRepository.findByEmail(email);
        if(checkEmail.isPresent()){
            throw new Error("이미 가입된 계정입니다.");
        }

        Optional<Member>checkNickname = memberRepository.findByNickname(nickname);
        if(checkNickname.isPresent()){
            throw new Error("중복된 닉네임입니다.");
        }

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

        memberRepository.save(member);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<MemberResponseDTO>login(LoginRequestDTO requestDto, HttpServletResponse response) {
        Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(()
                -> new Error("로그인 정보를 확인해주세요")
        );
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new Error("로그인 정보를 확인해주세요");
        }

        String accessToken = tokenProvider.createToken(member.getEmail());
        String refreshToken = UUID.randomUUID().toString();
        response.addHeader(TokenProvider.REFRESH_HEADER, refreshToken);
        response.addHeader(TokenProvider.AUTHORIZATION_HEADER, accessToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}