package com.v6.yeogaekgi.community.service;

import com.v6.yeogaekgi.community.dto.PostDTO;
import com.v6.yeogaekgi.community.dto.PostLikeDTO;
import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.community.entity.PostLike;
import com.v6.yeogaekgi.community.repository.PostLikeRepository;
import com.v6.yeogaekgi.community.repository.PostRepository;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.member.repository.MemberRepository;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository plRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    // 내가 좋아요한 POST의 ID list
    public List<Long> getLikeList(MemberDetailsImpl memberDetails) {
        Long memberNo= memberDetails == null ? 0L : memberDetails.getMember().getNo();
        return plRepository.findPost_IdByMember_Id(memberNo);
    }


    // Like 버튼 눌렀을 때
    @Transactional
    public Map<String, Object> postLikeActive(Long postNo, Member member){
        Map<String, Object> data = new HashMap<>();

        Long memberNo =  member.getNo();
        boolean likeState = false;
        int likeCnt = 0;

        Optional<Post> temp = postRepository.findById(postNo);
        if(temp.isPresent()) {
            Post post = temp.get();
            likeCnt = post.getLikeCnt();

            if (plRepository.existsByPost_IdAndMember_Id(postNo, memberNo)) { // 테이블에 값이 있으면 delete
                plRepository.deleteByPost_IdAndMember_Id(postNo, memberNo);
                post.changeLikeCnt(--likeCnt); // Post table LikeCnt update
                likeState = false;

            } else { // 테이블에 값이 없으면 insert
                plRepository.save(PostLike.builder()
                        .post(Post.builder().no(postNo).build())
                        .member(Member.builder().no(memberNo).build())
                        .build());
                post.changeLikeCnt(++likeCnt); // Post table LikeCnt update
                likeState = true;
            }
            postRepository.save(post);
        }

        // 데이터 추가
        data.put("likeCnt", likeCnt);
        data.put("state", likeState);
        return data;
    }
}
