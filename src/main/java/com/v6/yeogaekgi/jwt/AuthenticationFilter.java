package com.v6.yeogaekgi.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.v6.yeogaekgi.member.dto.LoginRequestDTO;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.member.entity.RefreshToken;
import com.v6.yeogaekgi.member.repository.MemberRepository;
import com.v6.yeogaekgi.member.repository.RefreshTokenRepository;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.io.PrintWriter;

public class AuthenticationFilter {
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    public AuthenticationFilter(TokenProvider tokenProvider,RefreshTokenRepository refreshTokenRepository, MemberRepository memberRepository){
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberRepository = memberRepository;
        setFilterProcessesUrl("/members/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDTO requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
            if(member != null && !member.getIsDeleted()){
                return getAuthenticationManager().authenticate(
                        new UsernamePasswordAuthenticationToken(
                                requestDto.getEmail(),
                                requestDto.getPassword(),
                                null
                        )
                );
            }else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                writeResponse(response, "존재하지 않는 회원입니다.");
                return null;
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult){
        String username = ((MemberDetailsImpl) authResult.getPrincipal()).getUsername();

        String token = tokenProvider.createToken(username);
        String refresh = tokenProvider.createRefreshToken(username);


        RefreshToken refreshToken = refreshTokenRepository.findByUsername(email).orElse(null);
        if (refreshToken == null) {
            refreshToken = new RefreshToken(refresh, username);
        } else {
            refreshToken.updateToken(refresh);
        }
        refreshTokenRepository.save(refreshToken);
        response.addHeader(TokenProvider.AUTHORIZATION_HEADER, token);
        response.addHeader(TokenProvider.REFRESH_HEADER, refreshToken.getToken());
        response.setContentType("application/json; charset=UTF-8");

        response.setStatus(HttpServletResponse.SC_OK);
        writeResponse(response, "로그인 성공");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        writeResponse(response, "아이디 또는 비밀번호가 틀렸습니다.");
    }

    private void writeResponse(HttpServletResponse response, String message) {
        try {
            response.setContentType("text/plain;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
        }
    }
}