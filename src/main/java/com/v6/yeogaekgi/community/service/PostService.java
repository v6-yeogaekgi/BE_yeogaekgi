package com.v6.yeogaekgi.community.service;


import com.v6.yeogaekgi.community.dto.PostDTO;
import com.v6.yeogaekgi.community.dto.PostRequestDTO;
import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.community.repository.PostRepository;
import com.v6.yeogaekgi.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor

public class PostService {
    private final PostRepository repository;

    public List<PostDTO> getList(PostRequestDTO dto, String jwt) { //test 필요
        Pageable pageable = PageRequest.of(dto.getPage(), 10);
        Page<Object[]> result;

        // 토큰에서 유저 id 가져옴
        Long memberId = 1L; // 일단은 고정..


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

    public Long register(PostDTO postDTO) {
        Post post = dtoToEntity(postDTO);
        repository.save(post);
        return post.getId();
    }



    public Post dtoToEntity(PostDTO postDTO){

        Post post = Post.builder()
                .id(postDTO.getPostId())
                .content(postDTO.getContent())
                .hashtag(postDTO.getHashtag())
                .commentCnt(postDTO.getCommentCnt())
                .images(postDTO.getImages())
                .likeCnt(postDTO.getLikeCnt())
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
                .build();

        return postDTO;
    }
    private PostDTO objectToDto(Object[] objects) {
        PostDTO postDto = new PostDTO();
        postDto.setPostId((Long) objects[0]);
        postDto.setMemberId((Long) objects[1]);
        postDto.setContent((String) objects[2]);
        postDto.setImages((String) objects[3]);
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