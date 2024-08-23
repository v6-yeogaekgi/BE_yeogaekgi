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
        Long memberId= memberDetails == null ? 0L : memberDetails.getMember().getId();
        return plRepository.findPost_IdByMember_Id(memberId);
    }


    // Like 버튼 눌렀을 때
    @Transactional
    public Map<String, Object> postLikeActive(Long postId, MemberDetailsImpl memberDetails){
        Map<String, Object> data = new HashMap<>();

        Long memberId =  memberDetails.getMember().getId();
        PostLike postLike = PostLike.builder()
                .post(Post.builder().id(postId).build())
                .member(Member.builder().id(memberId).build())
                .build();
        boolean likeState = false;
        int likeCnt = 0;

        Optional<Post> temp = postRepository.findById(postId);
        if(temp.isPresent()) {
            Post post = temp.get();
            likeCnt = post.getLikeCnt();

            if (plRepository.existsByPost_IdAndMember_Id(postId, memberId)) { // 테이블에 값이 있으면
                // delete
                plRepository.deleteByPost_IdAndMember_Id(postId, memberId);
                // Post table LikeCnt update
                post.changeLikeCnt(--likeCnt);
                likeState = false;

            } else { // 테이블에 값이 없으면
                // insert
                plRepository.save(postLike);
                // Post table LikeCnt update
                post.changeLikeCnt(++likeCnt);
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
