package com.v6.yeogaekgi.services.service;

import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.services.dto.ServicesDTO;
import com.v6.yeogaekgi.services.entity.ServiceLike;
import com.v6.yeogaekgi.services.entity.Services;
import com.v6.yeogaekgi.services.entity.ServicesType;
import com.v6.yeogaekgi.services.repository.ServicesLikeRepository;
import com.v6.yeogaekgi.services.repository.ServicesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class Servicesservice {
    private final ServicesRepository servicesRepository;
    private final ServicesLikeRepository servicesLikeRepository;

    public ServicesDTO entityToDto(Services service) {
        return ServicesDTO.builder()
                .id(service.getId())
                .serviceType(service.getType())
                .name(service.getName())
                .content(service.getContent())
                .address(service.getAddress())
                .lat(service.getLat())
                .lon(service.getLon())
                .likeCnt(service.getLikeCnt())
                .build();
    }

    public List<ServicesDTO> findAllServices(String area) {
        return servicesRepository.findAllServices(area).stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public List<ServicesDTO> findServicesWithFilters(List<ServicesType> type, String area, Boolean myLike, Boolean myReview, Long memberId) {
        // myLike와 myReview가 모두 true인 경우
        List<Services> result = null;
        if (myLike && myReview) {
            result= (type == null || type.isEmpty())
                    ? servicesRepository.findServicesByAreaAndMyReviewANDmyLike(area, memberId)
                    : servicesRepository.findServicesByTypesAndAreaAndMyReviewANDmyLike(type, area, memberId);
        }

        // myLike만 true인 경우
        if (myLike) {
            result= (type == null || type.isEmpty())
                    ? servicesRepository.findServicesByAreaAndMyLike(area, memberId)
                    : servicesRepository.findServicesByTypesAndAreaAndMyLike(type, area, memberId);
        }

        // myReview만 true인 경우
        if (myReview) {
            result= (type == null || type.isEmpty())
                    ? servicesRepository.findServicesByAreaAndMyReview(area, memberId)
                    : servicesRepository.findServicesByTypesAndAreaAndMyReview(type, area, memberId);
        }

        // 기본적으로 타입 필터만 적용된 서비스 조회
        result= servicesRepository.findServicesByTypesAndArea(type, area);
        return result.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Transactional
    public boolean servicesLike (Long servicesId,Long memberId){
        Optional<ServiceLike> serviceLikeCheck = servicesLikeRepository.findByServiceIdAndMemberId(servicesId,memberId);
        Services services = servicesRepository.findById(servicesId)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));
        if (!serviceLikeCheck.isPresent()) {
            ServiceLike serviceLike = ServiceLike.builder()
                    .service(Services.builder().id(servicesId).build())
                    .member(Member.builder().id(memberId).build())
                    .build();
            servicesLikeRepository.save(serviceLike);
            services.incrementLikeCnt();
            servicesRepository.save(services);
            return true;
        }else{
            Long servicesLikeId = serviceLikeCheck.get().getNo();
            servicesLikeRepository.deleteById(servicesLikeId);
            services.decreaseLikeCnt();
            servicesRepository.save(services);
            return false;
        }

    }
    @Transactional
    public Map<String,Object> servicesLikeCheck(Long servicesId,Long memberId){
        Map<String,Object> result = new HashMap<>();
        Optional<ServiceLike> serviceLikeCheck = servicesLikeRepository.findByServiceIdAndMemberId(servicesId,memberId);
        Optional<Long> servicesLikeCount =servicesLikeRepository.servicesLikeCount(servicesId);
        if(serviceLikeCheck.isPresent()){
            result.put("status",true);
            result.put("count",servicesLikeCount.get());
            return result;
        }else{
            result.put("status",false);
            result.put("count",servicesLikeCount.get());
            return result;
        }
    }

}
