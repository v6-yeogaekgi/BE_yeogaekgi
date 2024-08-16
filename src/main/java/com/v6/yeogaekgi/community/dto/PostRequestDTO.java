package com.v6.yeogaekgi.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDTO {


    private String content; // 내용 검색
    private String hashtag; // 해시태그 검색
    private int page = 0; // 몇 page인지 요청
    private int myPost = 0; // 1이면 내 게시글만 보기
}
