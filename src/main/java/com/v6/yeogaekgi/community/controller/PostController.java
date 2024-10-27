package com.v6.yeogaekgi.community.controller;

import com.v6.yeogaekgi.community.dto.HashtagDTO;
import com.v6.yeogaekgi.community.dto.PostDTO;
import com.v6.yeogaekgi.community.dto.SearchDTO;
import com.v6.yeogaekgi.community.repository.PostLikeRepository;
import com.v6.yeogaekgi.community.repository.PostRepository;
import com.v6.yeogaekgi.community.service.PostLikeService;
import com.v6.yeogaekgi.community.service.PostService;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import com.v6.yeogaekgi.util.PageDTO.PageResultDTO;
import com.v6.yeogaekgi.util.S3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;


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
    public ResponseEntity<PageResultDTO<PostDTO>> getPostList(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            SearchDTO search) {
        PageResultDTO<PostDTO> result = postService.getPostList(search, memberDetails.getMember());
        if (result != null) {
            if (result.getContent().size() == 0) {
                return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/register")
    public ResponseEntity<Long> registerPost(
            @RequestPart(value = "multipartFile", required = false) MultipartFile multipartFile,
            @RequestParam(value = "hashtag", required = false) String hashtag,
            @RequestParam("content") String content,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        PostDTO postDTO = PostDTO.builder().zip(multipartFile).hashtag(hashtag).content(content).build();

        log.info("\n----------------register Post-------------------");
        log.info("postDTO : " + postDTO);
        Long postNo = postService.register(postDTO, memberDetails.getMember());
        return new ResponseEntity<>(postNo, HttpStatus.OK);
    }


    @PutMapping("/{postNo}")
    public ResponseEntity<Long> modifyPost(@PathVariable Long postNo,
                                           @RequestPart(value = "multipartFile", required = false) MultipartFile multipartFile,
                                           @RequestParam(value = "existingImages", required = false) List<String> existingImages,  // 기존 이미지 URL 목록
                                           @RequestParam(value = "deleteImages", required = false) List<String> deleteImages,  // 삭제할 이미지 URL 목록
                                           @RequestPart(value = "hashtag", required = false) String hashtag,
                                           @RequestPart("content") String content,
                                           @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        PostDTO postDTO = PostDTO.builder().postNo(postNo).zip(multipartFile).existingImages(existingImages).deleteImages(deleteImages).hashtag(hashtag).content(content).build();
        log.info("---------------modify post--------------");
        log.info("postDTO: " + postDTO);
        Long modifiedPostNo = postService.modify(postDTO,memberDetails.getMember());
        return new ResponseEntity<>(modifiedPostNo, HttpStatus.OK);
    }


    @DeleteMapping("/{postNo}")
    public ResponseEntity<Long> removePost(@PathVariable Long postNo) {
        log.info("---------------remove post--------------");
        log.info("postNo: " + postNo);
        postService.remove(postNo);
        return new ResponseEntity<>(postNo, HttpStatus.OK);
    }

    @GetMapping("/{postNo}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long postNo,
                                           @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return new ResponseEntity<>(postService.getPost(postNo, memberDetails == null ? null : memberDetails.getMember()), HttpStatus.OK);
    }

    @GetMapping("/hashtag/{hashtag}")
    public ResponseEntity<List<HashtagDTO>> searchHashtag(@PathVariable String hashtag) {
        return new ResponseEntity<>(postService.searchHashtag(hashtag), HttpStatus.OK);
    }

    @GetMapping("/likeList")
    public ResponseEntity<?> getLikeList(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        try {
            return new ResponseEntity<>(postLikeService.getLikeList(memberDetails), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/like/{postNo}")
    public ResponseEntity<?> postLikeActive(@PathVariable Long postNo,
                                            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        log.info("-------------- post like on/off --------------");
        try {
            Member member = memberDetails.getMember();
            return new ResponseEntity<>(postLikeService.postLikeActive(postNo, member), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}