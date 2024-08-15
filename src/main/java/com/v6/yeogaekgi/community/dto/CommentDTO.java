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
    private Long commentNo;
    private String comment;
    private Timestamp regDate,modDate;

    // member
    private Long memberNo;
    private String nickname;

}
