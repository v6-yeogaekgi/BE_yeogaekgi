package com.v6.yeogaekgi.services.service;

import com.amazonaws.services.ec2.model.ServiceType;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.review.entity.Review;
import com.v6.yeogaekgi.services.dto.ServiceResponseDTO;
import com.v6.yeogaekgi.services.dto.ServicesLikeResponsDTO;
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

    public ServiceResponseDTO entityToDto(Services service) {
        return ServiceResponseDTO.builder()
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

    public List<Services> findAllServices() {
        return servicesRepository.findAll();
    }

    public List<Services> findServicesByTypes(List<ServicesType> type){
        return servicesRepository.findByTypes(type);
    }

    @Transactional
    public Map<String,Object> servicesLike (Long servicesId,Long memberId){
        Map<String,Object> result = new HashMap<>();
        Boolean likeCheckRs =null;
        Optional<ServiceLike> serviceLikeCheck = servicesLikeRepository.findByServiceIdAndMemberId(servicesId,memberId);
        Services services = servicesRepository.findById(servicesId)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));
        if(serviceLikeCheck.isPresent()){
            Long servicesLikeId = serviceLikeCheck.get().getId();
            servicesLikeRepository.deleteById(servicesLikeId);
            services.decreaseLikeCnt();
            int countUpdated = services.getLikeCnt();
            likeCheckRs = false;
            servicesRepository.save(services);
            result.put("like cancel",countUpdated);
            result.put("likeCheckRs",likeCheckRs);
            return result;
        }else{
            ServiceLike serviceLike = ServiceLike.builder()
                    .service(Services.builder().id(servicesId).build())
                    .member(Member.builder().id(memberId).build())
                    .build();
            servicesLikeRepository.save(serviceLike);
            services.incrementLikeCnt();
            int countUpdated = services.getLikeCnt();
            likeCheckRs = true;
            servicesRepository.save(services);
            result.put("like add",countUpdated);
            result.put("likeCheckRs",likeCheckRs);
            return result;
        }
    }
    @Transactional
    public Boolean servicesLikeCheck(Long servicesId,Long memberId){
        Optional<ServiceLike> serviceLikeCheck = servicesLikeRepository.findByServiceIdAndMemberId(servicesId,memberId);
        Services services = servicesRepository.findById(servicesId)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));
        if(serviceLikeCheck.isPresent()){
            return true;
        }else{
            return false;
        }
    }


}
