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
            UserCard userCard = UserCard.builder()
                    .expDate(
                            new Timestamp(System.currentTimeMillis() + 77L * 24 * 60 * 60 * 1000)
                    )
                    .payBalance(0)
                    .starred(1)
                    .status(1)
                    .transitBalance(0)
                    .card(Card.builder().no(1L).build())
                    .member(Member.builder().no(5L).build())
                    .build();
            userCardRepository.save(userCard);
    }
}
