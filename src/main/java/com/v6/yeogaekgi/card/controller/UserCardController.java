package com.v6.yeogaekgi.card.controller;

import com.v6.yeogaekgi.card.dto.UserCardDTO;
import com.v6.yeogaekgi.card.service.CardService;
import com.v6.yeogaekgi.card.service.UserCardService;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usercard")
@CrossOrigin(origins = {"*"})
@Log4j2
@RequiredArgsConstructor
public class UserCardController {
    private final UserCardService userCardService;
    private final CardService cardService;

    @GetMapping("/list")
    public ResponseEntity<List<UserCardDTO>> list(@AuthenticationPrincipal MemberDetailsImpl memberDetails) { // 사용자카드 리스트 가져오기
        log.info("get mapping user card list");
        return ResponseEntity.ok(userCardService.getUserCardByUserNo(memberDetails.getMember().getNo()));
    }

    @GetMapping("/{userCardId}")
    public ResponseEntity<UserCardDTO> getDetail(@PathVariable Long userCardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails, HttpServletRequest request) { // 카드번호로 카드 상세 조회
        return ResponseEntity.ok(userCardService.getUserCardByUserCardNo(userCardId, memberDetails.getMember().getNo()));
    }

    @PutMapping("/modify/star")
    public ResponseEntity<String> modifyStar(@RequestBody UserCardDTO userCardDTO, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        boolean result = userCardService.changeUserCardStarred(userCardDTO, memberDetails.getMember().getNo());
        return result ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping("/delete/star")
    public ResponseEntity<String> deleteStar(@RequestBody UserCardDTO userCardDTO, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        boolean result = userCardService.deleteUserCardStarred(userCardDTO, memberDetails.getMember().getNo());
        return result ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping("/delete/card")
    public ResponseEntity<String> deleteCard(@RequestBody UserCardDTO userCardDTO, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        boolean result = userCardService.deleteUserCard(userCardDTO, memberDetails.getMember().getNo());
        return result ? ResponseEntity.ok("success") : ResponseEntity.badRequest().build();
    }

    @GetMapping("/area")
    public ResponseEntity<?> getCardAreas(@AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return ResponseEntity.ok(cardService.getCardAreas());
    }

    @PostMapping("/area")
    public ResponseEntity<?> getHomeCards(@RequestBody String area, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return ResponseEntity.ok(userCardService.getHomeCardByMemberAndArea(memberDetails.getMember().getNo(), area));
    }
}
