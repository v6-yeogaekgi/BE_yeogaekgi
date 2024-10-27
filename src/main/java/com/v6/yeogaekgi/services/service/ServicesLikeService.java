package com.v6.yeogaekgi.services.service;

import com.v6.yeogaekgi.services.dto.ServicesLikeDTO;
import com.v6.yeogaekgi.services.entity.ServiceLike;
import com.v6.yeogaekgi.services.entity.Services;
import com.v6.yeogaekgi.services.repository.ServicesLikeRepository;
import com.v6.yeogaekgi.services.repository.ServicesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ServicesLikeService {
    private final ServicesRepository servicesRepository;
    private final ServicesLikeRepository servicesLikeRepository;

    public List<ServicesLikeDTO> findAllServiceLike(Long memberNo) {
        List<ServiceLike> serviceLikeListByMemberNo = servicesLikeRepository.findByMemberId(memberNo);
        return serviceLikeListByMemberNo.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        servicesLikeRepository.deleteById(id);
    }

    private ServicesLikeDTO entityToDto(ServiceLike serviceLike) {
        Optional<Services> byId = servicesRepository.findById(serviceLike.getService().getId());
        if(!byId.isPresent()){
            return null;
        }
        Services services = byId.get();

        return ServicesLikeDTO.builder()
                .servicesLikeId(serviceLike.getId())
                .memberId(serviceLike.getMember().getNo())
                .servicesId(serviceLike.getService().getId())
                .address(services.getAddress())
                .content(services.getContent())
                .likeCnt(services.getLikeCnt())
                .name(services.getName())
                .type(services.getType().ordinal())
                .build();
    }
}
