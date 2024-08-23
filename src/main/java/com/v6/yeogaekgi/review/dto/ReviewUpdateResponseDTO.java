package com.v6.yeogaekgi.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateResponseDTO {
    private List<String> images;
    private List<String> thumbnails;
    private String content;
    private int score;
}
