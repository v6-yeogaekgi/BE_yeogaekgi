package com.v6.yeogaekgi.repository;

import com.v6.yeogaekgi.community.dto.HashtagDTO;
import com.v6.yeogaekgi.community.entity.Comment;
import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.community.entity.PostLike;
import com.v6.yeogaekgi.community.repository.CommentRepository;
import com.v6.yeogaekgi.community.repository.PostLikeRepository;
import com.v6.yeogaekgi.community.repository.PostRepository;
import com.v6.yeogaekgi.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class PostRepositoryTests {

    private static final Logger log = LoggerFactory.getLogger(PostRepositoryTests.class);
//    @Autowired
//    private PostRepository repository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostLikeRepository postLikeRepository;


//    @Test
//    // 더미 등록
//    public void insertPosts() {
//        int number = 10; // 더미 등록할 개수
//        int max_member_no = 2; // member_no 1에서 몇 까지 등록할 건지
//        IntStream.rangeClosed(1,number).forEach(i->{
//            long mno = (long)(Math.random()*max_member_no)+1; // member
//            Member member = Member.builder().id(mno).build();
//            Post post = Post.builder()
//                    .content("Post content......"+i)
//                    .member(member)
//                    .build();
//
//            postRepository.save(post);
//        });
//    }


    @Test
    public void searchHashtag(){
//        log.info("================== 해시태그 검색 ==================");
//        List<HashtagDTO> results = postRepository.getHashtag("test");
//
//        log.info("ㄴTotal elements: {}", results);
//        log.info("ㄴTotal pages: {}", results);
    }

    @Test
    public void GetAllList(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

        Page<Post> resultPage = postRepository.findAll(pageable);
        for (Post row : resultPage.getContent()) {
            log.info("ㄴRow data: {}", row.toString());
        }
        log.info("ㄴTotal elements: {}", resultPage.getTotalElements());
        log.info("ㄴTotal pages: {}", resultPage.getTotalPages());
    }

    @Test
    public void GetSearchContentList(){
        Pageable pageable = PageRequest.of(0, 10);

        Page<Post> resultPage = postRepository.findByContentContaining("1" ,pageable);
        for (Post row : resultPage.getContent()) {
            log.info("ㄴRow data: {}", row.toString());
        }
        log.info("ㄴTotal elements: {}", resultPage.getTotalElements());
        log.info("ㄴTotal pages: {}", resultPage.getTotalPages());
    }

    @Test
    public void GetSearchHashtaglist(){
        Pageable pageable = PageRequest.of(0, 10);

        Page<Post> resultPage = postRepository.findByHashtag("test" ,pageable);
        for (Post row : resultPage.getContent()) {
            log.info("ㄴRow data: {}", row.toString());
        }
        log.info("ㄴTotal elements: {}", resultPage.getTotalElements());
        log.info("ㄴTotal pages: {}", resultPage.getTotalPages());
    }

    @Test
    public void GetMyPostlist(){
        Pageable pageable = PageRequest.of(0, 10);

        Page<Post> resultPage = postRepository.findByMember_Id(1L ,pageable);
        for (Post row : resultPage.getContent()) {
            log.info("ㄴRow data: {}", row.toString());
        }
        log.info("ㄴTotal elements: {}", resultPage.getTotalElements());
        log.info("ㄴTotal pages: {}", resultPage.getTotalPages());
    }
    @Test
    public void GetMyLikelist(){
        List<Long> result = postLikeRepository.findPost_IdByMember_Id(1L );
        log.info(result.toString());
    }

//    @Transactional
//    @Test
//    public void insertPosts() {
//
//        //100개 더미 등록
//        IntStream.rangeClosed(1,100).forEach(i -> {
//
//            Post post = Post.builder()
//                    .title("Post...." +i)
//                    .content("Content..."+i)
//                    .build();
//
//            repository.save(post);
//
//        });
//    }
//
////    @Transactional
////    @Test
////    public void testGetMovieReviews() {
////        Movie movie = Movie.builder().mno(92L).build();
////        List<Review> result = reviewRepository.findByMovie(movie);
////
////        result.forEach(movieReview ->{
////            System.out.println(movieReview.getReviewnum());
////            System.out.println("\t"+movieReview.getGrade());
////            System.out.println("\t"+movieReview.getText());
////            System.out.println("\t"+movieReview.getMember().getEmail());
////            System.out.println("------------------------------------------");
////
////        });
////    }
//
//
//
}