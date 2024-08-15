package com.v6.yeogaekgi.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeDTO {

    // comment
    private Long postLikeNo;

    // post
    private Long postNo;

    // member
    private Long memberNo;

}