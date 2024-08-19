package com.v6.yeogaekgi.member.controller;

import com.v6.yeogaekgi.jwt.TokenProvider;
import com.v6.yeogaekgi.member.dto.LoginRequestDTO;
import com.v6.yeogaekgi.member.dto.MemberRequestDTO;

import com.v6.yeogaekgi.member.dto.MemberResponseDTO;
import com.v6.yeogaekgi.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/members/signup")
    public ResponseEntity<String>signUp(@RequestBody MemberRequestDTO memberReqeustDto) {
        try {
            memberService.signUp(memberReqeustDto);
            return new ResponseEntity<>("회원가입 성공", HttpStatus.CREATED);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/members/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDto, HttpServletResponse response) {
        try {
            MemberResponseDTO memberResponseDTO = memberService.login(loginRequestDto);
            String accessToken = tokenProvider.createToken(memberResponseDTO.getEmail());
            String refreshToken = tokenProvider.createRefreshToken(memberResponseDTO.getEmail());

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "로그인 성공");
            responseBody.put("accessToken", accessToken);
            responseBody.put("refreshToken", refreshToken);
            responseBody.put("user", memberResponseDTO);

            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}