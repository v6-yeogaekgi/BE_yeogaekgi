package com.v6.yeogaekgi.repository;

import com.v6.yeogaekgi.community.entity.Comment;
import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.community.repository.CommentRepository;
import com.v6.yeogaekgi.community.repository.PostRepository;
import com.v6.yeogaekgi.member.entity.Gender;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.member.repository.CountryRepository;
import com.v6.yeogaekgi.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;


import java.util.Optional;
import java.util.stream.IntStream;


@SpringBootTest
public class CommentRepositoryTests {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void insertComments() {

        // 200개의 Comment 더미 데이터를 등록합니다.
        IntStream.rangeClosed(1, 200).forEach(i -> {

            // 회원 번호
            Long memberId = (long) (Math.random() * 10) + 1;

            // post 번호
            Long postNo = (long) (Math.random() * 10) + 1;


            // Comment 객체 생성
            Comment comment = Comment.builder()
                    .comment("댓글..." + i)
                    .post(Post.builder().id(postNo).build())
                    .member(Member.builder().id(memberId).build()) // Comment에 member 설정
                    .build();

            // Comment 저장
            commentRepository.save(comment);

        });
    }
}
