package com.v6.yeogaekgi.community.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
@ToString
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    // post
    private Long postId;
    private String content;
    private List<String> images;
    private String hashtag;
    private int likeCnt;
    private int commentCnt;
    private Timestamp regDate,modDate;

    // member
    private Long memberId;
    private String nickname;
    private String code;

    // service
    private boolean likeState; // 현재 접속한 유저가 좋아요한 게시글인지. 아니면0, 맞으면1
    private Long currentMemberId;
    private String currentMemberCode;
}
