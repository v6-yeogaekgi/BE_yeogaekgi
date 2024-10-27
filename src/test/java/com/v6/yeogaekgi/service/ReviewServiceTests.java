package com.v6.yeogaekgi.service;

import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.review.dto.ReviewDTO;
import com.v6.yeogaekgi.review.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ReviewServiceTests {
    @Autowired
    private ReviewService reviewService;

    @Test
    public void testGetUserReview() {
        Member member = Member.builder().no(1396L).build();
        List<ReviewDTO> result = reviewService.getUserReviewList(member);
        for(ReviewDTO review : result) {
            System.out.println(review);
        }
    }
}
