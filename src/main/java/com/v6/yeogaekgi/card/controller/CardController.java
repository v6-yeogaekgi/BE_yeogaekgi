package com.v6.yeogaekgi.card.controller;

import com.v6.yeogaekgi.card.dto.CardDTO;
import com.v6.yeogaekgi.card.entity.Card;
import com.v6.yeogaekgi.card.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/card")
@Log4j2
@RequiredArgsConstructor

public class CardController {
    private final CardService cardService;

//    @GetMapping("/{userId}")
//    public ResponseEntity<List<CardDTO>> list(@PathVariable("userId") Long userId) {
//        log.info("=================Card Controller list 실행================");
//        log.info("userId = " + userId);
//        return new ResponseEntity<>(cardService.getCardListByUserId(userId), HttpStatus.OK);
//    }
}
