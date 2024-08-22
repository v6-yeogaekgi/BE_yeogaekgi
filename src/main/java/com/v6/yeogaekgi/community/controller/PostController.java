package com.v6.yeogaekgi.community.controller;

import com.v6.yeogaekgi.community.dto.HashtagDTO;
import com.v6.yeogaekgi.community.dto.PostDTO;
import com.v6.yeogaekgi.community.dto.SearchDTO;
import com.v6.yeogaekgi.community.repository.PostRepository;
import com.v6.yeogaekgi.community.service.PostService;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = {"*"})

public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    @GetMapping("/list")
    public ResponseEntity<List<PostDTO>> getPostList(SearchDTO dto, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        log.info("-------- post list -------- search [Hashtag/Content] = ["+dto.getHashtag()+"/"+dto.getContent()+"]");
        return new ResponseEntity<>(postService.getPostList(dto, memberDetails) ,HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Long> registerPost(@RequestBody PostDTO postDTO){
        log.info("----------------register Post-------------------");
        log.info("postDTO : "+postDTO);
        Long postId = postService.register(postDTO);
        return new ResponseEntity<>(postId,HttpStatus.OK);

    }


    @PutMapping("/{postId}")
    public ResponseEntity<Long> modifyPost(@PathVariable("postId") Long postId,
                                              @RequestBody PostDTO postDTO){
        postDTO.setPostId(postId);
        log.info("---------------modify post--------------" + postId);
        log.info("postDTO: " + postDTO);

        postService.modify(postDTO);

        return new ResponseEntity<>(postId, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Long> removePost(@PathVariable Long postId){
        log.info("---------------remove post--------------");
        log.info("postId: " + postId);

        postService.remove(postId);

        return new ResponseEntity<>(postId, HttpStatus.OK);
    }
    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long postId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        log.info("--------------get post--------------");
        log.info("postId: " + postId);
        return new ResponseEntity<>(postService.getPost(postId, memberDetails), HttpStatus.OK);
    }

    @GetMapping("/hashtag/{hashtag}")
    public ResponseEntity<List<HashtagDTO>> searchHashtag(@PathVariable String hashtag){
        log.info("--------------get hashtag list--------------");
        log.info("hashtag keyword: " + hashtag);
        return new ResponseEntity<>(postService.searchHashtag(hashtag), HttpStatus.OK);
    }

    @GetMapping("/like")
    public ResponseEntity<List<Long>> getLikeList(@AuthenticationPrincipal MemberDetailsImpl memberDetails){
        log.info("--------------get Post Like list--------------");
        return new ResponseEntity<>(postService.getLikeList(memberDetails), HttpStatus.OK);
    }

}
