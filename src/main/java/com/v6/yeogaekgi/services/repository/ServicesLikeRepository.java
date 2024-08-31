package com.v6.yeogaekgi.services.repository;

import com.v6.yeogaekgi.services.entity.ServiceLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.OptionalInt;

public interface ServicesLikeRepository extends JpaRepository<ServiceLike,Long> {
    Optional<ServiceLike> findByServiceIdAndMemberId(Long serviceId, Long memberId);

    @Query("""
    select count(sk)
    from ServiceLike sk
    where sk.service.id = :servicesId
""")
    Optional<Long> servicesLikeCount(Long servicesId);
}
