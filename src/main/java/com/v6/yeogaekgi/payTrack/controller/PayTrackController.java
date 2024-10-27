package com.v6.yeogaekgi.payTrack.controller;

import com.v6.yeogaekgi.payTrack.dto.PayTrackDTO;
import com.v6.yeogaekgi.payTrack.service.PayTrackService;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payTrack")
@CrossOrigin(origins = {"*"})
@RequiredArgsConstructor
public class PayTrackController {

    private final PayTrackService payTrackService;

    @GetMapping("/list")
    public ResponseEntity<List<PayTrackDTO>> getPayTrackList(
            @RequestParam Long userCardNo,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
            List<PayTrackDTO> payTracks = payTrackService.findPayTrackByUserCardNo(
                    memberDetails.getMember().getNo(), userCardNo, year, month);
            return ResponseEntity.ok(payTracks);
    }
}