package com.v6.yeogaekgi.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ReviewRequestDTO {
    private int score;
    private String content;
    private int status;
}
