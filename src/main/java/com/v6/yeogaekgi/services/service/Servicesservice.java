package com.v6.yeogaekgi.services.service;

import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.services.dto.ServicesDTO;
import com.v6.yeogaekgi.services.entity.Services;
import com.v6.yeogaekgi.services.entity.ServicesLike;
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
                .no(service.getNo())
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

    public List<ServicesDTO> findServicesWithFilters(List<ServicesType> type, String area, Boolean myLike, Boolean myReview, Long memberNo) {
        List<Services> result;

        // myLike와 myReview가 모두 true인 경우
        if (Boolean.TRUE.equals(myLike) && Boolean.TRUE.equals(myReview)) {
            result = (type == null || type.isEmpty())
                    ? servicesRepository.findServicesByAreaAndMyReviewANDmyLike(area, memberNo)
                    : servicesRepository.findServicesByTypesAndAreaAndMyReviewANDmyLike(type, area, memberNo);
            return result.stream().map(this::entityToDto).collect(Collectors.toList());
        }

        // myLike만 true인 경우
        if (Boolean.TRUE.equals(myLike)) {
            result = (type == null || type.isEmpty())
                    ? servicesRepository.findServicesByAreaAndMyLike(area, memberNo)
                    : servicesRepository.findServicesByTypesAndAreaAndMyLike(type, area, memberNo);
            return result.stream().map(this::entityToDto).collect(Collectors.toList());
        }

        // myReview만 true인 경우
        if (Boolean.TRUE.equals(myReview)) {
            result = (type == null || type.isEmpty())
                    ? servicesRepository.findServicesByAreaAndMyReview(area, memberNo)
                    : servicesRepository.findServicesByTypesAndAreaAndMyReview(type, area, memberNo);
            return result.stream().map(this::entityToDto).collect(Collectors.toList());
        }

        // 기본적으로 타입 필터만 적용된 서비스 조회
        result = servicesRepository.findServicesByTypesAndArea(type, area);
        return result.stream().map(this::entityToDto).collect(Collectors.toList());
    }


    @Transactional
    public boolean servicesLike (Long servicesNo,Long memberNo){
        Optional<ServicesLike> serviceLikeCheck = servicesLikeRepository.findByServiceNoAndMemberNo(servicesNo,memberNo);
        Services services = servicesRepository.findById(servicesNo)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));
        if (!serviceLikeCheck.isPresent()) {
            ServicesLike serviceLike = ServicesLike.builder()
                    .services(Services.builder().no(servicesNo).build())
                    .member(Member.builder().no(memberNo).build())
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
    public Map<String,Object> servicesLikeCheck(Long servicesNo,Long memberNo){
        Map<String,Object> result = new HashMap<>();
        Optional<ServicesLike> serviceLikeCheck = servicesLikeRepository.findByServiceNoAndMemberNo(servicesNo,memberNo);
        Optional<Long> servicesLikeCount =servicesLikeRepository.servicesLikeCount(servicesNo);
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
