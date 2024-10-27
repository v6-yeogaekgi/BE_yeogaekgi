package com.v6.yeogaekgi.community.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.v6.yeogaekgi.community.dto.HashtagDTO;
import com.v6.yeogaekgi.community.dto.PostDTO;
import com.v6.yeogaekgi.community.dto.SearchDTO;
import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.community.entity.PostLike;
import com.v6.yeogaekgi.community.repository.PostLikeRepository;
import com.v6.yeogaekgi.community.repository.PostRepository;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.util.InputStreamMultipartFile;
import com.v6.yeogaekgi.util.PageDTO.PageRequestDTO;
import com.v6.yeogaekgi.util.PageDTO.PageResultDTO;
import com.v6.yeogaekgi.util.S3.S3Service;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import java.util.zip.ZipFile;

import org.springframework.data.domain.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

@Service
@Log4j2
@RequiredArgsConstructor

public class PostService {
    private final PostRepository repository;
    private final PostLikeRepository plRepository;
    private final S3Service s3Service;


    // 게시글 리스트
    public PageResultDTO<PostDTO> getPostList(SearchDTO search, Member member) {
        Slice<Post> result = null;
        Pageable pageable = PageRequest.of(search.getPage(), 10, Sort.by(Sort.Direction.DESC, "no"));
        if ("content".equals(search.getType())) { // 내용 검색
            result = repository.findByContentContaining(search.getKeyword(), pageable);
        }
        if ("hashtag".equals(search.getType())) { // 해시태그 검색
            result = repository.findByHashtag(search.getKeyword(), pageable);
        }
        if (search.getMyPost()) { // 내가 작성한 게시글
            result = repository.findByMember_Id(member.getNo(), pageable);
        }

        if (result != null) {
            List<PostDTO> content = result.getContent().stream()
                    .map(Post -> entityToDto(Post, member))
                    .collect(Collectors.toList());
            PageResultDTO<PostDTO> pageResultDTO = new PageResultDTO<>(content, pageable, result.hasNext());
            return pageResultDTO;
        }

        return null;
    }

    // 게시글 상세
    public PostDTO getPost(Long postNo, Member member) {
        Long memberNo = member == null ? 0L : member.getNo(); // memberNo 가져옴
        Optional<Post> post = repository.findById(postNo);
        PostDTO postDto = null;
        if (post.isPresent()) {
            postDto = entityToDto(post.get(), member);
            postDto.setLikeState(plRepository.existsByPost_IdAndMember_Id(postNo, memberNo));
        }
        return postDto;
    }

    // 게시글 등록
    public Long register(PostDTO postDTO, Member member) {

        MultipartFile multipartFile = postDTO.getZip();
        postDTO.setImages(uploadZipFile(multipartFile));
        Post post = dtoToEntity(postDTO, member);
        repository.save(post);
        return post.getNo();
    }

    // 게시글 수정
    public Long modify(PostDTO postDTO, Member member) {

        // 게시글 조회
        Post post = repository.findById(postDTO.getPostNo())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        List<String> newImageUrls = new ArrayList<>();
        MultipartFile multipartFile = postDTO.getZip();


        // 새 이미지 업로드 -> url 리스트로 반환
        if (multipartFile != null && !multipartFile.isEmpty()) {
            newImageUrls = uploadZipFile(multipartFile);
        }

        // S3 삭제할 이미지 처리
        if (postDTO.getDeleteImages() != null) {
            for (String imageUrl : postDTO.getDeleteImages()) {
                s3Service.deleteImage(imageUrl); // S3 서비스에서 이미지 삭제
            }
        }

        // 새 이미지 URL들을 먼저 추가
        List<String> combinedImageUrls = new ArrayList<>();

        // 기존 이미지 URL들을 새 이미지 URL들 뒤에 추가
        if (postDTO.getExistingImages() != null) {
            combinedImageUrls.addAll(postDTO.getExistingImages());
        }
        combinedImageUrls.addAll(newImageUrls);

        postDTO.setImages(combinedImageUrls);
        post.changeContent(postDTO.getContent());
        post.changeImages(s3Service.convertListToString2(postDTO.getImages()));
        post.changeHashtag(postDTO.getHashtag());

        repository.save(post);
        return post.getNo();
    }


