package com.v6.yeogaekgi.community.controller;

import com.v6.yeogaekgi.community.dto.HashtagDTO;
import com.v6.yeogaekgi.community.dto.PostDTO;
import com.v6.yeogaekgi.community.dto.SearchDTO;
import com.v6.yeogaekgi.community.repository.PostLikeRepository;
import com.v6.yeogaekgi.community.repository.PostRepository;
import com.v6.yeogaekgi.community.service.PostLikeService;
import com.v6.yeogaekgi.community.service.PostService;
import com.v6.yeogaekgi.member.dto.MemberResponseDTO;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import com.v6.yeogaekgi.util.S3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/community")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = {"*"})

public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final PostLikeService postLikeService;
    private final PostLikeRepository postLikeRepository;
    private final S3Service s3Service;

    @GetMapping("/list")
    public ResponseEntity<List<PostDTO>> getPostList(SearchDTO dto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        log.info("-------- post list -------- search [Hashtag/Content] = [" + dto.getHashtag() + "/" + dto.getContent() + "]");
        return new ResponseEntity<>(postService.getPostList(dto, memberDetails), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Long> registerPost(
            @RequestPart(value = "multipartFile", required = false) List<MultipartFile> multipartFiles,
            @RequestPart(value = "hashtag", required = false) String hashtag,
            @RequestPart("content") String content,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        // 이미지 업로드 -> url 리스트로 반환 -> List<String>을 JSON 문자열로 변환
        String imageUrl = null;
        if(multipartFiles != null && multipartFiles.size() > 0) {
            imageUrl = s3Service.convertListToString(s3Service.uploadImage(multipartFiles));
        }

        PostDTO postDTO = PostDTO.builder().images(imageUrl).hashtag(hashtag).content(content).build();

        log.info("----------------register Post-------------------");
        log.info("postDTO : " + postDTO);
        Long postId = postService.register(postDTO,memberDetails.getMember());
        return new ResponseEntity<>(postId, HttpStatus.OK);
    }


    @PutMapping("/{postId}")
    public ResponseEntity<Long> modifyPost(@PathVariable("postId") Long postId,
                                           @RequestBody PostDTO postDTO) {
        postDTO.setPostId(postId);
        log.info("---------------modify post--------------" + postId);
        log.info("postDTO: " + postDTO);

        postService.modify(postDTO);

        return new ResponseEntity<>(postId, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Long> removePost(@PathVariable Long postId) {
        log.info("---------------remove post--------------");
        log.info("postId: " + postId);

        postService.remove(postId);

        return new ResponseEntity<>(postId, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long postId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        log.info("--------------get post--------------");
        log.info("postId: " + postId);
        return new ResponseEntity<>(postService.getPost(postId, memberDetails), HttpStatus.OK);
    }

    @GetMapping("/hashtag/{hashtag}")
    public ResponseEntity<List<HashtagDTO>> searchHashtag(@PathVariable String hashtag) {
        log.info("--------------get hashtag list--------------");
        log.info("hashtag keyword: " + hashtag);
        return new ResponseEntity<>(postService.searchHashtag(hashtag), HttpStatus.OK);
    }

    @GetMapping("/likeList")
    public ResponseEntity<?> getLikeList(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        log.info("--------------get Post Like list--------------");
        try{
            return new ResponseEntity<>(postLikeService.getLikeList(memberDetails), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<?> postLikeActive(@PathVariable Long postId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        log.info("-------------- post like on/off --------------");
        try {
            Member member  = memberDetails.getMember();
            return new ResponseEntity<>(postLikeService.postLikeActive(postId, member), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
