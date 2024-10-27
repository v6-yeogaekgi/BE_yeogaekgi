package com.v6.yeogaekgi.payTrack.controller;

import com.v6.yeogaekgi.payTrack.dto.TransactionDTO;
import com.v6.yeogaekgi.payTrack.service.TransactionService;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@Log4j2
@CrossOrigin(origins = {"*"})
@RequiredArgsConstructor

public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/{tranNo}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable("tranNo") Long tranNo) {
        return new ResponseEntity<>(transactionService.getTransactionByNo(tranNo), HttpStatus.OK);
    }

    @PostMapping("/refund")
    public ResponseEntity<?> refund(@RequestBody TransactionDTO transactionDTO, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        transactionService.refundTransaction(transactionDTO, memberDetails.getMember());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/toptup")
    public ResponseEntity<String> topup(@RequestBody TransactionDTO transactionDTO, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        transactionService.topupTransaction(transactionDTO, memberDetails.getMember());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/conversion")
    public ResponseEntity<String> conversion(@RequestBody TransactionDTO transactionDTO, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        transactionService.conversionAmount(transactionDTO, memberDetails.getMember());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
