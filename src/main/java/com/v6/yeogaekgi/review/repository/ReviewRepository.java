package com.v6.yeogaekgi.review.repository;

import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.review.entity.Review;
import com.v6.yeogaekgi.services.entity.Services;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long>,ReviewListRepository {
    @EntityGraph(attributePaths = {"services"})
    @Query("""
    select r from Review r
    where r.services.id = :servicesId
    and r.status != 1
    order by r.regDate desc
""")
    List<Review> findImageMatchByServicesId(Long servicesId);

    @EntityGraph(attributePaths = {"services"})
    List<Review> findAllByServicesId(Long servicesId);

    Optional<Review> findByServicesIdAndId(Long servicesId, Long reviewId);

    Optional<Review> findByServicesIdAndIdAndMemberId(Long servicesId, Long reviewId, Long memberId);
}
