package com.v6.yeogaekgi.services.service;

import com.v6.yeogaekgi.review.entity.Review;
import com.v6.yeogaekgi.services.dto.ServiceResponseDTO;
import com.v6.yeogaekgi.services.entity.Services;
import com.v6.yeogaekgi.services.repository.ServicesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class Servicesservice {
    private final ServicesRepository servicesRepository;

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

}
