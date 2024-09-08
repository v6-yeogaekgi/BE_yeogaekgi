package com.v6.yeogaekgi.review.service;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.payTrack.entity.Payment;
import com.v6.yeogaekgi.payTrack.repository.PaymentRepository;
import com.v6.yeogaekgi.payTrack.service.PaymentService;
import com.v6.yeogaekgi.review.dto.*;
import com.v6.yeogaekgi.review.entity.Review;
import com.v6.yeogaekgi.review.repository.ReviewRepository;
import com.v6.yeogaekgi.services.entity.Services;
import com.v6.yeogaekgi.services.repository.ServicesRepository;
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

    public Review dtoToEntity(ReviewRequestDTO reviewRequestDTO,Member member,Long servicesId) {
        //url들을 프론트에서 배열로 보내줄 예정
        Review review = Review.builder()
                .score(reviewRequestDTO.getScore())
                .content(reviewRequestDTO.getContent())
                .member(member)
//                .payment(Payment.builder().id(reviewRequestDTO.getPayNo()).build())
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
              if(review.getPayment()!= null){
                  responseDTO.setPaymentId(review.getPayment().getId());
              }
           return responseDTO;
    }

    @Transactional
    public Long register(List<MultipartFile> multipartFile, Long servicesId,ReviewRequestDTO reviewRequestDTO, Member member) {
        String service = servicesRepository.findServiceNameById(servicesId);

        Optional<Payment> payment = paymentRepository.findByMemberIdAndServiceName(member.getId(),service, reviewRequestDTO.getPayNo());
        if(!payment.isPresent()) {
            throw new EntityNotFoundException("Payment not found with id: " + reviewRequestDTO.getPayNo());
        }

        Boolean reviewExist = reviewRepository.existsByPaymentId(reviewRequestDTO.getPayNo());
        if(reviewExist){
            throw new IllegalStateException("Review already exists for this payment");
        }

        Review review = dtoToEntity(reviewRequestDTO,member,servicesId);
        review.setPayment(payment.get());

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
        System.out.println(savedReview);

        return savedReview.getId();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> ImageList (Long serviceId) {
        List<Review> reviews = reviewRepository.findImageMatchByServicesId(serviceId);
        List<ReviewResponseDTO> result = new ArrayList<>();
        for (Review review : reviews) {
            ReviewResponseDTO dto = ReviewResponseDTO.builder()
                    .images(review.getImages())
                    .nickname(review.getMember().getNickname())
                    .country(review.getMember().getCountry())
                    .score(review.getScore())
                    .build();
            result.add(dto);
        }
        return result;
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public SliceResponse<ReviewResponseDTO> reviewList(Long servicesId, Pageable pageable) {
        // ReviewRepository의 listPage 메서드를 호출하여 페이징된 리뷰 목록을 가져옴
        Slice<Review> result = reviewRepository.listPage(servicesId, pageable);
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
    public List<ReviewResponseDTO> getUserReviewList(Member member) {
        List<Review> result = reviewRepository.findByMemberId(member.getId());
        ArrayList<ReviewResponseDTO> dtoList = new ArrayList<>();
        for(Review review : result) {
            ReviewResponseDTO dto = ReviewResponseDTO.builder()
                    .reviewId(review.getId())
                    .images(review.getImages())
                    .thumbnail(review.getThumbnails())
                    .score(review.getScore())
                    .content(review.getContent())
                    .status(review.getStatus())
                    .nickname(review.getMember().getNickname())
                    .country(review.getMember().getCountry())
                    .serviceId(review.getServices().getId())
                    .serviceName(review.getServices().getName())
                    .regDate(review.getRegDate())
                    .modDate(review.getModDate())
                    .build();
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
