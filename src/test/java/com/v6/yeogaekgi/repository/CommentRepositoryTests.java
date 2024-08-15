package com.v6.yeogaekgi.repository;

import com.v6.yeogaekgi.community.entity.Comment;
import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.community.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.stream.IntStream;

@SpringBootTest
public class CommentRepositoryTests {

    @Autowired
    private CommentRepository repository;


    @Test
    public void insertComments() {

        //200개의 리뷰를 등록
        IntStream.rangeClosed(1,200).forEach(i -> {

            //post 번호
            Long postNo = (long)(Math.random()*100) + 1;

            Comment comment = Comment.builder()
                    .comment("댓글..." + i)
                    .post(Post.builder().id(postNo).build())
                    .build();

            repository.save(comment);
        });
    }

//    @Transactional
//    @Test
//    public void testGetMovieReviews() {
//        Movie movie = Movie.builder().mno(92L).build();
//        List<Review> result = reviewRepository.findByMovie(movie);
//
//        result.forEach(movieReview ->{
//            System.out.println(movieReview.getReviewnum());
//            System.out.println("\t"+movieReview.getGrade());
//            System.out.println("\t"+movieReview.getText());
//            System.out.println("\t"+movieReview.getMember().getEmail());
//            System.out.println("------------------------------------------");
//
//        });
//    }



}