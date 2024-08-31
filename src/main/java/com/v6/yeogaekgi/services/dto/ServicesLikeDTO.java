package com.v6.yeogaekgi.services.dto;

import lombok.*;

import java.sql.Timestamp;

@ToString
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicesLikeDTO {
    private Long servicesLikeId;

    private Long memberId;

    private Long servicesId;

    private String address;

    private String content;

    private Integer likeCnt;

    private String name;
    // 0: 전체 | 1: 관광지 | 2: 액티비티 | 3: 기타
    private Integer type;
}
