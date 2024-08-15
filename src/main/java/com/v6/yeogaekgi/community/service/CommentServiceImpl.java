package com.v6.yeogaekgi.community.service;


import com.v6.yeogaekgi.community.dto.CommentDTO;
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

        Post post = Post.builder().mno(mno).build();
        List<Review> result = reviewRepository.findByMovie(movie);
        return result.stream().map(movieReview -> entityToDto(movieReview)).collect(Collectors.toList());

    }

    public Long register(ReviewDTO movieReviewDTO) {
        Review moviewReview = dtoToEntity(movieReviewDTO);
        reviewRepository.save(moviewReview);
        return moviewReview.getReviewnum();
    }


}
