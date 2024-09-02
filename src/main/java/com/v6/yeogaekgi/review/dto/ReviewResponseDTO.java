package com.v6.yeogaekgi.review.dto;

import com.v6.yeogaekgi.member.entity.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDTO {
    private Long reviewId;
    private List<String> images;
    private List<String> thumbnail;
    private int score;
    private String content;
    private int status;
    private Long serviceId;  // 서비스 no
    private String serviceName; // 서비스 이름
    private String nickname;
    private Country country;
    private Timestamp regDate;
    private Timestamp modDate;
    private Long paymentId;
}
