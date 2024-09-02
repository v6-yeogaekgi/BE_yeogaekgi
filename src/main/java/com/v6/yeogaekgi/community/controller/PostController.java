package com.v6.yeogaekgi.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.v6.yeogaekgi.community.dto.HashtagDTO;
import com.v6.yeogaekgi.community.dto.PostDTO;
import com.v6.yeogaekgi.community.dto.SearchDTO;
import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.community.repository.PostLikeRepository;
import com.v6.yeogaekgi.community.repository.PostRepository;
import com.v6.yeogaekgi.community.service.PostLikeService;
import com.v6.yeogaekgi.community.service.PostService;
import com.v6.yeogaekgi.member.dto.MemberResponseDTO;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import com.v6.yeogaekgi.util.PageDTO.PageRequestDTO;
import com.v6.yeogaekgi.util.PageDTO.PageResultDTO;
import com.v6.yeogaekgi.util.S3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

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
        log.info("\n-------- post list --------\n[" + search.toString() + "]");
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
        Long postId = postService.register(postDTO, memberDetails.getMember());
        return new ResponseEntity<>(postId, HttpStatus.OK);
    }


    @PutMapping("/{postId}")
    public ResponseEntity<Long> modifyPost(@PathVariable Long postId,
                                           @RequestPart(value = "multipartFile", required = false) MultipartFile multipartFile,
                                           @RequestParam(value = "existingImages", required = false) List<String> existingImages,  // 기존 이미지 URL 목록
                                           @RequestParam(value = "deleteImages", required = false) List<String> deleteImages,  // 삭제할 이미지 URL 목록
                                           @RequestPart(value = "hashtag", required = false) String hashtag,
                                           @RequestPart("content") String content,
                                           @AuthenticationPrincipal MemberDetailsImpl memberDetails) {



        PostDTO postDTO = PostDTO.builder().postId(postId).zip(multipartFile).existingImages(existingImages).deleteImages(deleteImages).hashtag(hashtag).content(content).build();

        log.info("---------------modify post--------------");
        log.info("postDTO: " + postDTO);

        Long modifiedPostId = postService.modify(postDTO,memberDetails.getMember());


        return new ResponseEntity<>(modifiedPostId, HttpStatus.OK);
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<Long> removePost(@PathVariable Long postId) {
        log.info("---------------remove post--------------");
        log.info("postId: " + postId);

        postService.remove(postId);

        return new ResponseEntity<>(postId, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long
                                                   postId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        log.info("--------------get post--------------");
        log.info("postId: " + postId);
        return new ResponseEntity<>(postService.getPost(postId, memberDetails == null ? null : memberDetails.getMember()), HttpStatus.OK);
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
        try {
            return new ResponseEntity<>(postLikeService.getLikeList(memberDetails), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<?> postLikeActive(@PathVariable Long
                                                    postId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        log.info("-------------- post like on/off --------------");
        try {
            Member member = memberDetails.getMember();
            return new ResponseEntity<>(postLikeService.postLikeActive(postId, member), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}