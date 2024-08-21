package com.v6.yeogaekgi.payTrack.controller;

import com.v6.yeogaekgi.payTrack.dto.PayDetailRequestDTO;
import com.v6.yeogaekgi.payTrack.dto.PaymentDTO;
import com.v6.yeogaekgi.payTrack.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/wallet/detail")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/getPayDetail")
    public ResponseEntity<PaymentDTO> getHistoryById(@RequestBody PayDetailRequestDTO requestDTO) {
        return new ResponseEntity<>(paymentService.findById(requestDTO.getPayNo()) , HttpStatus.OK);
    }


}
