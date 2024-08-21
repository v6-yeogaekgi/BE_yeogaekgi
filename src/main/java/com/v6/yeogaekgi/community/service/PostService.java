package com.v6.yeogaekgi.community.service;


import com.v6.yeogaekgi.community.dto.HashtagDTO;
import com.v6.yeogaekgi.community.dto.PostDTO;
import com.v6.yeogaekgi.community.dto.SearchDTO;
import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.community.entity.PostLike;
import com.v6.yeogaekgi.community.repository.PostLikeRepository;
import com.v6.yeogaekgi.community.repository.PostRepository;
import com.v6.yeogaekgi.member.entity.Member;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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


    // 게시글 리스트 (검색/필터 포함) [settong]
    public List<PostDTO> getList(SearchDTO dto, String jwt) {
        Pageable pageable = PageRequest.of(dto.getPage(), 10);
        Page<Object[]> result;

        // jwt
        Long memberId = 0L; // 일단은 고정.. // 토큰에서 유저 id 가져옴


        if(dto.getHashtag() != null && !dto.getHashtag().isEmpty()){
            result = repository.getfindPostsByHashtag(memberId, dto.getHashtag(), pageable);
        } else if(dto.getContent() != null && !dto.getContent().isEmpty()){
            result = repository.getfindPostsByContent(memberId, dto.getContent(), pageable);
        } else if(dto.getMyPost() == 1 && memberId != null && memberId > 0 ){
            result = repository.getMemberPosts(memberId, pageable);
        }else{
            result = repository.getAllPosts(memberId, pageable);
        }
        for (Object[] row : result.getContent()) {
            System.out.println("ㄴRow data: "+ Arrays.toString(row));
        }

        return result.getContent().stream()
                .map(Post -> objectToDto(Post))
                .collect(Collectors.toList());

    }

    // 게시글 상세 [settong]
    public PostDTO getPost(Long postId, String jwt) {

        Long memberId = 1L; // 일단은 고정.. // 토큰에서 유저 id 가져옴

        Optional<Post> post = repository.findById(postId);
        PostDTO postDto = null;
        if(post.isPresent()){
            postDto = entityToDto(post.get());
            Optional<PostLike> like = plRepository.findByPost_IdAndMember_Id(postId, memberId);
            if(like.isPresent()){
                postDto.setLikeState(1);
            }else{
                postDto.setLikeState(0);
            }

        }
        return postDto;
    }

    // 게시글 등록 [bongbong]
    public Long register(PostDTO postDTO) {
        Post post = dtoToEntity(postDTO);
        repository.save(post);
        return post.getId();
    }

    // 게시글 수정 [bongbong]
    public void modify(PostDTO postDTO) {

        Optional<Post> result = repository.findById(postDTO.getPostId());
        if(result.isPresent()){
            Post post = result.get();
            post.changeContent(postDTO.getContent());
            post.changeImages(postDTO.getImages()!= null && postDTO.getImages().length > 0 ? String.join(" ", postDTO.getImages()): null);
            post.changeHashtag(postDTO.getHashtag());

            repository.save(post);
        }

    }

    // 게시글 삭제 [bongbong]
    public void remove(Long postId) {
        repository.deleteById(postId);

    }

    // 해시태그 검색 list [settong]
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
    public Post dtoToEntity(PostDTO postDTO){

        Post post = Post.builder()
                .id(postDTO.getPostId())
                .content(postDTO.getContent())
                .hashtag(postDTO.getHashtag())
                .commentCnt(postDTO.getCommentCnt())
                .images(String.join(" ", postDTO.getImages()))
                .likeCnt(postDTO.getLikeCnt())
                .member(Member.builder().id(postDTO.getMemberId()).build())
                .build();

        return post;
    }
    public PostDTO entityToDto(Post post){

        PostDTO postDTO = PostDTO.builder()
                .postId(post.getId())
                .content(post.getContent())
                .hashtag(post.getHashtag())
                .commentCnt(post.getCommentCnt())
                .images(post.getImages() != null ? (post.getImages()).split(" ") : new String[] {}  )
                .likeCnt(post.getLikeCnt())
                .regDate(post.getRegDate())
                .modDate(post.getModDate())
                .memberId(post.getMember().getId())
                .nickname(post.getMember().getNickname())
                .build();

        return postDTO;
    }
    private PostDTO objectToDto(Object[] objects) {
        PostDTO postDto = new PostDTO();

        postDto.setPostId((Long) objects[0]);
        postDto.setMemberId((Long) objects[1]);
        postDto.setContent((String) objects[2]);
        postDto.setImages((objects[3] != null ? ((String)objects[3]).split(" ") : new String[] {} ));
        postDto.setHashtag((String) objects[4]);
        postDto.setLikeCnt((Integer) objects[5]);
        postDto.setCommentCnt((Integer) objects[6]);
        postDto.setModDate((Timestamp) objects[7]);
        postDto.setRegDate((Timestamp) objects[8]);
        postDto.setLikeState(((Long)objects[9]).intValue());
        postDto.setNickname((String) objects[10]);
        return postDto;
    }


}
