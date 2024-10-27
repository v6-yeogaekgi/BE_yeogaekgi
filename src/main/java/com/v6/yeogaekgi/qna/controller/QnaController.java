package com.v6.yeogaekgi.qna.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.v6.yeogaekgi.qna.dto.QnaDTO;
import com.v6.yeogaekgi.qna.entity.Qna;
import com.v6.yeogaekgi.qna.repository.QnaRepository;
import com.v6.yeogaekgi.qna.service.QnaService;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import com.v6.yeogaekgi.util.PageDTO.PageRequestDTO;
import com.v6.yeogaekgi.util.PageDTO.PageResultDTO;
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

@RestController
@RequestMapping("/qna")
@CrossOrigin(origins = {"*"})
@Log4j2
@RequiredArgsConstructor
public class QnaController {
    private final QnaService service;
    private final QnaRepository repository;
    private final S3Service s3Service;

    @GetMapping("/list")
    public ResponseEntity<PageResultDTO<QnaDTO>> getPostList( @AuthenticationPrincipal MemberDetailsImpl memberDetails,
                                                              PageRequestDTO pageRequestDTO) {
        if(memberDetails == null){
            new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        PageResultDTO<QnaDTO> result = service.getQnaList(pageRequestDTO, memberDetails.getMember());
        if(result != null){
            if(result.getContent().size() == 0){
                return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{qnaNo}")
    public ResponseEntity<QnaDTO> getPost(@PathVariable Long qnaNo,
                                          @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        log.info("-------------- get qna --------------");
        if(memberDetails == null){
            new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        log.info("postNo: " + qnaNo);
        return new ResponseEntity<>(service.getQna(qnaNo), HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<Long> registerQna(
            @RequestPart(value = "multipartFile", required = false) List<MultipartFile> multipartFiles,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam("content") String content,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        // 이미지 업로드 -> url 리스트로 반환 -> List<String>을 JSON 문자열로 변환
        log.info("\n----------------qna Post-------------------");
        String imageUrl = null;
        if(multipartFiles != null && multipartFiles.size() > 0) {
            imageUrl = s3Service.convertListToString(s3Service.uploadImage(multipartFiles));
        }

        Qna qna = Qna.builder().images(imageUrl).title(title).content(content).member(memberDetails.getMember()).build();
        Long no = service.register(qna);
        return new ResponseEntity<>(no, HttpStatus.OK);
    }


    @PutMapping("/{qnaNo}")
    public ResponseEntity<Long> modifyPost(@PathVariable Long qnaNo,
                                           @RequestPart(value = "multipartFile", required = false) List<MultipartFile> multipartFiles,
                                           @RequestParam(value = "existingImages", required = false) List<String> existingImages,  // 기존 이미지 URL 목록
                                           @RequestParam(value = "deleteImages", required = false) List<String> deleteImages,  // 삭제할 이미지 URL 목록
                                           @RequestParam(value = "title", required = false) String title,
                                           @RequestParam("content") String content,
                                           @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        // 새 이미지 업로드 -> url 리스트로 반환
        List<String> newImageUrls = new ArrayList<>();
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            newImageUrls.addAll(s3Service.uploadImage(multipartFiles).stream()
                    .map(map -> map.get("imageUrl"))
                    .toList());
        }

        // 삭제할 이미지 처리
        if (deleteImages != null) {
            for (String imageUrl : deleteImages) {
                s3Service.deleteImage(imageUrl); // S3 서비스에서 이미지 삭제
            }
        }

        // 기존 이미지와 새 이미지 URL 합치기
        if (existingImages != null) {
            newImageUrls.addAll(existingImages);
        }

        // 이미지 URL 리스트를 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String imageUrls = "[]"; // 빈 리스트로 초기화
        try {
            imageUrls = objectMapper.writeValueAsString(newImageUrls);
        } catch (Exception e) {
            log.error("Error converting image URLs to JSON string", e);
        }

        Qna qna = Qna.builder().images(imageUrls).title(title).content(content).member(memberDetails.getMember()).no(qnaNo).build();


        log.info("---------------modify post--------------" + qna.getNo());

        service.modify(qna);

        return new ResponseEntity<>(qna.getNo(), HttpStatus.OK);
    }



    @DeleteMapping("/{qnaNo}")
    public ResponseEntity<Long> removePost(@PathVariable Long qnaNo) {
        log.info("---------------remove post--------------");
        log.info("postNo: " + qnaNo);

        service.remove(qnaNo);

        return new ResponseEntity<>(qnaNo, HttpStatus.OK);
    }
}
