package com.v6.yeogaekgi.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchDTO {


    private String content; // 내용 검색
    private String hashtag; // 해시태그 검색
    @Builder.Default
    private Boolean myPost = false; // 1이면 내 게시글만 보기
}
