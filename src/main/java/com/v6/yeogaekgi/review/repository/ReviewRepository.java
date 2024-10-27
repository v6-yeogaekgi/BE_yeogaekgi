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
    where r.services.id = :servicesId
    and r.status != 1
    order by r.regDate desc
""")
    List<Review> findImageMatchByServicesId(Long servicesNo);

    @Query("SELECT r FROM Review r WHERE r.member.id = :memberNo AND r.payment IS NOT NULL")
    List<Review> findByMemberIdNotNull(@Param("memberId") Long memberNo);

    Optional<Review> findByServicesIdAndId(Long servicesId, Long reviewId);

    Optional<Review> findByServicesIdAndIdAndMemberId(Long servicesId, Long reviewId, Long memberId);

    List<Review> findByMemberId(Long memberId);

    @Modifying
    @Query("UPDATE Review r SET r.payment = :payment WHERE r.no = :reviewNo")
    void updatePaymentById(@Param("reviewId") Long reviewNo, @Param("payment") Payment payment);

    Boolean existsByPaymentId(Long paymentNo);

    Boolean existsByServicesIdAndMemberId(Long servicesNo, Long memberNo);



}
