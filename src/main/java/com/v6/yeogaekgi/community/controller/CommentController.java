package com.v6.yeogaekgi.community.controller;

import com.v6.yeogaekgi.community.dto.CommentDTO;
import com.v6.yeogaekgi.community.service.CommentService;
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

    // cicd test

    private final CommentService commentService;

    @GetMapping("all/{postNo}")
    public ResponseEntity<List<CommentDTO>> getCommentList(@PathVariable("postNo") Long postNo, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        log.info("--------list--------");
        log.info("postNo : "+ postNo);

        List<CommentDTO> commentDTOList = commentService.getListOfComment(postNo,memberDetails.getMember());
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @GetMapping("/{commentNo}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable("commentNo") Long commentNo, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        log.info("--------one comment--------");
        log.info("commentNo : "+ commentNo);

        CommentDTO commentDTO = commentService.getComment(commentNo,memberDetails.getMember());
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    @PostMapping("/{postNo}")
    public ResponseEntity<Long> registerComment(@PathVariable("postNo") Long postNo, @RequestBody CommentDTO commentDTO, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        commentDTO.setPostNo(postNo);
        log.info("----------------register comment-------------------");
        log.info("commentDTO : "+commentDTO);
        Long commentNo = commentService.register(commentDTO,memberDetails.getMember());
        return new ResponseEntity<>(commentNo,HttpStatus.OK);

    }

    @PutMapping("/{commentNo}")
    public ResponseEntity<Long> modifyComment(@PathVariable("commentNo") Long commentNo,
                                             @RequestBody CommentDTO commentDTO){
        commentDTO.setCommentNo(commentNo);
        log.info("---------------modify Comment--------------" + commentNo);
        log.info("commentDTO: " + commentDTO);

        commentService.modify(commentDTO);

        return new ResponseEntity<>(commentNo, HttpStatus.OK);
    }


    @DeleteMapping("/{postNo}/{commentNo}")
    public ResponseEntity<Long> removeComment(@PathVariable Long commentNo, @PathVariable Long postNo){
        log.info("---------------remove comment--------------");
        log.info("commentNo: " + commentNo);
        log.info("postNo: " + postNo);



        commentService.remove(commentNo, postNo);


        return new ResponseEntity<>(commentNo, HttpStatus.OK);
    }
}
