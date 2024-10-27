package com.v6.yeogaekgi.community.repository;


import com.v6.yeogaekgi.community.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 해시태그 검색
    @Query(value = "SELECT p.hashtag, COUNT(p.hashtag) count FROM post p WHERE p.hashtag LIKE CONCAT(:hashtag, '%') GROUP BY p.hashtag ORDER BY count DESC limit 10", nativeQuery = true)
    List<Object[]> getHashtag(String hashtag);

    // 내용 검색 리스트 조회
    @EntityGraph(attributePaths = {"member"})
    Slice<Post> findByContentContaining(String content, Pageable pageable);

    // 해시 검색 리스트 조회
    @EntityGraph(attributePaths = {"member"})
    Slice<Post> findByHashtag(String hashtag, Pageable pageable);

    // 해시 검색 리스트 조회
    @EntityGraph(attributePaths = {"member"})
    Slice<Post> findByMember_No(Long memberNo, Pageable pageable);



}
