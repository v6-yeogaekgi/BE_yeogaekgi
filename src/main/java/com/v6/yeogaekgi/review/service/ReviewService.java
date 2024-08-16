package com.v6.yeogaekgi.review.service;

import com.v6.yeogaekgi.member.dto.MemberRequestDTO;
import com.v6.yeogaekgi.member.repository.MemberRepository;
import com.v6.yeogaekgi.review.dto.ReviewRequestDTO;
import com.v6.yeogaekgi.review.entity.Review;
import com.v6.yeogaekgi.review.repository.ReviewRepository;
import com.v6.yeogaekgi.service.repository.ServiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ServiceRepository serviceRepository;
//
//    Map<String, Object> dtoToEntity(ReviewRequestDTO requestDTO){
//        Map<String,Object> entityMap = new HashMap();
//        Review review = Review.builder();
//
//    }




}
