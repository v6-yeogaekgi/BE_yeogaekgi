package com.v6.yeogaekgi.payTrack.controller;

import com.v6.yeogaekgi.payTrack.dto.PayDetailRequestDTO;
import com.v6.yeogaekgi.payTrack.dto.PaymentDTO;
import com.v6.yeogaekgi.payTrack.entity.Payment;
import com.v6.yeogaekgi.payTrack.service.PaymentService;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wallet/detail")
@CrossOrigin(origins = {"*"})
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/{payNo}")
    public ResponseEntity<PaymentDTO> getPaymentDetail(@PathVariable Long payNo, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        try {
            PaymentDTO paymentDTO = paymentService.findById(payNo, memberDetails.getMember().getId());
            return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getPaymentsWithoutReviews")
    public ResponseEntity<List<PaymentDTO>> getPaymentsWithoutReviews(@AuthenticationPrincipal MemberDetailsImpl memberDetails){
            List<PaymentDTO> paymentDTOS = paymentService.findPaymentsWithoutReviews(memberDetails.getMember().getId());
            return new ResponseEntity<>(paymentDTOS, HttpStatus.OK);
    }
}
