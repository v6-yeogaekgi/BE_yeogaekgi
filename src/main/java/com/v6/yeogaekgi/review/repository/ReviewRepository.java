package com.v6.yeogaekgi.review.repository;

import com.v6.yeogaekgi.review.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
