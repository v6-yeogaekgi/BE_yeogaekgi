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
public class CommentDTO {

    // comment
    private Long commentId;
    private String content;
    private Timestamp regDate,modDate;

    // post
    private Long postId;

    // member
    private Long memberId;
    private String nickname;

    // country
    private String code;

}