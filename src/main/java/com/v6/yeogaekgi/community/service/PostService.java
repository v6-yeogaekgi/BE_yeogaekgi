package com.v6.yeogaekgi.community.service;


import com.v6.yeogaekgi.community.dto.HashtagDTO;
import com.v6.yeogaekgi.community.dto.PostDTO;
import com.v6.yeogaekgi.community.dto.SearchDTO;
import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.community.entity.PostLike;
import com.v6.yeogaekgi.community.repository.PostLikeRepository;
import com.v6.yeogaekgi.community.repository.PostRepository;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.util.PageDTO.PageRequestDTO;
import com.v6.yeogaekgi.util.PageDTO.PageResultDTO;
import com.v6.yeogaekgi.util.S3.S3Service;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor

public class PostService {
    private final PostRepository repository;
    private final PostLikeRepository plRepository;
    private final S3Service s3Service;


    // 게시글 리스트 (내용/해시태그 검색 포함)
    public PageResultDTO<PostDTO> getPostList(SearchDTO search, PageRequestDTO page, Member member) {
        Slice<Post> result = null;
        if("content".equals(search.getType())){
            result = repository.findByContentContaining(search.getKeyword());
        }
        if("hashtag".equals(search.getType())){
            result = repository.findByHashtag(search.getKeyword());
        }
        if(search.getMyPost()){
            result = repository.findByMember_Id(member.getId());
        }

        if(result != null){
            List<PostDTO> content = result.getContent().stream()
                    .map(Post -> entityToDto(Post, member))
                    .collect(Collectors.toList());
            Pageable pageable = PageRequest.of(page.getPage(), 10, Sort.by(Sort.Direction.DESC, "id"));

            PageResultDTO<PostDTO> pageResultDTO = new PageResultDTO<>(content, pageable, result.hasNext());

            return pageResultDTO;
        }
        return null;
    }

    // 게시글 상세
    public PostDTO getPost(Long postId, Member member) {
//        // memberId 불러옴
//        Long memberId= memberDetails == null ? 0L : memberDetails.getMember().getId();

        Optional<Post> post = repository.findById(postId);
        PostDTO postDto = null;
        if(post.isPresent()){
            postDto = entityToDto(post.get(), member);
            postDto.setLikeState(plRepository.existsByPost_IdAndMember_Id(postId, member.getId()));
        }
        return postDto;
    }

    // 게시글 등록 [bongbong]
    public Long register(Post post) {
//        Post post = dtoToEntity(postDTO,member);
        repository.save(post);
        return post.getId();
    }

    // 게시글 수정 [bongbong]
    public void modify(Post post) {

        Optional<Post> result = repository.findById(post.getId());
        if(result.isPresent()){
//            Post post = result.get();
//            post.changeContent(postDTO.getContent());
//            post.changeImages(postDTO.getImages());
//            post.changeHashtag(postDTO.getHashtag());

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
                .images(s3Service.convertListToString2(postDTO.getImages()))
                .likeCnt(postDTO.getLikeCnt())
                .member(member)
                .build();
        return post;
    }
    public PostDTO entityToDto(Post post, Member member){

        PostDTO postDTO = PostDTO.builder()
                .postId(post.getId())
                .content(post.getContent())
                .hashtag(post.getHashtag())
                .commentCnt(post.getCommentCnt())
                .images(s3Service.convertStringToList(post.getImages()))
                .likeCnt(post.getLikeCnt())
                .regDate(post.getRegDate())
                .modDate(post.getModDate())
                .memberId(post.getMember().getId())
                .nickname(post.getMember().getNickname())
                .code(post.getMember().getCountry().getCode())
                .currentMemberId(member.getId())
                .currentMemberCode(member.getCountry().getCode())
                .build();

        return postDTO;
    }



}