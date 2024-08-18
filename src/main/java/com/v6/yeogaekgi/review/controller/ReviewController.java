package com.v6.yeogaekgi.review.controller;


import com.v6.yeogaekgi.review.dto.ReviewRequestDTO;
import com.v6.yeogaekgi.review.entity.Review;
import com.v6.yeogaekgi.review.service.ReviewService;
import com.v6.yeogaekgi.services.entity.Services;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;

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
//    @GetMapping("/api/reviews")
//    public ResponseEntity<> getReviews(
//            @RequestParam Long serviceId,
//            Pageable pageable,
//            @RequestParam(required = false) int payStatus
//    ) {
//        Slice<Review> reviews =  reviewService.reviewList(serviceId, pageable, payStatus);
//        return ResponseEntity.ok(reviews);
//    }




}
