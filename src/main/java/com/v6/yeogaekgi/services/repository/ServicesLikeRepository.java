package com.v6.yeogaekgi.services.repository;

import com.v6.yeogaekgi.services.entity.ServicesLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface ServicesLikeRepository extends JpaRepository<ServicesLike,Long> {
//    Optional<ServiceLike> findByServiceIdAndMemberId(Long serviceId, Long memberId);
    Optional<ServicesLike> findByServiceNoAndMemberNo(Long serviceNo, Long memberNo);

    List<ServicesLike> findByMemberNo(Long memberNo);

    @Query("""
    select count(sk)
    from ServicesLike sk
    where sk.services.no = :servicesNo
""")
    Optional<Long> servicesLikeCount(Long servicesNo);
}
