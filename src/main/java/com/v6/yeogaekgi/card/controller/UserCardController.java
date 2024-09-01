package com.v6.yeogaekgi.card.controller;

import com.v6.yeogaekgi.card.dto.UserCardDTO;
import com.v6.yeogaekgi.card.service.CardService;
import com.v6.yeogaekgi.card.service.UserCardService;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
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
        return new ResponseEntity<>(userCardService.getUserCardByUserId(memberDetails.getMember().getId()), HttpStatus.OK);
    }

    @GetMapping("/{userCardId}")
    public ResponseEntity<UserCardDTO> getDetail(@PathVariable Long userCardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) { // 카드번호로 카드 상세 조회
        return new ResponseEntity<>(userCardService.getUserCardByUserCardId(userCardId), HttpStatus.OK);
    }

    @PutMapping("/modify/star")
    public ResponseEntity<String> modifyStar(@RequestBody UserCardDTO userCardDTO, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        // 프론트에서 받을 정보 userCardNo
        // security 에서 받을 정보 -> member
        // Long id = memberDetails.getMember().getId();

        boolean result = userCardService.changesUserCardStarred(userCardDTO);
        if(result){
            return new ResponseEntity<>("success", HttpStatus.OK);
        }
        return new ResponseEntity<>("false", HttpStatus.OK);
    }

    @PutMapping("/delete/star")
    public ResponseEntity<String> deleteStar(@RequestBody UserCardDTO userCardDTO, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        // 프론트에서 받을 정보 userCardNo
        // security 에서 받을 정보 -> member
        // Long id = memberDetails.getMember().getId();
        boolean result = userCardService.deleteUserCardStarred(userCardDTO);
        if(result){
            return new ResponseEntity<>("success", HttpStatus.OK);
        }
        return new ResponseEntity<>("false", HttpStatus.OK);
    }

    @PutMapping("/delete/card")
    public ResponseEntity<String> deleteCard(@RequestBody UserCardDTO userCardDTO, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        boolean result = userCardService.deleteUserCard(userCardDTO);
        if(result){
            return new ResponseEntity<>("success", HttpStatus.OK);
        }
        return new ResponseEntity<>("false", HttpStatus.OK);
    }

    @GetMapping("/area")
    public ResponseEntity<?> getCardAreas(@AuthenticationPrincipal MemberDetailsImpl memberDetails){
        log.info("-----------getCardAreas-----------");
        return new ResponseEntity<>(cardService.getCardAreas(), HttpStatus.OK);
    }

    @PostMapping("/area")
    public ResponseEntity<?> getHomeCards(@RequestBody String area, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        log.info("-----------getHomeCards-----------");
        log.info("-----------print area: " + area);
        List<UserCardDTO> result = userCardService.getHomeCardByMemberAndArea(memberDetails.getMember().getId(), area);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
