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

    @Builder.Default
    private String keyword = ""; // 검색 keyword
    @Builder.Default
    private String type = "content"; // 검색 type : content, hashtag
    @Builder.Default
    private Boolean myPost = false; // 1이면 내 게시글만 보기
}
