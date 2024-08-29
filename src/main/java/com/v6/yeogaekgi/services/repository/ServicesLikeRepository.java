package com.v6.yeogaekgi.services.repository;

import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.services.entity.ServiceLike;
import nonapi.io.github.classgraph.utils.LogNode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServicesLikeRepository extends JpaRepository<ServiceLike,Long> {
    Optional<ServiceLike> findByServiceIdAndMemberId(Long serviceId, Long memberId);
    List<ServiceLike> findByMemberId(Long memberId);

}
