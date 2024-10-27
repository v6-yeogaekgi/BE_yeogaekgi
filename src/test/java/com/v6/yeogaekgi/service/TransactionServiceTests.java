package com.v6.yeogaekgi.service;

import com.v6.yeogaekgi.card.repository.UserCardRepository;
import com.v6.yeogaekgi.payTrack.dto.TransactionDTO;
import com.v6.yeogaekgi.payTrack.service.TransactionService;
import com.v6.yeogaekgi.repository.CommonTest.CommonTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

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

        transactionService.refundTransaction(built, member);
    }

    @Test
    void topUpService(){

        TransactionDTO built = TransactionDTO.builder()
                .krwAmount(BigDecimal.valueOf(10000.00))
                .currencyType(1)
                .userCardNo(400L)
                .build();

        transactionService.topupTransaction(built, member);
    }

    @Test
    void conversionService(){

        TransactionDTO built = TransactionDTO.builder()
                .krwAmount(BigDecimal.valueOf(1000))
                .transferType((byte) 1) // 0 카드 -> 교통 | 1 교통 -> 카드
                .userCardNo(399L)
                .build();

        transactionService.conversionAmount(built, member);
    }
}
