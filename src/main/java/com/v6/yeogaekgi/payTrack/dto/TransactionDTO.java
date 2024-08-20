package com.v6.yeogaekgi.payTrack.dto;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Long tranNo;

    private int tranType;

    private Timestamp tranDate;

    private Byte transferType;

    private Integer payBalanceSnap;

    private Integer transitBalanceSnap;

    private BigDecimal krwAmount;

    private BigDecimal foreignAmount;

    private int currencyType;

    // UserCard
    private Long userCardNo;

    // Member
    private Long memberNo;
}
