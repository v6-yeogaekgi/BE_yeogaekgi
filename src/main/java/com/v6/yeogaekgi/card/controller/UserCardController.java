package com.v6.yeogaekgi.card.controller;

import com.v6.yeogaekgi.card.dto.UserCardDTO;
import com.v6.yeogaekgi.card.entity.UserCard;
import com.v6.yeogaekgi.card.service.UserCardService;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usercard")
@CrossOrigin(origins = {"*"})
@Log4j2
@RequiredArgsConstructor
public class UserCardController {
    private final UserCardService userCardService;

    @GetMapping("/list")
    public ResponseEntity<List<UserCardDTO>> list(UserCardDTO userCardDTO) { // 사용자카드 리스트 가져오기
        log.info("list userCardDTO: " + userCardDTO);
        return new ResponseEntity<>(userCardService.getUserCardByUserId(userCardDTO.getMemberId()), HttpStatus.OK);
    }

    @PostMapping("/list")
    public ResponseEntity<List<UserCardDTO>> postList(@AuthenticationPrincipal MemberDetailsImpl memberDetails) { // 사용자카드 리스트 가져오기
        return new ResponseEntity<>(userCardService.getUserCardByUserId(memberDetails.getMember().getId()), HttpStatus.OK);
    }

    //카드, 사용자카드 리스트 가져오기
    @PostMapping("/list2")
    public ResponseEntity<List<UserCardDTO>> list3(@RequestBody UserCardDTO userCardDTO) {
        log.info("list2 userCardDTO: " + userCardDTO);
        List<UserCardDTO> all = userCardService.getAll(userCardDTO);
        log.info("all : " + all);
        return new ResponseEntity<>(userCardService.getAll(userCardDTO), HttpStatus.OK);
    }
}
