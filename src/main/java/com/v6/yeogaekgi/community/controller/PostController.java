package com.v6.yeogaekgi.community.controller;

import com.v6.yeogaekgi.community.dto.CommentDTO;
import com.v6.yeogaekgi.community.dto.PostDTO;
import com.v6.yeogaekgi.community.dto.PostRequestDTO;
import com.v6.yeogaekgi.community.service.CommentService;
import com.v6.yeogaekgi.community.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community")
@Log4j2
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/list")
    public ResponseEntity<List<PostDTO>> get(PostRequestDTO dto) {
        // @RequestHeader("Authorization") String token
        // 인증 헤더에서 JWT 토큰 추출
        // String jwtToken = token.replace("Bearer ", "");
        log.info("--------list--------");
        log.info("request search [Hashtag/Content] = ["+dto.getHashtag()+"/"+dto.getContent()+"] My Posts Filter = "+dto.getMyPost());
        // String jwtToken = token.replace("Bearer ", "");

        return new ResponseEntity<>(postService.getList(dto, null) ,HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Long> registerPost(@RequestBody PostDTO postDTO){
        log.info("----------------register Post-------------------");
        log.info("postDTO : "+postDTO);
        Long postId = postService.register(postDTO);
        return new ResponseEntity<>(postId,HttpStatus.OK);

    }

    @PutMapping("/{postId}")
    public ResponseEntity<Long> modifyComment(@PathVariable("postId") Long postId,
                                              @RequestBody PostDTO postDTO){
        postDTO.setPostId(postId);
        log.info("---------------modify post--------------" + postId);
        log.info("postDTO: " + postDTO);

        postService.modify(postDTO);

        return new ResponseEntity<>(postId, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Long> removeComment(@PathVariable Long postId){
        log.info("---------------remove post--------------");
        log.info("postId: " + postId);

        postService.remove(postId);

        return new ResponseEntity<>(postId, HttpStatus.OK);
    }
}
