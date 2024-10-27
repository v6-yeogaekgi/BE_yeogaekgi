package com.v6.yeogaekgi.service;

import com.v6.yeogaekgi.card.dto.UserCardDTO;
import com.v6.yeogaekgi.card.service.UserCardService;
import com.v6.yeogaekgi.repository.CommonTest.CommonTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserCardServiceTests extends CommonTests {
    @Autowired
    private UserCardService userCardService;

    @Test
    public void testGetUserCard() {
        long userCardNo = 399;
        UserCardDTO userCardByCardId = userCardService.getUserCardByUserCardNo(userCardNo, member.getNo());
        System.out.println(userCardByCardId);

        assertNotNull(userCardByCardId);
    }

    @Test
    public void testModifyStarred(){
        long userCardNo = 400;
        long member_no = 1396;

        UserCardDTO cardDTO = UserCardDTO.builder()
                .userCardNo(userCardNo)
                .memberNo(member_no)
                .build();
        userCardService.changesUserCardStarred(cardDTO, member_no);
    }

    @Test
    public void testGetHomeCard() {
        long memberId = 1396;
        String area = "서울";
        List<UserCardDTO> result = userCardService.getHomeCardByMemberAndArea(memberId, area);
        System.out.println(result);
    }
}
