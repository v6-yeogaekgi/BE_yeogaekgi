package com.v6.yeogaekgi.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ReviewRequestDTO {

    private String images;
    private int score;
    private String content;
    private int status;

    private Long serviceId;  // 서비스 no
    private Long memberId;   // 멤버 no

}
