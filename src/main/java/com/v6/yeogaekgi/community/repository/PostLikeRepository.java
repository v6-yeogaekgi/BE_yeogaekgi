package com.v6.yeogaekgi.community.repository;

import com.v6.yeogaekgi.community.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPost_IdAndMember_Id(Long postId, Long memberId);
}
