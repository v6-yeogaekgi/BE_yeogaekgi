package com.v6.yeogaekgi.review.entity;

import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.payTrack.entity.Payment;
import com.v6.yeogaekgi.services.entity.Services;
import com.v6.yeogaekgi.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"services","member","payment"})
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_no")
    private Long id;

    private String images;

    private String thumbnail;

    @Column(nullable=false)
    private int score;

    @Column(nullable=false)
    private String content;

    @Column(nullable=false)
    @ColumnDefault("0")
    private int status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_no", nullable = false)
    private Services services;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;


    @OneToOne(fetch = FetchType.LAZY,optional = true)
    @JoinColumn(name = "pay_no")
    private Payment payment;
}
