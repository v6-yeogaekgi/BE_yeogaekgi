package com.v6.yeogaekgi.community.repository;

import com.v6.yeogaekgi.community.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
