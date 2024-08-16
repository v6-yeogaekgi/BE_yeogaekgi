package com.v6.yeogaekgi.community.repository;


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

public interface PostRepository extends JpaRepository<Post, Long> {
    // list
    @Query(value = "SELECT p.post_no, p.member_no, p.content, p.images, p.hashtag, p.like_cnt, p.comment_cnt, p.mod_date, p.reg_date," +
            "IF(pl.post_no IS NULL, 0, 1) AS like_state, m.nickname " +
            "FROM post p " +
            "LEFT JOIN (SELECT post_no FROM post_like WHERE member_no = :memberNo) pl " +
            "ON p.post_no = pl.post_no "+
            "JOIN member m ON m.member_no = p.member_no "+
            " ORDER BY p.postno DESC",
            countQuery = "SELECT COUNT(*) FROM post p " +
                    "LEFT JOIN (SELECT post_no FROM post_like WHERE member_no = :memberNo) pl " +
                    "ON p.post_no = pl.post_no "+
                    "JOIN member m ON m.member_no = p.member_no ",
            nativeQuery = true)
    Page<Object[]> getAllPosts(@Param("memberNo") Long memberNo, Pageable pageable);

    @Query(value = "SELECT p.post_no, p.member_no, p.content, p.images, p.hashtag, p.like_cnt, p.comment_cnt, p.mod_date, p.reg_date," +
            "IF(pl.post_no IS NULL, 0, 1) AS like_state, m.nickname " +
            "FROM post p " +
            "LEFT JOIN (SELECT post_no FROM post_like WHERE member_no = :memberNo) pl " +
            "ON p.post_no = pl.post_no " +
            "JOIN member m ON m.member_no = p.member_no "+
            "WHERE p.member_no = :memberNo"+
            " ORDER BY p.postno DESC",
            countQuery = "SELECT COUNT(*) FROM post p " +
                    "LEFT JOIN (SELECT post_no FROM post_like WHERE member_no = :memberNo) pl " +
                    "ON p.post_no = pl.post_no " +
                    "JOIN member m ON m.member_no = p.member_no "+
                    "WHERE p.member_no = :memberNo",
            nativeQuery = true)
    Page<Object[]> getMemberPosts(@Param("memberNo") Long memberNo, Pageable pageable);

    @Query(value = "SELECT p.post_no, p.member_no, p.content, p.images, p.hashtag, p.like_cnt, p.comment_cnt, p.mod_date, p.reg_date," +
            "IF(pl.post_no IS NULL, 0, 1) AS like_state, m.nickname " +
            "FROM post p " +
            "LEFT JOIN (SELECT post_no FROM post_like WHERE member_no = :memberNo) pl " +
            "ON p.post_no = pl.post_no "+
            "JOIN member m ON m.member_no = p.member_no " +
            "WHERE p.hashtag = :hashtag"+
            " ORDER BY p.postno DESC",
            countQuery = "SELECT COUNT(*) FROM post p " +
                    "LEFT JOIN (SELECT post_no FROM post_like WHERE member_no = :memberNo) pl " +
                    "ON p.post_no = pl.post_no "+
                    "JOIN member m ON m.member_no = p.member_no " +
                    "WHERE p.hashtag = :hashtag",
            nativeQuery = true)
    Page<Object[]> getfindPostsByHashtag(@Param("memberNo") Long memberNo, @Param("hashtag") String hashtag, Pageable pageable);

    @Query(value = "SELECT p.post_no, p.member_no, p.content, p.images, p.hashtag, p.like_cnt, p.comment_cnt, p.mod_date, p.reg_date," +
            "IF(pl.post_no IS NULL, 0, 1) AS like_state, m.nickname " +
            "FROM post p " +
            "LEFT JOIN (SELECT post_no FROM post_like WHERE member_no = :memberNo) pl " +
            "ON p.post_no = pl.post_no " +
            "JOIN member m ON m.member_no = p.member_no "+
            "WHERE p.content LIKE %:content%"+
            " ORDER BY p.postno DESC",
            countQuery = "SELECT COUNT(*) FROM post p " +
                    "LEFT JOIN (SELECT post_no FROM post_like WHERE member_no = :memberNo) pl " +
                    "ON p.post_no = pl.post_no "+
                    "JOIN member m ON m.member_no = p.member_no " +
                    "WHERE p.content LIKE %:content%",
            nativeQuery = true)
    Page<Object[]> getfindPostsByContent(@Param("memberNo") Long memberNo, @Param("content") String content, Pageable pageable);

}