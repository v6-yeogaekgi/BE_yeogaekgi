package com.v6.yeogaekgi.services.service;

import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.review.entity.Review;
import com.v6.yeogaekgi.services.dto.ServiceResponseDTO;
import com.v6.yeogaekgi.services.dto.ServicesLikeResponsDTO;
import com.v6.yeogaekgi.services.entity.ServiceLike;
import com.v6.yeogaekgi.services.entity.Services;
import com.v6.yeogaekgi.services.repository.ServicesLikeRepository;
import com.v6.yeogaekgi.services.repository.ServicesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public List<ServiceResponseDTO> findAllServices() {
        List<Services> servicesList = servicesRepository.findAll();
        return servicesList.stream()
                .map(this::entityToDto) // entityToDto 메서드를 사용하여 변환
                .collect(Collectors.toList());
    }

    @Transactional
    public Boolean servicesLike (Long servicesId,Long memberId){
        Optional<ServiceLike> serviceLikeCheck = servicesLikeRepository.findByServiceIdAndMemberId(servicesId,memberId);
        Services services = servicesRepository.findById(servicesId)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));
//        if(serviceLikeCheck.isPresent()){
//            Long servicesLikeId = serviceLikeCheck.get().getId();
//            servicesLikeRepository.deleteById(servicesLikeId);
//
//            services.decreaseLikeCnt();
//            servicesRepository.save(services);
//            return false;
//        }else{
//            ServiceLike serviceLike = ServiceLike.builder()
//                    .service(Services.builder().id(servicesId).build())
//                    .member(Member.builder().id(memberId).build())
//                    .build();
//            servicesLikeRepository.save(serviceLike);
//            services.incrementLikeCnt();
//            servicesRepository.save(services);
//            return true;
//        }
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
            Long servicesLikeId = serviceLikeCheck.get().getId();
            servicesLikeRepository.deleteById(servicesLikeId);
            services.decreaseLikeCnt();
            servicesRepository.save(services);
            return false;
        }
    }

}
