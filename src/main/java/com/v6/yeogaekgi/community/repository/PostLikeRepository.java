package com.v6.yeogaekgi.community.repository;

import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.community.entity.PostLike;
import com.v6.yeogaekgi.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    @Query("SELECT pl.post.no FROM PostLike pl WHERE pl.member.no = :memberNo")
    List<Long> findPost_IdByMember_Id(@Param("memberNo") Long memberNo);

    @EntityGraph(attributePaths = {"member"})
    boolean existsByPost_IdAndMember_Id(Long postNo, Long memberNo); // member가 post에 좋아요를 누른건지 확인

    void deleteByPost_IdAndMember_Id(Long postNo, Long memberNo);
}
