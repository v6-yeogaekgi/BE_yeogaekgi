package com.v6.yeogaekgi.repository;

import com.v6.yeogaekgi.card.entity.UserCard;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.payTrack.entity.Payment;
import com.v6.yeogaekgi.payTrack.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.stream.LongStream;

@SpringBootTest
public class PaymentRepositoryTests {
    @Autowired
    PaymentRepository paymentRepository;

    @Test
    void insertPaymentHistories(){
        LongStream.rangeClosed(1,100).forEach(i->{
            Payment payment = Payment.builder()
                    .payType(i%2==0?0:1)
                    .payPrice(10000)
                    .payDate(new Timestamp(System.currentTimeMillis()))
                    .payBalanceSnap(5000)
                    .transitBalanceSnap(2000)
                    .status(i%2==0?0:1)
                    .serviceName(i%2==0?"가게":"교통공사")
                    .userCard(UserCard.builder().
                            id(i).
                            build())
                    .member(Member.builder().
                            id(i).
                            build())
                    .build();
            paymentRepository.save(payment);
        });
    }

}
