package com.v6.yeogaekgi.community.repository;

import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.community.entity.PostLike;
import com.v6.yeogaekgi.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    @EntityGraph(attributePaths = {"member"})
    List<Long> findPost_IdByMember_Id(Long memberId); // member가 좋아요 누른 postId 리스트

    @EntityGraph(attributePaths = {"member"})
    boolean existsByPost_IdAndMember_Id(Long postId, Long member_Id); // member가 post에 좋아요를 누른건지 확인

    void deleteByPost_IdAndMember_Id(Long postId, Long memberId);
}
