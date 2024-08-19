package com.v6.yeogaekgi.payTrack.dto;

import lombok.*;

import java.sql.Timestamp;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long payNo;

    private int payType;

    private int payPrice;


    private Integer payBalanceSnap;

    private Integer transitBalanceSnap;

    private Timestamp payDate;

    private int status;

    private String serviceName;

    // userCard
    private Long userCardNo;

    // member
    private Long memberNo;

    //Services
//    private Long serviceNo;
}
