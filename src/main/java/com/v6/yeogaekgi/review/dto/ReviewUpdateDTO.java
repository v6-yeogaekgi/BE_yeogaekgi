package com.v6.yeogaekgi.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateDTO {
    private List<Integer> chooseImages;
    private Integer score;
    private String content;
}
