package com.v6.yeogaekgi.review.repository;

import com.v6.yeogaekgi.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface ReviewListRepository {
    @EntityGraph(attributePaths = {"member", "services"})
    Slice<Review> listPage(Long servicesId,Pageable pageable);

}
