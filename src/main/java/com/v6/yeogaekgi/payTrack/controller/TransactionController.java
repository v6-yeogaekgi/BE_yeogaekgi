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
        try {
            return new ResponseEntity<>(transactionService.getTransactionByNo(tranNo), HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/refund")
    public ResponseEntity<?> refund(@RequestBody TransactionDTO transactionDTO, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        try {
            boolean result = transactionService.refundTransaction(transactionDTO, memberDetails.getMember());
            if (result) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/toptup")
    public ResponseEntity<String> topup(@RequestBody TransactionDTO transactionDTO, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        try {
            boolean result = transactionService.topupTransaction(transactionDTO, memberDetails.getMember());
            if (result) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/conversion")
    public ResponseEntity<String> conversion(@RequestBody TransactionDTO transactionDTO, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        try {
            boolean result = transactionService.conversionAmount(transactionDTO, memberDetails.getMember());
            if (result) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
