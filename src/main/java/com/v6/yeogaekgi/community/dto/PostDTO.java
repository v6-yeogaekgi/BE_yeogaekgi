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
    private Long postId;
    private String content;
    private String images;
    private String hashtag;
    private int likeCnt;
    private int commentCnt;
    private Timestamp regDate,modDate;

    // member
    private Long memberId;
    private String nickname;

    // service
    private Integer likeState; // 현재 접속한 유저가 좋아요한 게시글인지. 아니면0, 맞으면1
}
