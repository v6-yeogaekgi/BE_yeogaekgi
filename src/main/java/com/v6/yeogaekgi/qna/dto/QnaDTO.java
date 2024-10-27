package com.v6.yeogaekgi.qna.dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@ToString
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaDTO {

    // post
    private Long qnaNo;
    private String title;
    private String content;
    private List<String> images;
    private String reply;
    private Timestamp qnaDate,replyDate;
    private Boolean status;

    // member
    private Long memberNo;
    private String code;
}
