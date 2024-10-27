package com.v6.yeogaekgi.services.controller;

import com.v6.yeogaekgi.security.MemberDetailsImpl;
import com.v6.yeogaekgi.services.dto.ServicesLikeDTO;
import com.v6.yeogaekgi.services.service.ServicesLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/servicesLike")
@Log4j2
@RequiredArgsConstructor
public class ServicesLikeController {
    private final ServicesLikeService servicesLikeService;

    @GetMapping("/list")
    public ResponseEntity<List<ServicesLikeDTO>> userLikeList(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return new ResponseEntity<>(servicesLikeService.findAllServiceLike(memberDetails.getMember().getNo()), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{servicesLikeNo}")
    public ResponseEntity<String> deleteUserLike(@PathVariable Long servicesLikeNo) {
        servicesLikeService.deleteByNo(servicesLikeNo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
