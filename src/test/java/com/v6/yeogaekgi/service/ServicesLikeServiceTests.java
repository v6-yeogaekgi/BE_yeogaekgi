package com.v6.yeogaekgi.service;

import com.v6.yeogaekgi.repository.CommonTest.CommonTests;
import com.v6.yeogaekgi.services.dto.ServicesLikeDTO;
import com.v6.yeogaekgi.services.service.ServicesLikeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ServicesLikeServiceTests extends CommonTests {
    @Autowired
    private ServicesLikeService servicesLikeService;

    @Test
    public void getLikeList(){
        List<ServicesLikeDTO> list = servicesLikeService.findAllServiceLike(1645L);
        for (ServicesLikeDTO servicesLikeDTO : list) {
            System.out.println(servicesLikeDTO);
        }
    }
}
