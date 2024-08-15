package com.v6.yeogaekgi.Member.controller;

import com.v6.yeogaekgi.Member.dto.MemberRequestDto;
import com.v6.yeogaekgi.Member.entity.Member;
import com.v6.yeogaekgi.Member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class MemberController {
    private final MemberService memberService;

    @PostMapping("/members/signup")
    public ResponseEntity<?>signUp(@RequestBody MemberRequestDto memberReqeustDto) {
        try{
            Member member = memberService.signUp(memberReqeustDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(member);

        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
