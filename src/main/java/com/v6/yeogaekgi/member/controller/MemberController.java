package com.v6.yeogaekgi.member.controller;

import com.v6.yeogaekgi.member.dto.LoginRequestDTO;
import com.v6.yeogaekgi.member.dto.MemberRequestDTO;

import com.v6.yeogaekgi.member.dto.MemberResponseDTO;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/members/signup")
    public ResponseEntity<?>signUp(@RequestBody MemberRequestDTO memberReqeustDto) {
        return memberService.signUp(memberReqeustDto);
    }

    @PostMapping("/members/login")
    public ResponseEntity<MemberResponseDTO>login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response){
        return memberService.login(loginRequestDTO,response);
    }


}
