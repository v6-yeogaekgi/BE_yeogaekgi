package com.v6.yeogaekgi.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    // post
    private Long postNo;
    private String title;
    private String content;
    private String images;
    private String hashtag;
    private int likeCnt;
    private int commentCnt;
    private Timestamp regDate,modDate;

    // member
    private Long memberNo;
    private String nickname;
}
