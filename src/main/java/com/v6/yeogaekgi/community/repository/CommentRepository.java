package com.v6.yeogaekgi.community.repository;


import com.v6.yeogaekgi.community.entity.Comment;
import com.v6.yeogaekgi.community.entity.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"member"})
    List<Comment> findByPost(Post post);


}
