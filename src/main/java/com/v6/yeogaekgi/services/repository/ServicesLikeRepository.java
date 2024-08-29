package com.v6.yeogaekgi.services.repository;

import com.v6.yeogaekgi.services.entity.ServiceLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServicesLikeRepository extends JpaRepository<ServiceLike,Long> {
    Optional<ServiceLike> findByServiceIdAndMemberId(Long serviceId, Long memberId);
}
