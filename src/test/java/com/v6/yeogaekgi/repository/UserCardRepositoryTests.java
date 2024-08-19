package com.v6.yeogaekgi.repository;

import com.v6.yeogaekgi.card.entity.Card;
import com.v6.yeogaekgi.card.entity.UserCard;
import com.v6.yeogaekgi.card.repository.UserCardRepository;
import com.v6.yeogaekgi.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.stream.LongStream;

@SpringBootTest
public class UserCardRepositoryTests {

    @Autowired
    UserCardRepository userCardRepository;

    @Test
    void insertCard() {
        LongStream.rangeClosed(1, 100).forEach(i -> {
            UserCard userCard = UserCard.builder()
                    .expDate(
                            new Timestamp(System.currentTimeMillis() + 77L * 24 * 60 * 60 * 1000)
                    )
                    .payBalance(10000)
                    .starred(i%2==0?0:1)
                    .status(i%3==0?0:(i%3==1?1:2))
                    .transitBalance(10000)
                    .card(Card.builder().id(i).build())
                    .member(Member.builder().id(i).build())
                    .build();

            userCardRepository.save(userCard);
        });

    }
}
