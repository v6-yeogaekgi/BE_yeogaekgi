package com.v6.yeogaekgi.services.dto;

import com.v6.yeogaekgi.services.entity.ServicesType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
@Getter
@Builder
public class ServicesDTO {
    private Long id;

    private ServicesType serviceType;

    private String name;

    private String content;

    private String address;

    private BigDecimal lat;

    private BigDecimal lon;

    private int likeCnt;

}
