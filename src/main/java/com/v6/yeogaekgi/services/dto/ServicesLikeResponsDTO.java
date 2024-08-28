package com.v6.yeogaekgi.services.dto;

import lombok.*;

@ToString
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicesLikeResponsDTO {
    private Long id;

    private Long memberId;

    private Long servicesId;
}
