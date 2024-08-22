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
@RequiredArgsConstructor
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/{tranId}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable("tranId") Long tranId) {
        log.info("tranId: " + tranId);
        return new ResponseEntity<>(transactionService.getTransactionById(tranId), HttpStatus.OK);
    }

    @PostMapping("/refund")
    public ResponseEntity<String> refund(@RequestBody TransactionDTO transactionDTO, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        boolean result = transactionService.refundTransaction(transactionDTO, memberDetails.getMember());

        if(!result){
            return new ResponseEntity<>("false", HttpStatus.OK);
        }

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/toptup")
    public ResponseEntity<String> topup(@RequestBody TransactionDTO transactionDTO, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        boolean result = transactionService.topupTransaction(transactionDTO, memberDetails.getMember());
        if(result) {
            return new ResponseEntity<>("success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("false", HttpStatus.OK);
        }
    }
}
