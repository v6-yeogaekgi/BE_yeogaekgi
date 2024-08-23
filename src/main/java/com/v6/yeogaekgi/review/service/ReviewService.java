package com.v6.yeogaekgi.review.service;

import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.review.dto.ReviewRequestDTO;
import com.v6.yeogaekgi.review.dto.ReviewResponseDTO;
import com.v6.yeogaekgi.review.dto.SliceResponse;
import com.v6.yeogaekgi.review.entity.Review;
import com.v6.yeogaekgi.review.repository.ReviewRepository;
import com.v6.yeogaekgi.services.entity.Services;
import com.v6.yeogaekgi.util.S3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final S3Service awsS3Service;

    public Review dtoToEntity(ReviewRequestDTO reviewRequestDTO,Member member) {
        String image = String.join(" ",reviewRequestDTO.getImages());
        String thumbnail = String.join(" ",reviewRequestDTO.getThumbnail());
        //url들을 프론트에서 배열로 보내줄 예정

        Review review = Review.builder()
                .id(reviewRequestDTO.getReviewId())
                .score(reviewRequestDTO.getScore())
                .images(image)
                .thumbnail(thumbnail)
                .content(reviewRequestDTO.getContent())
                .member(member)
                .services(Services.builder().id(reviewRequestDTO.getServiceId()).build())
                .build();
        return review;
    }

    public ReviewResponseDTO entityToDto(Review review) {
        List<String> images = Arrays.stream(review.getImages().split(" ")).toList();
        List<String> thumbnails = Arrays.stream(review.getThumbnail().split(" ")).toList();
        ReviewResponseDTO responseDTO = ReviewResponseDTO.builder()
                   .reviewId(review.getId())
                   .score(review.getScore())
                   .images(images)
                   .thumbnail(thumbnails)
                   .content(review.getContent())
                   .modDate(review.getModDate())
                   .memberId(review.getId())
                   .nickname(review.getMember().getNickname())
                   .serviceId(review.getServices().getId())
                   .build();
           return responseDTO;
    }

    public Long register(ReviewRequestDTO reviewRequestDTO, Member member, List<MultipartFile> multipartFile) {
        Review review = dtoToEntity(reviewRequestDTO,member);
        List<Map<String, String>> uploadImage = awsS3Service.uploadImage(multipartFile);
        StringBuilder imageBuilder = new StringBuilder();
        StringBuilder thumbnailBuilder = new StringBuilder();

        uploadImage.forEach(image ->{
            imageBuilder.append(image.get("imageUrl") + " ");
            thumbnailBuilder.append(image.get("thumbnailUrl"+ " "));
        });

        reviewRepository.save(review);
        return review.getId();
    }

    public List<String> ImageList (Long serviceId) {
        List<String> reviewList = reviewRepository.findImagesByServiceId(serviceId);
        return reviewList;
    }

    public SliceResponse<ReviewResponseDTO> reviewList(Long serviceId, Pageable pageable, Integer payStatus) {
        // ReviewRepository의 listPage 메서드를 호출하여 페이징된 리뷰 목록을 가져옴
        Slice<Review> result = reviewRepository.listPage(serviceId, pageable, payStatus);
        // Review 엔터티를 ReviewResponseDTO로 변환
        List<ReviewResponseDTO> dtoList = result.getContent().stream()
                .map(review -> entityToDto(review))
                .collect(Collectors.toList());
        // SliceResponse 객체 생성
        System.out.println("dtoList" + dtoList);
        System.out.println("result" + result);
        return new SliceResponse<>(dtoList, pageable, result.hasNext());
    }
  
    public void deleteReview(Long reviewNo) {
        Review review = reviewRepository.findById(reviewNo)
                .orElseThrow(()->new IllegalArgumentException("리뷰가 존재하지 않습니다."));
        reviewRepository.delete(review);
    }
}
