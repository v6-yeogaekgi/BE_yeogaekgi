package com.v6.yeogaekgi.services.controller;


import com.v6.yeogaekgi.security.MemberDetailsImpl;
import com.v6.yeogaekgi.services.entity.Services;
import com.v6.yeogaekgi.services.entity.ServicesType;
import com.v6.yeogaekgi.services.service.Servicesservice;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/services")
@Log4j2
@RequiredArgsConstructor
public class ServicesController {
    private final Servicesservice servicesservice;

    @GetMapping("/servicesList/{area}")
    public ResponseEntity<List<Services>> getAllServices(
            @PathVariable String area,
            @RequestParam(required = false) List<ServicesType> type,
            @RequestParam(required = false) Boolean myLike,
            @RequestParam(required = false) Boolean myReview,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {
        List<Services> services;
        Long memberId = memberDetails.getMember().getNo();
        // 기본 필터링 조건 설정
        if ((type == null || type.isEmpty()) && (myLike == null || !myLike) && (myReview == null || !myReview)) {
            services = servicesservice.findAllServices(area);
        } else {
            services = servicesservice.findServicesWithFilters(type, area, myLike, myReview, memberId);
        }
        return new ResponseEntity<>(services, HttpStatus.OK);
    }


    @GetMapping("/like/{servicesId}/check")
    public ResponseEntity<?> servicesLikeCheck(@PathVariable Long servicesId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        Long memberId = memberDetails.getMember().getNo();
        return new ResponseEntity<>(servicesservice.servicesLikeCheck(servicesId,memberId),HttpStatus.OK);
    }


    @PostMapping("/like/{servicesId}")
    public ResponseEntity<String> servicesLike (@PathVariable Long servicesId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        Long memberId = memberDetails.getMember().getNo();
        if(servicesservice.servicesLike(servicesId,memberId)){
            return new ResponseEntity<>("like add",HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("like cancel",HttpStatus.OK);
        }
    }

}
