package com.v6.yeogaekgi.review.controller;

import com.v6.yeogaekgi.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @DeleteMapping("/review/{reviewNo}")
    public ResponseEntity<?>deleteReview(@PathVariable Long reviewNo){
        reviewService.deleteReview(reviewNo);
        return ResponseEntity.ok().body("리뷰가 삭제되었습니다");
    }

}
