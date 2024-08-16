package com.v6.yeogaekgi.review.service;

import com.v6.yeogaekgi.review.entity.Review;
import com.v6.yeogaekgi.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public void deleteReview(Long reviewNo) {
        Review review = reviewRepository.findById(reviewNo)
                .orElseThrow(()->new IllegalArgumentException("리뷰가 존재하지 않습니다."));
        reviewRepository.delete(review);
    }

}
