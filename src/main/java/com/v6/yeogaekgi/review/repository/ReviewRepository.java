package com.v6.yeogaekgi.review.repository;

import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.review.dto.ReviewResponseDTO;
import com.v6.yeogaekgi.review.entity.Review;
import com.v6.yeogaekgi.services.entity.Services;
import jakarta.persistence.Entity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long>,ReviewListRepository {
    @EntityGraph(attributePaths = {"services"})
    @Query("""
    select r.images from Review r
    join Services s on s.id =:serviceId
    where r.status != 1
""")
    List<String> findImagesByServiceId(Long serviceId);

}
