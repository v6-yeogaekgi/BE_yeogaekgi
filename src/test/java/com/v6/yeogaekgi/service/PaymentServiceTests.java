package com.v6.yeogaekgi.service;

import com.v6.yeogaekgi.payTrack.dto.PaymentDTO;
import com.v6.yeogaekgi.payTrack.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

@SpringBootTest
public class PaymentServiceTests {

    @Autowired
    PaymentService paymentService;

    @Test
    void insertPaymentHistory(){

        PaymentDTO dto = PaymentDTO.builder()
                .payType(0)
                .payPrice(13000)
                .payDate(new Timestamp(System.currentTimeMillis()))
                .payBalanceSnap(0)
                .transitBalanceSnap(0)
                .status(1)
                .serviceNo(30L)
                .userCardNo(3L)
                .memberNo(4L)
                .build();
        paymentService.insertPayment(dto);

    }
}