package com.v6.yeogaekgi.community.service;


import com.v6.yeogaekgi.community.dto.CommentDTO;
import com.v6.yeogaekgi.community.entity.Comment;
import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.community.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor

public class CommentServiceImpl {
    private final CommentRepository repository;

    public List<CommentDTO> getListOfMovie(Long postNo) {

        Post post = Post.builder().post_no(postNo).build();
        List<Comment> result = repository.findByPost(post);
        return result.stream().map(Comment -> entityToDto(Comment)).collect(Collectors.toList());

    }

    public Long register(CommentDTO commentDTO) {
        Comment comment = dtoToEntity(commentDTO);
        repository.save(comment);
        return comment.getComment_no();
    }

    public Comment dtoToEntity(CommentDTO commentDTO){

        Comment comment = Comment.builder()
                .comment_no(commentDTO.getCommentNo())
                .comment(commentDTO.getComment())
                .post(Post.builder().post_no(commentDTO.getPostNo()).build())
                .build();

        return comment;
    }

    public CommentDTO entityToDto(Comment comment){

        CommentDTO commentDTO = CommentDTO.builder()
                .commentNo(comment.getComment_no())
                .comment(comment.getComment())
                .regDate(comment.getRegDate())
                .modDate(comment.getModDate())
                .postNo(comment.getPost().getPost_no())
                .build();

        return commentDTO;
    }


}
