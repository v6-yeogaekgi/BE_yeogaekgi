package com.v6.yeogaekgi.service;

import com.v6.yeogaekgi.payTrack.dto.PayTrackDTO;
import com.v6.yeogaekgi.payTrack.service.PayTrackService;
import com.v6.yeogaekgi.repository.CommonTest.CommonTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PayTrackServiceTests extends CommonTests {

    @Autowired
    private PayTrackService payTrackService;

    @Test
    void selectHistories(){
        long uno = 17L;

        List<PayTrackDTO> listByUserCardNo = payTrackService.findPayTrackByUserCardNo(member.getNo() ,uno, 2024, 8);

        for (PayTrackDTO payTrackDTO : listByUserCardNo) {
            System.out.println(payTrackDTO);
        }
    }

}
