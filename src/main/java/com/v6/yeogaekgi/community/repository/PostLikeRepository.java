package com.v6.yeogaekgi.community.repository;

import com.v6.yeogaekgi.community.entity.PostLike;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPost_IdAndMember_Id(Long postId, Long member_Id); // member가 post에 좋아요를 누른것만
//
//    @EntityGraph(attributePaths = {"member"})
//    List<Long> findPost_IdByMember_Email(String memberEmail); // member가 좋아요 누른 postId 리스트
//
    boolean existsPost_IdAndMember_Id(Long postId, Long member_Id); // member가 post에 좋아요를 누른것만
}
