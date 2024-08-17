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
@Table(name = "user_card")
public class UserCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="user_card_no")
    private Long id;

    // 등록날짜 + 77일
    @Column(name = "exp_date" ,nullable=false)
    private Timestamp expDate;

    @Column(name="pay_balance", nullable=false)
    private int payBalance;

    @Column(name="transit_balance", nullable=false)
    private int transitBalance;

    @ManyToOne
    @JoinColumn(name = "card_no", nullable = false)
    private Card card;

    @ManyToOne
    @JoinColumn(name = "memeber_no", nullable = false)
    private Member member;

    @Column(nullable=false)
    // 0: 비활성화 1:활성화 2:삭제
    private int status;

    @Column(nullable=false)
    //0: 주카드 X 1:주카드 O
    //0: 주카드 X 1:주카드 O
    private int starred;
}
