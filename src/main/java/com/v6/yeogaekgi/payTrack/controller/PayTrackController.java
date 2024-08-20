package com.v6.yeogaekgi.payTrack.controller;

import com.v6.yeogaekgi.payTrack.dto.PayTrackDTO;
import com.v6.yeogaekgi.payTrack.service.PayTrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wallet/card-detail")
@RequiredArgsConstructor
public class PayTrackController {

    private final PayTrackService payTrackService;

    @PostMapping("/getPayTrack")
    public ResponseEntity<List<PayTrackDTO>> getPayTrackList(@RequestBody Map<String, String> request){
        long userCardNo = Long.parseLong(request.get("userCardNo"));
        int year = Integer.parseInt(request.get("year"));
        int month = Integer.parseInt(request.get("month"));
        List<PayTrackDTO> payTrackByUserCardNo = payTrackService.findPayTrackByUserCardNo(userCardNo, year, month);
        return new ResponseEntity<>(payTrackByUserCardNo, HttpStatus.OK);
    }
}