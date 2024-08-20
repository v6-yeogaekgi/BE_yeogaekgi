package com.v6.yeogaekgi.payTrack.controller;

import com.v6.yeogaekgi.payTrack.dto.PaymentDTO;
import com.v6.yeogaekgi.payTrack.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/wallet/card-detail")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/getPayDetail/{id}")
    public ResponseEntity<PaymentDTO> getHistoryById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(paymentService.findById(id) , HttpStatus.OK);
    }
//    @PostMapping("/getPayDetail")
//    public ResponseEntity<PaymentDTO> getHistoryById(@RequestBody Map<String, Long> request) {
//        return new ResponseEntity<>(paymentService.findById(request.get("PayNo")) , HttpStatus.OK);
//    }


}
