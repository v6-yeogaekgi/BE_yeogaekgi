package com.v6.yeogaekgi.payTrack.entity;

import com.v6.yeogaekgi.card.entity.UserCard;
import com.v6.yeogaekgi.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tran_no")
    private Long id;

    @Column(name = "tran_type", nullable = false)
    private int tranType; // 0: 전환 | 1: 충전 | 2: 환급

    @CreationTimestamp
    @Column(name = "tran_date", nullable = false)
    private Timestamp tranDate;

    @Column(name = "transfer_type") // 0: 페이 -> 교통 | 1: 교통 -> 페이
    private Byte transferType;

    @Column(name = "pay_balance_snap")
    private Integer payBalanceSnap;

    @Column(name = "transit_balance_snap")
    private Integer transitBalanceSnap;

    @Column(name = "krw_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal krwAmount;

    @Column(name = "foreign_amount", precision = 10, scale = 2)
    private BigDecimal foreignAmount;

    @Column(name = "currency_type", nullable = false)
    private int currencyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_card_no", nullable = false)
    private UserCard userCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

    public void updatePayBalanceSnap(int payBalanceSnap) {this.payBalanceSnap = payBalanceSnap;}
}
