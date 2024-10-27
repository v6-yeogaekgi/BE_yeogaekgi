package com.v6.yeogaekgi.repository;

import com.v6.yeogaekgi.card.entity.UserCard;
import com.v6.yeogaekgi.card.repository.UserCardRepository;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.payTrack.entity.Transaction;
import com.v6.yeogaekgi.payTrack.repository.TransactionRepository;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class TransactionRepositoryTests {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserCardRepository userCardRepository;

    @Test
    public void insertTransaction() {
        IntStream.rangeClosed(1, 30).forEach(i -> {
            Long memberId = (long) (Math.random() * 100) + 1;
            Long userCardId = (long) (Math.random() * 100) + 1;

            Transaction transaction = Transaction.builder()
                    .tranType(1)
                    .tranDate(new Timestamp(System.currentTimeMillis()))
                    .payBalanceSnap(20000)
                    .transitBalanceSnap(10000)
                    .krwAmount(new BigDecimal("10000.00"))
                    .foreignAmount(new BigDecimal("1099.95"))
                    .currencyType(1)
                    .userCard(UserCard.builder().no(userCardId).build())
                    .member(Member.builder().no(memberId).build())
                    .build();

            transactionRepository.save(transaction);

            Optional<UserCard> userCard = userCardRepository.findById(userCardId);
            if(userCard.isPresent()) {
                UserCard card = userCard.get();
                card.updatePayBalance(20000);
                userCardRepository.save(card);
            }

        });
    }
}
