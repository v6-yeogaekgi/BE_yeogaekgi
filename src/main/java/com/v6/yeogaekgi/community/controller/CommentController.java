package com.v6.yeogaekgi.community.controller;

import com.v6.yeogaekgi.community.dto.CommentDTO;
import com.v6.yeogaekgi.community.service.CommentService;
import com.v6.yeogaekgi.kiosk.dto.KioskRequestDTO;
import com.v6.yeogaekgi.kiosk.dto.KioskResponseDTO;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/community/comment")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = {"*"})
public class CommentController {

    private final CommentService commentService;

    @GetMapping("all/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentList(@PathVariable("postId") Long postId,@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        log.info("--------list--------");
        log.info("postNo : "+postId);

        List<CommentDTO> commentDTOList = commentService.getListOfComment(postId,memberDetails.getMember());
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable("commentId") Long commentId,@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        log.info("--------one comment--------");
        log.info("commentNo : "+commentId);

        CommentDTO commentDTO = commentService.getComment(commentId,memberDetails.getMember());
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    @PostMapping("/{postId}")
    public ResponseEntity<Long> registerComment(@PathVariable("postId") Long postId, @RequestBody CommentDTO commentDTO,@AuthenticationPrincipal MemberDetailsImpl memberDetails){
        commentDTO.setPostId(postId);
        log.info("----------------register commnet-------------------");
        log.info("commentDTO : "+commentDTO);
        Long commentId = commentService.register(commentDTO,memberDetails.getMember());
        return new ResponseEntity<>(commentId,HttpStatus.OK);

    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Long> modifyComment(@PathVariable("commentId") Long commentId,
                                             @RequestBody CommentDTO commentDTO, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        commentDTO.setCommentId(commentId);
        log.info("---------------modify Comment--------------" + commentId);
        log.info("commentDTO: " + commentDTO);

        commentService.modify(commentDTO);

        return new ResponseEntity<>(commentId, HttpStatus.OK);
    }


    @DeleteMapping("/{postId}/{commentId}")
    public ResponseEntity<Long> removeComment( @PathVariable Long commentId, @PathVariable Long postId){
        log.info("---------------remove comment--------------");
        log.info("commentId: " + commentId);
        log.info("postId: " + postId);



        commentService.remove(commentId,postId);


        return new ResponseEntity<>(commentId, HttpStatus.OK);
    }
}
