package com.v6.yeogaekgi.services.repository;

import com.v6.yeogaekgi.services.entity.ServiceLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ServicesLikeRepository extends JpaRepository<ServiceLike,Long> {
    Optional<ServiceLike> findByServiceNoAndMemberNo(Long serviceNo, Long memberNo);

    List<ServiceLike> findByMemberNo(Long memberNo);

    @Query("""
    select count(sk)
    from ServiceLike sk
    where sk.service.id = :servicesId
""")
    Optional<Long> servicesLikeCount(Long servicesId);
}
