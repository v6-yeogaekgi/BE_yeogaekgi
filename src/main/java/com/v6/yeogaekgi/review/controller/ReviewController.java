package com.v6.yeogaekgi.review.controller;


import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.review.dto.*;
import com.v6.yeogaekgi.review.entity.Review;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@Log4j2
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{servicesId}/register")
    public ResponseEntity<Long> registerPost(@RequestParam (value = "files", required = false)  List<MultipartFile> multipartFile,
                                             @ModelAttribute("reviewRequestDTO") ReviewRequestDTO reviewRequestDTO,
                                             @AuthenticationPrincipal MemberDetailsImpl memberDetails,
                                             @PathVariable Long servicesId){
        log.info("----------------register Review-------------------");
        log.info("ReviewDTO : "+reviewRequestDTO);
        Long id = reviewService.register(multipartFile,servicesId,reviewRequestDTO,memberDetails.getMember());
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    //이미지만 있는 리스트들 (맵에 이미지들만 있는 부분)
    @GetMapping("/{servicesId}/ImgList")
    public ResponseEntity<List<ReviewResponseDTO>> Imglist (@PathVariable("servicesId") Long servicesId){
        log.info("-------------list review---------------");
         return new ResponseEntity<>(reviewService.ImageList(servicesId),HttpStatus.OK);
    }

//     리뷰 무한스크롤 구현을 위한(리뷰 리스트들)
    @GetMapping("/{servicesId}/reviewList")
    public ResponseEntity<SliceResponse<ReviewResponseDTO>> reviewList(
            @PathVariable Long servicesId,
            Pageable pageable,
            @RequestParam(required = false) Integer payStatus
    ) {
        return new ResponseEntity<>(reviewService.reviewList(servicesId, pageable, payStatus),HttpStatus.OK);
    }

    @PostMapping("/list")
    public ResponseEntity<List<ReviewResponseDTO>> getUserReviews(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        Member member = memberDetails.getMember();
        return new ResponseEntity<>(reviewService.getUserReviewList(member), HttpStatus.OK);
    }

    @GetMapping("/{servicesId}/{reviewId}/detail")
    public ResponseEntity<ReviewResponseDTO> getReviewDetail(
            @PathVariable Long servicesId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        log.info("-------------detail review---------------");
        return new ResponseEntity<>(reviewService.Detail(servicesId, reviewId, memberDetails.getMember()), HttpStatus.OK);
    }


    @PutMapping("/{servicesId}/{reviewId}")
    public ResponseEntity<ReviewUpdateResponseDTO>updateReview(@RequestPart (value = "images", required = false) List<MultipartFile> images,
                                                               @RequestParam (required = false) List<Integer> chooseImages,
                                                               @RequestParam (required = false) Integer score,
                                                               @RequestParam (required = false) String content,
                                                               @PathVariable Long servicesId,
                                                               @PathVariable Long reviewId,
                                                               @AuthenticationPrincipal MemberDetailsImpl MemberDetails){
        ReviewUpdateDTO reviewUpdateDTO = new ReviewUpdateDTO(chooseImages, score, content);
        ReviewUpdateResponseDTO reviewUpdateResponseDTO = reviewService.updateReview(images, servicesId,reviewId,reviewUpdateDTO,MemberDetails.getMember());
        return ResponseEntity.ok().body(reviewUpdateResponseDTO);
    }

    @DeleteMapping("/{servicesId}/{reviewId}")
    public ResponseEntity<?>deleteReview(@PathVariable  Long servicesId, @PathVariable Long reviewId, @AuthenticationPrincipal MemberDetailsImpl MemberDetails){
        reviewService.deleteReview(servicesId,reviewId, MemberDetails.getMember());
        return ResponseEntity.ok().body("리뷰가 삭제되었습니다");
    }
}
