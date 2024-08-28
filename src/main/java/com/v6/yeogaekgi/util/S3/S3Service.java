package com.v6.yeogaekgi.util.S3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.v6.yeogaekgi.review.dto.ReviewUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import net.coobird.thumbnailator.Thumbnailator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public List<Map<String, String>> uploadImage(List<MultipartFile> multipartFiles) {
        List<Map<String, String>> fileUrlList = new ArrayList<>();

        multipartFiles.forEach(file -> {
            String originalFileName = file.getOriginalFilename();
            String fileName = createFileName(originalFileName);
            String thumbnailFileName = createThumbnailFileName(fileName);

            // 원본 파일 메타데이터 설정
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                // 원본 파일 업로드
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                // 업로드한 원본 파일의 URL 가져오기
                String fileUrl = amazonS3.getUrl(bucket, fileName).toString();

                // 썸네일 생성 및 업로드
                byte[] thumbnailBytes = createThumbnail(file.getInputStream());
                ObjectMetadata thumbnailMetadata = new ObjectMetadata();
                thumbnailMetadata.setContentLength(thumbnailBytes.length);
                thumbnailMetadata.setContentType(file.getContentType());

                amazonS3.putObject(new PutObjectRequest(bucket, thumbnailFileName,
                        new ByteArrayInputStream(thumbnailBytes), thumbnailMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                // 업로드한 썸네일 파일의 URL 가져오기
                String thumbnailUrl = amazonS3.getUrl(bucket, thumbnailFileName).toString();

                // 원본 파일 이름, 이미지 URL, 썸네일 URL을 Map에 추가
                Map<String, String> fileUrlMap = new HashMap<>();
                fileUrlMap.put("originalFileName", originalFileName);  // 파일 이름 추가
                fileUrlMap.put("imageUrl", fileUrl);
                fileUrlMap.put("thumbnailUrl", thumbnailUrl);

                // 리스트에 추가
                fileUrlList.add(fileUrlMap);

            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
            }
        });

        return fileUrlList;
    }



    public void deleteImage(String fileName) {
        System.out.println("Deleting file: " + fileName);
        // 원본 이미지 삭제
        String objectKey = extractObjectKeyFromUrl(fileName);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, objectKey));

        // 썸네일 삭제
//        String thumbnailFileName = createThumbnailFileName(fileName);
        String thumbnailFileName = createThumbnailFileName(objectKey);
        System.out.println("Deleting thumbnail: " + thumbnailFileName);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, thumbnailFileName));
    }

    private String extractObjectKeyFromUrl(String url){
        try {
            return new java.net.URL(url).getPath().substring(1);
        } catch (Exception e) {
            throw new RuntimeException("객체 키 추출 오류: " + url, e);
        }
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String createThumbnailFileName(String fileName) {
        return "s_" + fileName;
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }

    private byte[] createThumbnail(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnailator.createThumbnail(inputStream, outputStream, 100, 100);
        return outputStream.toByteArray();
    }

    public List<String> convertStringToList(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return new ObjectMapper().readValue(jsonString, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("변환 중 오류 발생", e);
        }
    }

    public String convertListToString(List<Map<String, String>> list) {
        // List<Map<String, String>>에서 "imageUrl" 값만 추출하여 List<String>으로 변환
        List<String> imageUrls = list.stream()
                .map(map -> map.get("imageUrl"))
                .collect(Collectors.toList());

        // List<String>을 JSON 문자열로 변환
        try {
            return new ObjectMapper().writeValueAsString(imageUrls);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("변환 중 오류 발생", e);
        }
    }
    public String convertListToString2(List<String> list) {
        // List<String>을 JSON 문자열로 변환
        try {
            return new ObjectMapper().writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("변환 중 오류 발생", e);
        }
    }


}
