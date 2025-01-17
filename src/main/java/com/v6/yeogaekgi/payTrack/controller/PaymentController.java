package com.v6.yeogaekgi.payTrack.controller;

import com.v6.yeogaekgi.payTrack.dto.PaymentDTO;
import com.v6.yeogaekgi.payTrack.service.PaymentService;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallet/detail")
@CrossOrigin(origins = {"*"})
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/{payNo}")
    public ResponseEntity<PaymentDTO> getPaymentDetail(@PathVariable Long payNo, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        PaymentDTO paymentDTO = paymentService.findByNo(payNo, memberDetails.getMember().getNo());
        return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
    }

    @GetMapping("/getPaymentsWithoutReviews")
    public ResponseEntity<List<PaymentDTO>> getPaymentsWithoutReviews(@AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<PaymentDTO> paymentDTOS = paymentService.findPaymentsWithoutReviews(memberDetails.getMember().getNo());
        return new ResponseEntity<>(paymentDTOS, HttpStatus.OK);
    }
}
