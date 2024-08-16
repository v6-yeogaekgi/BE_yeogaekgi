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
    @Autowired
    private PostRepository postRepository;



    @Test
    // 더미 등록
    public void insertPosts() {
        int number = 10; // 더미 등록할 개수
        int max_member_no = 2; // member_no 1에서 몇 까지 등록할 건지
        IntStream.rangeClosed(1,number).forEach(i->{
            long mno = (long)(Math.random()*max_member_no)+1; // member
            Member member = Member.builder().id(mno).build();
            Post post = Post.builder()
                    .content("Post content......"+i)
                    .member(member)
                    .build();

            postRepository.save(post);
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