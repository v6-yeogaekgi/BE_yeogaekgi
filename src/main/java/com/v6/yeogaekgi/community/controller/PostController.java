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
}
