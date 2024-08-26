package com.v6.yeogaekgi.payTrack.controller;

import com.v6.yeogaekgi.payTrack.dto.PayTrackDTO;
import com.v6.yeogaekgi.payTrack.dto.PayTrackRequestDTO;
import com.v6.yeogaekgi.payTrack.service.PayTrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payTrack")
@CrossOrigin(origins = {"*"})
@RequiredArgsConstructor
public class PayTrackController {

    private final PayTrackService payTrackService;

    @PostMapping("/list")
    public ResponseEntity<List<PayTrackDTO>> getPayTrackList(@RequestBody PayTrackRequestDTO requestDTO){
        List<PayTrackDTO> payTrackByUserCardNo = payTrackService.findPayTrackByUserCardNo(requestDTO.getUserCardNo(), requestDTO.getYear(), requestDTO.getMonth());
        return new ResponseEntity<>(payTrackByUserCardNo, HttpStatus.OK);
    }
}