package com.v6.yeogaekgi.review.service;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.review.dto.*;
import com.v6.yeogaekgi.review.entity.Review;
import com.v6.yeogaekgi.review.repository.ReviewRepository;
import com.v6.yeogaekgi.services.entity.Services;
import com.v6.yeogaekgi.util.S3.S3Service;
import jakarta.transaction.Transactional;
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
    private final S3Service s3Service;


    public Review dtoToEntity(ReviewRequestDTO reviewRequestDTO,Member member,Long servicesId) {
        //url들을 프론트에서 배열로 보내줄 예정
        Review review = Review.builder()
                .score(reviewRequestDTO.getScore())
                .content(reviewRequestDTO.getContent())
                .member(member)
                .services(Services.builder().id(servicesId).build())
                .build();
        return review;
    }

    public ReviewResponseDTO entityToDto(Review review) {
        List<String> images = review.getImages();
        List<String> thumbnails = review.getThumbnails();
        ReviewResponseDTO responseDTO = ReviewResponseDTO.builder()
                   .reviewId(review.getId())
                   .score(review.getScore())
                   .images(images)
                   .thumbnail(thumbnails)
                   .content(review.getContent())
                   .regDate(review.getRegDate())
                   .modDate(review.getModDate())
                   .serviceId(review.getServices().getId())
                   .country(review.getMember().getCountry())
                   .nickname(review.getMember().getNickname())
                   .build();
           return responseDTO;
    }

    public Long register(List<MultipartFile> multipartFile, Long servicesId,ReviewRequestDTO reviewRequestDTO, Member member) {
        List<Map<String, String>> uploadImage = s3Service.uploadImage(multipartFile);
        List<String> imageList = new ArrayList<>();
        List<String> thumbnailList = new ArrayList<>();
        // 프론트에서 개수 확인해야함 3개 만 가능하게!
        Review review = dtoToEntity(reviewRequestDTO,member,servicesId);
        if (!uploadImage.isEmpty()) {
            uploadImage.forEach(image ->{
                imageList.add(image.get("imageUrl"));
                thumbnailList.add(image.get("thumbnailUrl"));
            });
        }
        review.setImages(imageList);
        review.setThumbnails(thumbnailList);
        Review savedReview = reviewRepository.save(review);
        return savedReview.getId();
    }

    public List<ReviewResponseDTO> ImageList (Long serviceId) {
        List<Review> reviews = reviewRepository.findAllByServicesId(serviceId);
        List<ReviewResponseDTO> result = new ArrayList<>();
        for (Review review : reviews) {
            ReviewResponseDTO dto = ReviewResponseDTO.builder()
                    .images(review.getImages())
                    .nickname(review.getMember().getNickname())
                    .country(review.getMember().getCountry())
                    .build();
            result.add(dto);
        }
        return result;
    }

    public ReviewResponseDTO Detail (Long servicesId,Long reviewId,Member member){
        Review review = reviewRepository.findByServicesIdAndIdAndMemberId(servicesId, reviewId,member.getId()).orElseThrow(
                () -> new IllegalArgumentException("리뷰를 찾을 수 없습니다.")
        );
         System.out.println("images" + review.getImages());
         System.out.println("review" + review);
         System.out.println("member" + member);
         System.out.println("reviewId" + reviewId);
         System.out.println("serviceId" + servicesId);
         return entityToDto(review);
    }

    public SliceResponse<ReviewResponseDTO> reviewList(Long servicesId, Pageable pageable, Integer payStatus) {
        // ReviewRepository의 listPage 메서드를 호출하여 페이징된 리뷰 목록을 가져옴
        Slice<Review> result = reviewRepository.listPage(servicesId, pageable, payStatus);
        // Review 엔터티를 ReviewResponseDTO로 변환
        List<ReviewResponseDTO> dtoList = result.getContent().stream()
                .map(review -> entityToDto(review))
                .collect(Collectors.toList());
        // SliceResponse 객체 생성
        System.out.println("dtoList" + dtoList);
        System.out.println("result" + result);
        return new SliceResponse<>(dtoList, pageable, result.hasNext());
    }

    @Transactional
    public ReviewUpdateResponseDTO updateReview(List<MultipartFile> images, Long servicesId, Long reviewId, ReviewUpdateDTO reviewUpdateDTO, Member member) {
        Review review = reviewRepository.findByServicesIdAndId(servicesId, reviewId).orElseThrow(
                () -> new IllegalArgumentException("리뷰를 찾을 수 없습니다.")
        );

        if (!review.getMember().getId().equals(member.getId())) {
            throw new SecurityException("리뷰를 수정할 권한이 없습니다.");
        }

        int score = reviewUpdateDTO.getScore() != null ? reviewUpdateDTO.getScore() : review.getScore();
        String content = reviewUpdateDTO.getContent() != null ? reviewUpdateDTO.getContent() : review.getContent();

        List<String> existingImages = new ArrayList<>(review.getImages());
        List<String> existingThumbnails = new ArrayList<>(review.getThumbnails());

        if (reviewUpdateDTO.getChooseImages() != null) {
            List<Integer> chooseImages = new ArrayList<>(reviewUpdateDTO.getChooseImages());
            Collections.sort(chooseImages, Collections.reverseOrder());
            for (Integer index : chooseImages) {
                if (index >= 0 && index < existingImages.size()) {
                    s3Service.deleteImage(existingImages.get(index));
                    existingImages.remove((int) index);
                    existingThumbnails.remove((int) index);
                }
            }
        }

        if ((existingImages.size() + (images != null ? images.size() : 0)) > 3) {
            throw new IllegalStateException("이미지 3개까지만 가능");
        }

        if (images != null && !images.isEmpty()) {
            List<Map<String, String>> uploadedImageDetails = s3Service.uploadImage(images);

            for (Map<String, String> imageDetails : uploadedImageDetails) {
                existingImages.add(imageDetails.get("imageUrl"));
                existingThumbnails.add(imageDetails.get("thumbnailUrl"));
            }
        }

        review.update(reviewUpdateDTO, existingImages, existingThumbnails, score, content);
        reviewRepository.save(review);

        return new ReviewUpdateResponseDTO(existingImages, existingThumbnails, review.getContent(), review.getScore());
    }

    @Transactional
    public void deleteReview(Long servicesId, Long reviewId, Member member) {
        Review review = reviewRepository.findByServicesIdAndId(servicesId,reviewId)
                .orElseThrow(()->new IllegalArgumentException("리뷰가 존재하지 않습니다."));

        if (!review.getMember().getId().equals(member.getId())) {
            throw new SecurityException("리뷰를 삭제할 권한이 없습니다.");
        }

        List<String> images = review.getImages();
        List<String> thumbnails = review.getThumbnails();
        for (String image : images) {
            s3Service.deleteImage(image);
        }
        for (String thumbnail : thumbnails) {
            s3Service.deleteImage(thumbnail);
        }
        reviewRepository.delete(review);
    }
}
