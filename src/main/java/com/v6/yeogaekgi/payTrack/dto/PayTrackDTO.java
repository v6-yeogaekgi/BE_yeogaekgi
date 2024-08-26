package com.v6.yeogaekgi.payTrack.dto;

import lombok.*;

import java.sql.Timestamp;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayTrackDTO {
    private long userCardNo;

    // 통합 관리
    private Timestamp datetime;

    private Integer transitBalanceSnap;

    private Integer payBalanceSnap;

    // [ 결제(Payment) 내역 ]
    private Integer pno;

    private Integer payType; // 0결제 | 1교통

    private Integer payPrice;

    private Integer status; // 0 결제 취소 | 1 결제 완료

    private String serviceName;


    // [ 거래(Transaction) 내역 ]
    private Integer tno;

    private Integer tranType; // 0전환 | 1충전 | 2환급

    private Integer transferType; // 0페이 -> 교통 | 교통 -> 페이

    private Integer krwAmount;

    private Integer foreignAmount;

    private Integer currencyType;

    // 결제인지 거래인지
    private boolean isPayment;

}
