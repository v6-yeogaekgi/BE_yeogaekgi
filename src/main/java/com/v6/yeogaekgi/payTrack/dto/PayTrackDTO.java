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

    // pay_date + tran_date 통합 관리
    private Timestamp datetime;

    // [ 결제(Payment) 내역 ]
    private Integer pno;

    private Integer payType; // 0결제 | 1교통

    private Integer payPrice;

    private Integer status; // 0 결제 취소 | 1 결제 완료

    private String serviceName;

    private Integer payment_transit_balance_snap;

    private Integer payment_pay_balance_snap;

    // [ 거래(Transaction) 내역 ]
    private Integer tno;

    private Integer tranType; // 0전환 | 1충전 | 2환급

    private Integer transfer_type; // 0페이 -> 교통 | 교통 -> 페이

    private Integer krw_amount;

    private Integer foreign_amount;

    private Integer transaction_transit_balance_snap;

    private Integer transaction_pay_balance_snap;

    // 결제인지 거래인지
    private boolean isPayment;

}
