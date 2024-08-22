package com.v6.yeogaekgi.payTrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayTrackRequestDTO {
    private Long userCardNo;
    private int year;
    private int month;
}
