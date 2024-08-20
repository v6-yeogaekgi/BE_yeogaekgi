package com.v6.yeogaekgi.repository;

import com.v6.yeogaekgi.card.entity.Card;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class CardRepositoryTests {

    @Autowired
    com.v6.yeogaekgi.card.repository.CardRepository cardRepository;

    @Test
    void insertCard() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Card card = Card.builder()
                    .area(i%2==0?"서울":"부산")
                    .cardName("testCard"+i)
                    .design("")
                    .build();

            cardRepository.save(card);
        });

    }
}
