package com.v6.yeogaekgi.service;

import com.v6.yeogaekgi.card.repository.UserCardRepository;
import com.v6.yeogaekgi.payTrack.dto.TransactionDTO;
import com.v6.yeogaekgi.payTrack.service.TransactionService;
import com.v6.yeogaekgi.repository.CommonTest.CommonTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransactionServiceTests extends CommonTests {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserCardRepository userCardRepository;

    @Test
    void refundService(){

        TransactionDTO built = TransactionDTO.builder()
                .currencyType(0)
                .userCardNo(400L)
                .build();

        boolean b = transactionService.refundTransaction(built, member);
        System.out.println(b);

    }
}
