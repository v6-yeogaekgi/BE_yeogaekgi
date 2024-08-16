package com.v6.yeogaekgi.repository;

import com.v6.yeogaekgi.community.entity.Comment;
import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.community.repository.CommentRepository;
import com.v6.yeogaekgi.community.repository.PostRepository;
import com.v6.yeogaekgi.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class PostRepositoryTests {

    @Autowired
    private PostRepository repository;


    @Test
    public void insertPosts() {

        //100개 더미 등록
        IntStream.rangeClosed(1,100).forEach(i -> {

            // 회원 번호
            Long memberId = (long)(Math.random()*100) + 1;

            Post post = Post.builder()
                    .title("Post...." +i)
                    .content("Post Content..."+i)
                    .member(Member.builder().id(memberId).build())
                    .build();

            repository.save(post);

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