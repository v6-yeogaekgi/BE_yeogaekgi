package com.v6.yeogaekgi.review.repository;

import com.v6.yeogaekgi.payTrack.entity.Payment;
import com.v6.yeogaekgi.review.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    @EntityGraph(attributePaths = {"services"})
    @Query("""
    select r from Review r
    where r.services.no = :servicesNo
    and r.status != 1
    order by r.regDate desc
""")
    List<Review> findImageMatchByServicesNo(Long servicesNo);

    @Query("SELECT r FROM Review r WHERE r.member.no = :memberNo AND r.payment IS NOT NULL")
    List<Review> findByMemberNoNotNull(@Param("memberNo") Long memberNo);

    Optional<Review> findByServicesNoAndNo(Long servicesNo, Long reviewNo);

    Optional<Review> findByServicesNoAndNoAndMemberNo(Long servicesNo, Long reviewNo, Long memberNo);

    List<Review> findByMemberNo(Long memberNo);

    Boolean existsByPaymentNo(Long paymentNo);

    Boolean existsByServicesNoAndMemberNo(Long servicesNo, Long memberNo);



}
