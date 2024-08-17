package com.v6.yeogaekgi.card.entity;

import com.v6.yeogaekgi.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_no")
    private Long id;

    @Column(name = "pay_type", nullable = false)
    private int payType; // COMMENT '충전, 환급, 결제, 전환,'

    @Column(name = "pay_price", nullable = false)
    private int payPrice;

    @Column(name = "pay_balance_snap")
    private Integer payBalanceSnap;

    @Column(name = "transit_balance_snap")
    private Integer transitBalanceSnap;

    @Column(name = "pay_date", nullable = false)
    private Timestamp payDate;

    @Column(nullable = false)
    private int status;

    @Column(name = "service_name")
    private String serviceName;

    @ManyToOne
    @JoinColumn(name = "user_card_no", nullable = false)
    private UserCard userCard;

    @ManyToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

//    @ManyToOne
//    @JoinColumn(name = "service_no", nullable = false)
//    private Service service;
}