    // 게시글 삭제
    public void remove(Long postNo) {


        // 게시글 조회
        Post post = repository.findById(postNo)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // S3 삭제할 이미지 처리
        if (post.getImages() != null) {
            List<String> imageUrls = s3Service.convertStringToList(post.getImages());

            for (String imageUrl : imageUrls) {
                s3Service.deleteImage(imageUrl); // S3 서비스에서 이미지 삭제
            }
        }

        repository.deleteById(postNo);
    }

    // 해시태그 검색 list
    public List<HashtagDTO> searchHashtag(String keyword) {
        List<Object[]> results = repository.getHashtag(keyword);
        List<HashtagDTO> hashtags = new ArrayList<>();

        for (Object[] result : results) {
            String hashtag = (String) result[0];
            int count = ((Number) result[1]).intValue();
            hashtags.add(new HashtagDTO(hashtag, count));
        }
        return hashtags;
    }


    // 압축해제 및 업로드
    public List<String> uploadZipFile(MultipartFile zipFile) {
        if (zipFile == null || zipFile.isEmpty()) {
            return null;
        }

        File tempFile = convertToFile(zipFile);
        List<MultipartFile> multipartFiles = new ArrayList<>();

        try (ZipFile zip = new ZipFile(tempFile)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    try (InputStream inputStream = zip.getInputStream(entry)) {
                        MultipartFile multipartFile = convertToMultipartFile(inputStream, entry.getName());
                        multipartFiles.add(multipartFile);
                    }
                }
            }

            // S3에 업로드하고 URL 리스트를 반환
            return s3Service.uploadImage(multipartFiles).stream()
                    .map(map -> map.get("imageUrl"))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to process the uploaded ZIP file.", e);
        } finally {
            tempFile.delete();
        }
    }


    // convertToFile
    private File convertToFile(MultipartFile file) {
        try {
            File tempFile = File.createTempFile("temp", null);
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(file.getBytes());
            }
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert MultipartFile to File", e);
        }
    }

    // convertToMultipartFile
    private MultipartFile convertToMultipartFile(InputStream inputStream, String fileName) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        // Content-Type 설정 (예: 이미지 파일의 경우 "image/jpeg" 등)
        String contentType = "image/png"; // 기본 Content-Type

        return new InputStreamMultipartFile(fileName, fileName, contentType, byteArrayOutputStream.toByteArray());
    }


    // ============================= convert type =============================
    public Post dtoToEntity(PostDTO postDTO, Member member) {
        Post post = Post.builder()
                .no(postDTO.getPostNo())
                .content(postDTO.getContent())
                .hashtag(postDTO.getHashtag())
                .commentCnt(postDTO.getCommentCnt())
                .images(s3Service.convertListToString2(postDTO.getImages()))
                .likeCnt(postDTO.getLikeCnt())
                .member(member)
                .build();
        return post;
    }

    public PostDTO entityToDto(Post post, Member member) {

        PostDTO postDTO = PostDTO.builder()
                .postNo(post.getNo())
                .content(post.getContent())
                .hashtag(post.getHashtag())
                .commentCnt(post.getCommentCnt())
                .images(s3Service.convertStringToList(post.getImages()))
                .likeCnt(post.getLikeCnt())
                .regDate(post.getRegDate())
                .modDate(post.getModDate())
                .memberNo(post.getMember().getNo())
                .nickname(post.getMember().getNickname())
                .code(post.getMember().getCountry().getCode())
                .currentMemberNo(member == null ? null : member.getNo())
                .currentMemberCode(member == null ? null : member.getCountry().getCode())
                .build();

        return postDTO;
    }


}