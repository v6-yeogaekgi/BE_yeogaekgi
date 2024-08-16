//package com.v6.yeogaekgi.community.controller;
//
//import com.v6.yeogaekgi.community.dto.CommentDTO;
//import com.v6.yeogaekgi.community.service.CommentService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/community/comment")
//@Log4j2
//@RequiredArgsConstructor
//public class PostController {
//
//    private final CommentService commentService;
//
//    @GetMapping("/{postId}")
//    public ResponseEntity<List<CommentDTO>> getCommentList(@PathVariable("postId") Long postId) {
//        log.info("--------list--------");
//        log.info("postNo : "+postId);
//
//        List<CommentDTO> commentDTOList = commentService.getListOfComment(postId);
//        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
//    }
//
//    @PostMapping("/{postId}")
//    public ResponseEntity<Long> registerCommnet(@PathVariable("postId") Long postId, @RequestBody CommentDTO commentDTO){
//        commentDTO.setPostId(postId);
//        log.info("----------------register commnet-------------------");
//        log.info("commentDTO : "+commentDTO);
//        Long commentId = commentService.register(commentDTO);
//        return new ResponseEntity<>(commentId,HttpStatus.OK);
//
//    }
//
//    @PutMapping("/{postId}/{commentId}")
//    public ResponseEntity<Long> modifyComment(@PathVariable("commentId") Long commentId,
//                                             @RequestBody CommentDTO commentDTO){
//        commentDTO.setCommentId(commentId);
//        log.info("---------------modify Comment--------------" + commentId);
//        log.info("commentDTO: " + commentDTO);
//
//        commentService.modify(commentDTO);
//
//        return new ResponseEntity<>(commentId, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{postId}/{commentId}")
//    public ResponseEntity<Long> removeComment( @PathVariable Long commentId){
//        log.info("---------------remove comment--------------");
//        log.info("commentId: " + commentId);
//
//        commentService.remove(commentId);
//
//        return new ResponseEntity<>(commentId, HttpStatus.OK);
//    }
//}
