package com.v6.yeogaekgi.community.repository;


import com.v6.yeogaekgi.community.dto.HashtagDTO;
import com.v6.yeogaekgi.community.dto.PostDTO;
import com.v6.yeogaekgi.community.entity.Comment;
import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 해시태그 검색
    @Query(value = "SELECT p.hashtag, COUNT(p.hashtag) count FROM post p WHERE p.hashtag LIKE CONCAT(:hashtag, '%') GROUP BY p.hashtag ORDER BY count DESC limit 10", nativeQuery = true)
    List<Object[]> getHashtag(String hashtag);
    
    // 전체 리스트 조회
    @EntityGraph(attributePaths = {"member"})
    Page<Post> findAll(Pageable pageable);


    // 내용 검색 리스트 조회
    @EntityGraph(attributePaths = {"member"})
    Page<Post> findByContentContaining(String content, Pageable pageable);

    // 해시 검색 리스트 조회
    @EntityGraph(attributePaths = {"member"})
    Page<Post> findByHashtag(String hashtag, Pageable pageable);

    // 해시 검색 리스트 조회
    @EntityGraph(attributePaths = {"member"})
    Page<Post> findByMember_Id(Long memberId, Pageable pageable);



}
