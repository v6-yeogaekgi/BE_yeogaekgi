package com.v6.yeogaekgi.community.repository;

import com.v6.yeogaekgi.community.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeReopository extends JpaRepository<Comment, Long> {

}
