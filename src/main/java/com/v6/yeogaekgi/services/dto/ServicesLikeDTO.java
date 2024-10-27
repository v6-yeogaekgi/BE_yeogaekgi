package com.v6.yeogaekgi.services.dto;

import lombok.*;

@ToString
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicesLikeDTO {
    private Long servicesLikeNo;

    private Long memberNo;

    private Long servicesNo;

    private String address;

    private String content;

    private Integer likeCnt;

    private String name;
    // 0: 전체 | 1: 관광지 | 2: 액티비티 | 3: 기타
    private Integer type;
}
