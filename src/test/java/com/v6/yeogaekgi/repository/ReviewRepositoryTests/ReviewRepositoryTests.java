package com.v6.yeogaekgi.repository.ReviewRepositoryTests;

import com.v6.yeogaekgi.card.entity.UserCard;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.payTrack.entity.Payment;
import com.v6.yeogaekgi.review.entity.Review;
import com.v6.yeogaekgi.review.repository.ReviewRepository;
import com.v6.yeogaekgi.services.entity.Services;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.LongStream;

@SpringBootTest
public class ReviewRepositoryTests {
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void insertPaymentHistories(){
        LongStream.rangeClosed(3,200).forEach(i->{
            Review review = Review.builder()
                    .content("test입니다" + i)
                    .images("test입니다" +i +" test이미지입니다" + " test이미지입니다")
                    .score(4)
                    .status(0)
                    .member(Member.builder().id(i-2).build())
                    .services(Services.builder().id(i%2==0?1L:2L).build())
                    .thumbnails("s-test입니다" +i +" s-test이미지입니다" + " s-test이미지입니다")
                    .payment(Payment.builder().id(i).build())
                    .build();
            reviewRepository.save(review);
        });
    }

    @Test
    void getUserReview() {
        List<Review> result = reviewRepository.findByMemberId(1000L);
        System.out.println(result);
    }
}

