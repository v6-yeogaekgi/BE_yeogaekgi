package com.v6.yeogaekgi.card.controller;

import com.v6.yeogaekgi.card.dto.UserCardDTO;
import com.v6.yeogaekgi.card.entity.UserCard;
import com.v6.yeogaekgi.card.service.UserCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/usercard")
@Log4j2
@RequiredArgsConstructor
public class UserCardController {
    private final UserCardService userCardService;

    @GetMapping("/list")
    public ResponseEntity<List<UserCardDTO>> list(UserCardDTO userCardDTO) {
        log.info("list userCardDTO: " + userCardDTO);
        return new ResponseEntity<>(userCardService.getUserCardByUserId(userCardDTO.getMemberId()), HttpStatus.OK);
    }
}
