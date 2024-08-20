package com.v6.yeogaekgi.payTrack.controller;

import com.v6.yeogaekgi.payTrack.dto.TransactionDTO;
import com.v6.yeogaekgi.payTrack.service.TransactionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transaction")
@Log4j2
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/{tranId}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable("tranId") Long tranId) {
        log.info("tranId: " + tranId);
        return new ResponseEntity<>(transactionService.getTransactionById(tranId), HttpStatus.OK);
    }
}
