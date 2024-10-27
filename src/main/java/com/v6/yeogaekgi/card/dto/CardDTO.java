package com.v6.yeogaekgi.card.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CardDTO {
    private Long userCardNo;
    private Long cardNo;
    private String design;
    private String cardName;
    private String area;
}
