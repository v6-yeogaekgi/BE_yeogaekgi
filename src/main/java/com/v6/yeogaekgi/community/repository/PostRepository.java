//package com.v6.yeogaekgi.community.repository;
//
//
//import com.v6.yeogaekgi.community.dto.PostDTO;
//import com.v6.yeogaekgi.community.entity.Comment;
//import com.v6.yeogaekgi.community.entity.Post;
//import com.v6.yeogaekgi.member.entity.Member;
//import org.springframework.data.jpa.repository.EntityGraph;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface PostRepository extends JpaRepository<Post, Long> {
//    @EntityGraph(attributePaths = {"member"})
//    List<Post> findAllOrderByIdDesc();
//    List<Post> findByContentLikeOrderByIdDesc(String content);
//    List<Post> findByHashtagOrderByIdDesc(String hashtag);
//    List<Post> findByMemberByIdDesc(Member member);
//
//
//}