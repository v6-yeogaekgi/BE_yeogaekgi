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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor

public class CommentService {
    private final CommentRepository repository;
    private final PostRepository postRepository;

    public List<CommentDTO> getListOfComment(Long postNo, Member member) {
        Post post = Post.builder().no(postNo).build();
        List<Comment> result = repository.findByPost(post);
        return result.stream().map(Comment -> entityToDto(Comment,member)).collect(Collectors.toList());

    }

    public CommentDTO getComment(Long commentNo, Member member) {
        Optional<Comment> optionalComment = repository.findById(commentNo);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();

            return entityToDto(comment,member);
        } else return null;

    }
    @Transactional
    public Long register(CommentDTO commentDTO, Member member) {
        Comment comment = dtoToEntity(commentDTO, member);
        // commentCnt + 1
        Optional<Post> result = postRepository.findById(comment.getPost().getNo());
        if (result.isPresent()) {
            Post post = result.get();
            post.changeCommentCnt(post.getCommentCnt()+1);
            postRepository.save(post);
        }
        repository.save(comment);
        return comment.getNo();
    }

    @Transactional
    public void modify(CommentDTO commentDTO) {
        Optional<Comment> result = repository.findById(commentDTO.getCommentNo());
        if (result.isPresent()) {
            Comment comment = result.get();
            comment.changeComment(commentDTO.getContent());
            repository.save(comment);
        }

    }

    @Transactional
    public void remove(Long commentNo, Long postNo) {

        // commentCnt - 1
        Optional<Post> result = postRepository.findById(postNo);
        if (result.isPresent()) {
            Post post = result.get();
            post.changeCommentCnt(post.getCommentCnt()-1);
            postRepository.save(post);
        }
        repository.deleteById(commentNo);

    }


    public Comment dtoToEntity(CommentDTO commentDTO, Member member) {

        Comment comment = Comment.builder()
                .no(commentDTO.getCommentNo())
                .content(commentDTO.getContent())
                .post(Post.builder().no(commentDTO.getPostNo()).build())
                .member(member)
                .build();

        return comment;
    }

    public CommentDTO entityToDto(Comment comment, Member member) {

        CommentDTO commentDTO = CommentDTO.builder()
                .commentNo(comment.getNo())
                .content(comment.getContent())
                .regDate(comment.getRegDate())
                .modDate(comment.getModDate())
                .postNo(comment.getPost().getNo())
                .memberNo(comment.getMember().getNo())
                .nickname(comment.getMember().getNickname())
                .code(comment.getMember().getCountry().getCode())
                .currentMemberNo(member.getNo())
                .currentMemberCode(member.getCountry().getCode())
                .build();

        return commentDTO;
    }


}