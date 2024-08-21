package com.v6.yeogaekgi.review.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.v6.yeogaekgi.review.dto.ReviewRequestDTO;
import com.v6.yeogaekgi.review.dto.SliceResponse;
import com.v6.yeogaekgi.review.service.ReviewService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@Log4j2
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/register")
    public ResponseEntity<Long> registerPost(@RequestBody ReviewRequestDTO reviewRequestDTO){
        log.info("----------------register Review-------------------");
        log.info("ReviewDTO : "+reviewRequestDTO);
        Long reviewId = reviewService.register(reviewRequestDTO);
        return new ResponseEntity<>(reviewId, HttpStatus.OK);
    }

    //이미지만 있는 리스트들 (맵에 이미지들만 있는 부분)
    @GetMapping("/{seviceId}/Imagelist")
    public ResponseEntity<List<String>> list (@PathVariable("serviceId") Long serviceId){
        log.info("-------------list review---------------");
         return new ResponseEntity<>(reviewService.ImageList(serviceId),HttpStatus.OK);
    }

//     리뷰 무한스크롤 구현을 위한(리뷰 리스트들)
    @GetMapping("/api/reviewList")
    public SliceResponse reviewList(
            @RequestParam Long serviceId,
            Pageable pageable,
            @RequestParam(required = false) int payStatus
    ) {
        SliceResponse reviews = reviewService.reviewList(serviceId, pageable, payStatus);
        return reviews;
    }

    @DeleteMapping("/review/{reviewNo}")
    public ResponseEntity<?>deleteReview(@PathVariable Long reviewNo){
        reviewService.deleteReview(reviewNo);
        return ResponseEntity.ok().body("리뷰가 삭제되었습니다");
    }
}
