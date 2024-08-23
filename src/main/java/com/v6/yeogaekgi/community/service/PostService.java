package com.v6.yeogaekgi.community.service;


import com.v6.yeogaekgi.community.dto.HashtagDTO;
import com.v6.yeogaekgi.community.dto.PostDTO;
import com.v6.yeogaekgi.community.dto.SearchDTO;
import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.community.entity.PostLike;
import com.v6.yeogaekgi.community.repository.PostLikeRepository;
import com.v6.yeogaekgi.community.repository.PostRepository;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor

public class PostService {
    private final PostRepository repository;
    private final PostLikeRepository plRepository;


    // 게시글 리스트 (내용/해시태그 검색 포함)
    public List<PostDTO> getPostList(SearchDTO search, MemberDetailsImpl memberDetails) {
        // memberId 불러옴
        Long memberId= memberDetails == null ? 0L : memberDetails.getMember().getId();

        Pageable pageable = PageRequest.of(search.getPage(), 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Post> result;
        if(search.getHashtag() != null && !search.getHashtag().isEmpty()){
            result = repository.findByHashtag(search.getHashtag(), pageable);
        }else if(search.getContent() != null && !search.getContent().isEmpty()){
            result = repository.findByContentContaining(search.getContent(), pageable);
        }else if(search.getMyPost()){
            result = repository.findByMember_Id(memberId, pageable);
        }else{
            result = repository.findAll(pageable);
        }

        return result.getContent().stream()
                .map(Post -> entityToDto(Post))
                .collect(Collectors.toList());
    }

    // 게시글 상세
    public PostDTO getPost(Long postId, MemberDetailsImpl memberDetails) {
        // memberId 불러옴
        Long memberId= memberDetails == null ? 0L : memberDetails.getMember().getId();

        Optional<Post> post = repository.findById(postId);
        PostDTO postDto = null;
        if(post.isPresent()){
            postDto = entityToDto(post.get());
            postDto.setLikeState(plRepository.existsByPost_IdAndMember_Id(postId, memberId));
        }
        return postDto;
    }

    // 게시글 등록 [bongbong]
    public Long register(PostDTO postDTO,Member member) {
        Post post = dtoToEntity(postDTO,member);
        repository.save(post);
        return post.getId();
    }

    // 게시글 수정 [bongbong]
    public void modify(PostDTO postDTO) {

        Optional<Post> result = repository.findById(postDTO.getPostId());
        if(result.isPresent()){
            Post post = result.get();
            post.changeContent(postDTO.getContent());
            post.changeImages(postDTO.getImages());
            post.changeHashtag(postDTO.getHashtag());

            repository.save(post);
        }

    }

    // 게시글 삭제 [bongbong]
    public void remove(Long postId) {
        repository.deleteById(postId);

    }

    // 해시태그 검색 list
    public List<HashtagDTO> searchHashtag(String keyword) {
        List<Object[]> results = repository.getHashtag(keyword);
        List<HashtagDTO> hashtags = new ArrayList<>();

        for (Object[] result : results) {
            String hashtag = (String) result[0];
            int count = ((Number) result[1]).intValue();
            hashtags.add(new HashtagDTO(hashtag, count));
        }
        return hashtags;
    }






    // ============================= convert type =============================
    public Post dtoToEntity(PostDTO postDTO, Member member){

        Post post = Post.builder()
                .id(postDTO.getPostId())
                .content(postDTO.getContent())
                .hashtag(postDTO.getHashtag())
                .commentCnt(postDTO.getCommentCnt())
                .images(postDTO.getImages())
                .likeCnt(postDTO.getLikeCnt())
//                .member(Member.builder().id(postDTO.getMemberId()).build())
                .member(member)
                .build();

        return post;
    }
    public PostDTO entityToDto(Post post){

        PostDTO postDTO = PostDTO.builder()
                .postId(post.getId())
                .content(post.getContent())
                .hashtag(post.getHashtag())
                .commentCnt(post.getCommentCnt())
                .images(post.getImages())
                .likeCnt(post.getLikeCnt())
                .regDate(post.getRegDate())
                .modDate(post.getModDate())
                .memberId(post.getMember().getId())
                .nickname(post.getMember().getNickname())
                .build();

        return postDTO;
    }



}
