package com.v6.yeogaekgi.repository;

import com.v6.yeogaekgi.community.dto.HashtagDTO;
import com.v6.yeogaekgi.community.entity.Comment;
import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.community.repository.CommentRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class PostRepositoryTests {

    private static final Logger log = LoggerFactory.getLogger(PostRepositoryTests.class);
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
    @Test // ==================전체 게시글 조회 ==================
    public void getAllPostsTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Object[]> resultPage = postRepository.getAllPosts(1L, pageable);
        log.info("==================전체 게시글 조회==================");
        for (Object[] row : resultPage.getContent()) {
            log.info("ㄴRow data: {}", Arrays.toString(row));
        }
        log.info("ㄴTotal elements: {}", resultPage.getTotalElements());
        log.info("ㄴTotal pages: {}", resultPage.getTotalPages());
    }

    @Test // ==================내용 검색 게시글 조회 ==================
    public void getfindPostsByContentTest(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Object[]> resultPage = postRepository.getfindPostsByContent(1L, "1", pageable);
        log.info("==================내용 검색 게시글 조회 ==================");
        for (Object[] row : resultPage.getContent()) {
            log.info("ㄴRow data: {}", Arrays.toString(row));
        }
        log.info("ㄴTotal elements: {}", resultPage.getTotalElements());
        log.info("ㄴTotal pages: {}", resultPage.getTotalPages());
    }

    @Test // ==================해시태그 검색 게시글 조회 ==================
    public void getfindPostsByHashtagTest(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Object[]> resultPage = postRepository.getfindPostsByHashtag(1L, "test", pageable);
        log.info("==================해시태그 검색 게시글 조회 ==================");
        for (Object[] row : resultPage.getContent()) {
            log.info("ㄴRow data: {}", Arrays.toString(row));
        }
        log.info("ㄴTotal elements: {}", resultPage.getTotalElements());
        log.info("ㄴTotal pages: {}", resultPage.getTotalPages());


    }

    @Test // ==================내가 쓴 게시글 조회 ==================
    public void getMemberPostsTest(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Object[]> resultPage = postRepository.getMemberPosts(1L, pageable);
        log.info("==================내가 쓴 게시글 조회 ==================");
        for (Object[] row : resultPage.getContent()) {
            log.info("ㄴRow data: {}", Arrays.toString(row));
        }
        log.info("ㄴTotal elements: {}", resultPage.getTotalElements());
        log.info("ㄴTotal pages: {}", resultPage.getTotalPages());
    }

    @Test
    public void searchHashtag(){
//        log.info("================== 해시태그 검색 ==================");
//        List<HashtagDTO> results = postRepository.getHashtag("test");
//
//        log.info("ㄴTotal elements: {}", results);
//        log.info("ㄴTotal pages: {}", results);
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