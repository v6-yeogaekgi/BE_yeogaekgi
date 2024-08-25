package com.v6.yeogaekgi.service;

import com.v6.yeogaekgi.card.dto.UserCardDTO;
import com.v6.yeogaekgi.card.service.UserCardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserCardServiceTests {
    @Autowired
    private UserCardService userCardService;

    @Test
    public void testGetUserCard() {
        long userCardNo = 399;
        UserCardDTO userCardByCardId = userCardService.getUserCardByCardId(userCardNo);
        System.out.println(userCardByCardId);

        assertNotNull(userCardByCardId);
    }
}
