package com.v6.yeogaekgi.services.controller;

import com.amazonaws.services.ec2.model.ServiceType;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import com.v6.yeogaekgi.services.dto.ServiceResponseDTO;
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
import java.util.Map;
import java.util.Objects;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/services")
@Log4j2
@RequiredArgsConstructor
public class ServicesController {
    private final Servicesservice servicesservice;

    @GetMapping("/servicesList")
    public ResponseEntity<List<Services>> getAllServices(@RequestParam(required = false)List<ServicesType> type) {
        List<Services> services;
        if (type == null||type.isEmpty()) {
            services = servicesservice.findAllServices();
        } else {
            services = servicesservice.findServicesByTypes(type);
        }
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        // 예외의 원인과 메시지를 로그에 출력
        System.err.println("Exception: " + ex.getMessage());
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/like/{servicesId}/check")
    public ResponseEntity<?> all(@PathVariable Long servicesId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        Long memberId = memberDetails.getMember().getId();
        return new ResponseEntity<>(servicesservice.servicesLikeCheck(servicesId,memberId),HttpStatus.OK);
    }


    @PostMapping("/like/{servicesId}")
    public ResponseEntity<Map<String, Object>> servicesLike (@PathVariable Long servicesId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        Long memberId = memberDetails.getMember().getId();
        return new ResponseEntity<>(servicesservice.servicesLike(servicesId,memberId),HttpStatus.OK);
    }


}
