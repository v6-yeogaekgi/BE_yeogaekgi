package com.v6.yeogaekgi.service;

import com.v6.yeogaekgi.card.service.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CardServiceTests {
    @Autowired
    private CardService cardService;

    @Test
    public void testGetCardAreas() {
        List<String> result = cardService.getCardAreas();
        System.out.println(result);
    }
}
