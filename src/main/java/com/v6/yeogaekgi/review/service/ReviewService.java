package com.v6.yeogaekgi.review.service;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.payTrack.entity.Payment;
import com.v6.yeogaekgi.payTrack.repository.PaymentRepository;
import com.v6.yeogaekgi.payTrack.service.PaymentService;
import com.v6.yeogaekgi.review.dto.*;
import com.v6.yeogaekgi.review.entity.Review;
import com.v6.yeogaekgi.review.repository.ReviewListRepository;
import com.v6.yeogaekgi.review.repository.ReviewRepository;
import com.v6.yeogaekgi.services.entity.Services;
import com.v6.yeogaekgi.services.repository.ServicesRepository;
import com.v6.yeogaekgi.util.PageDTO.PageResultDTO;
import com.v6.yeogaekgi.util.S3.S3Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;
    private final PaymentRepository paymentRepository;
    private final ServicesRepository servicesRepository;

    public Review dtoToEntity(ReviewDTO reviewDTO,Member member,Long servicesId) {
        Review review = Review.builder()
                .score(reviewDTO.getScore())
                .content(reviewDTO.getContent())
                .member(member)
                .services(Services.builder().id(servicesId).build())
                .build();
        return review;
    }


    public ReviewDTO entityToDto(Review review) {
        List<String> images = review.getImages();
        List<String> thumbnails = review.getThumbnails();

        ReviewDTO responseDTO = ReviewDTO.builder()
                   .reviewNo(review.getNo())
                   .images(images)
                   .thumbnail(thumbnails)
                   .score(review.getScore())
                   .content(review.getContent())
                   .regDate(review.getRegDate())
                   .modDate(review.getModDate())
                   .serviceNo(review.getServices().getId())
                   .country(review.getMember().getCountry())
                   .nickname(review.getMember().getNickname())
                   .build();
              if(review.getPayment()!= null){
                  responseDTO.setPaymentId(review.getPayment().getId());
              }
           return responseDTO;
    }

    @Transactional
    public Long register(List<MultipartFile> multipartFile, Long servicesNo,ReviewDTO reviewDTO, Member member) {
        String service = servicesRepository.findServiceNameById(servicesNo);
        Boolean reviewExist = null;
        Optional<Payment> payment = paymentRepository.findByMemberIdAndServiceName(member.getId(),service, reviewDTO.getPayNo());
        if(!payment.isPresent()) {
            reviewExist = reviewRepository.existsByServicesIdAndMemberId(servicesNo,member.getId());
        } else {
            reviewExist = reviewRepository.existsByPaymentId(reviewDTO.getPayNo());
        }
        if(reviewExist){
            return -1L;
        }

        Review review = dtoToEntity(reviewDTO,member,servicesNo);

        if (multipartFile != null){
            List<Map<String, String>> uploadImage = s3Service.uploadImage(multipartFile);
            List<String> imageList = new ArrayList<>();
            List<String> thumbnailList = new ArrayList<>();
            if (!uploadImage.isEmpty()) {
                uploadImage.forEach(image ->{
                    imageList.add(image.get("imageUrl"));
                    thumbnailList.add(image.get("thumbnailUrl"));
                });
                review.setImages(imageList);
                review.setThumbnails(thumbnailList);
            }
        }

        Review savedReview = reviewRepository.save(review);
        if(payment.isPresent()) {
            savedReview.setPayment(payment.get());
        }

        return savedReview.getNo();
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> ImageList (Long serviceId) {
        List<Review> reviews = reviewRepository.findImageMatchByServicesId(serviceId);
        List<ReviewDTO> result = new ArrayList<>();
        for (Review review : reviews) {
            ReviewDTO dto = entityToDto(review);
            result.add(dto);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public ReviewDTO Detail (Long servicesId,Long reviewId,Member member){
        Optional<Review> review = reviewRepository.findByServicesIdAndIdAndMemberId(servicesId, reviewId,member.getId());
        return review.map(this::entityToDto).orElseThrow(()-> new IllegalArgumentException("이미지를 찾을 수 없습니다"));
    }

    @Transactional(readOnly = true)
    public PageResultDTO<ReviewDTO> reviewList(Long servicesId, Pageable pageable) {
        Slice<Review> result = new ReviewListRepository().listPage(servicesId,pageable);
        List<ReviewDTO> dtoList = result.getContent().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return new PageResultDTO<>(dtoList, pageable, result.hasNext());
    }

    @Transactional
    public List<ReviewDTO> getUserReviewList(Member member) {
        List<Review> result = reviewRepository.findByMemberId(member.getId());
        ArrayList<ReviewDTO> dtoList = new ArrayList<>();
        for(Review review : result) {
            ReviewDTO dto = entityToDto(review);
            dtoList.add(dto);
        }
        return dtoList;
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
//        List<String> thumbnails = review.getThumbnails();
        for (String image : images) {
            s3Service.deleteImage(image);
        }
//        for (String thumbnail : thumbnails) {
//            s3Service.deleteImage(thumbnail);
//        }
        reviewRepository.delete(review);
    }
}
