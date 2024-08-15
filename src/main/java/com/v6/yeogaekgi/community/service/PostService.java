package com.v6.yeogaekgi.community.service;


import com.v6.yeogaekgi.community.dto.CommentDTO;
import com.v6.yeogaekgi.community.dto.PostDTO;
import com.v6.yeogaekgi.community.entity.Comment;
import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.community.repository.CommentRepository;
import com.v6.yeogaekgi.community.repository.PostRepository;
import com.v6.yeogaekgi.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor

public class PostService {
    private final PostRepository repository;

    public List<PostDTO> getList(PostDTO dto) { //test 필요
        List<Post> result;
        if(dto.getHashtag() != null){
            result = repository.findByHashtagOrderByIdDesc(dto.getHashtag());
        } else if(dto.getContent() != null){
            result = repository.findByContentLikeOrderByIdDesc("%"+dto.getContent()+"%");
        } else if(dto.getMemberNo() > 0){
            Member member = Member.builder().id(dto.getMemberNo()).build();
            result = repository.findByMemberByIdDesc(member);
        }else{
            result = repository.findAllOrderByIdDesc();
        }
        return result.stream().map(Post -> entityToDto(Post)).collect(Collectors.toList());

    }

    public Long register(PostDTO postDTO) {
        Post post = dtoToEntity(postDTO);
        repository.save(post);
        return post.getId();
    }



    public Post dtoToEntity(PostDTO postDTO){

        Post post = Post.builder()
                .id(postDTO.getPostNo())
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .hashtag(postDTO.getHashtag())
                .comment_cnt(postDTO.getCommentCnt())
                .images(postDTO.getImages())
                .like_cnt(postDTO.getLikeCnt())
                .build();

        return post;
    }

    public PostDTO entityToDto(Post post){

        PostDTO postDTO = PostDTO.builder()
                .postNo(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .hashtag(post.getHashtag())
                .commentCnt(post.getComment_cnt())
                .images(post.getImages())
                .likeCnt(post.getLike_cnt())
                .regDate(post.getRegDate())
                .modDate(post.getModDate())
                .build();

        return postDTO;
    }


}