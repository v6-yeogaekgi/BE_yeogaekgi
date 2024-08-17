package com.v6.yeogaekgi.util.S3;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service awsS3Service;

    @PostMapping("")
    public ResponseEntity<List<Map<String, String>>> uploadImage(@RequestParam List<MultipartFile> multipartFile) {
        return new ResponseEntity<>(awsS3Service.uploadImage(multipartFile), HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteImage(@RequestParam String fileName) {
        awsS3Service.deleteImage(fileName);
        return new ResponseEntity<>(fileName,HttpStatus.OK);
    }
}