package com.v6.yeogaekgi.member.controller;

import com.v6.yeogaekgi.member.dto.MemberRequestDTO;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class MemberController {
    private final MemberService memberService;

    @PostMapping("/members/signup")
    public ResponseEntity<?>signUp(@RequestBody MemberRequestDTO memberReqeustDto) {
        try{
            Member member = memberService.signUp(memberReqeustDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(member);

        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
