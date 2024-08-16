package com.v6.yeogaekgi.review.repository;

import com.v6.yeogaekgi.review.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review,Long> {


}
